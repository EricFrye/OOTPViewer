package data;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import boolean_logic.Quantifier;
import misc.Utilities;
import query.Operations;
import query.QueryParser;
import query.QueryResult;

/***
 * InfoHolder is the main mechanism for loading and accessing the data files
 * @author Eric
 *
 */
public class Holder {
	
	protected Map <String, Integer> mappings; //maps the col names to their index. the index in the list is the index in the report
	protected Data data; //holds the actual data
	protected Type [] types; //stores the type for each field
	private String directoryPath;
	private String fileName;
	
	/***
	 * Creates the InfoHolder object
	 * @param directoryPath the path to the target data file
	 * @param fileName the name of the file to load (no file extension)
	 */
	public Holder (String directoryPath, String fileName) {
		
		this.mappings = new HashMap <String, Integer> ();
		this.directoryPath = directoryPath;
		this.fileName = fileName;
		this.types = Utilities.loadTypes("settings\\fileData", fileName + ".csv");
		
	}
	
	/**
	 * Call from child
	 * @param directoryPath path to directory fileName is in
	 * @param fileName name of file 
	 * @param mappings
	 * @param data
	 * @param types types for the entities
	 */
	protected Holder (Map <String, Integer> mappings, Data data, Type [] types) {
		this.directoryPath = "";
		this.fileName = "";
		this.mappings = mappings;
		this.types = types;
		this.data = data;
	}
	
	/**
	 * Standard loading function.  All columns are kept
	 * @param condition The string representation of a query to check against the loaded values. A value of null means there is no condition to check against
	 */
	public void loadInfo (String condition) throws Exception {
		
		List <File> files = FileLooker.findFiles(this.fileName, new File(this.directoryPath));
		Thread [] workers = new Thread [files.size()];
		
		Quantifier queries = new Quantifier();
		
		if (condition != null) {
			queries.parseQuantifier(condition);
		}
		
		ExecutorService pool = Executors.newFixedThreadPool(8);
		
		for (int i = 0; i < files.size(); i++) {
			
			File curFile = files.get(i);
			Scanner curInput = new Scanner (curFile);
			
			//the first file has header info, we need this before anything can start
			if (i == 0) {
				
				String header = curInput.nextLine();
				String [] headers = header.split(",");

				//fill out the mappings structure
				for (int j = 0; j < headers.length; j++) {
					this.mappings.put(headers[j], j);
				}
				
				this.data = new Data (headers.length);
				
			}
			
			Runnable curWorker = new FileLoader (curFile, curInput, this.data, this.mappings, queries);
			pool.execute(curWorker);
			
		}
		
		pool.shutdown();

	}
	
	/**
	 * Performs a query
	 * @param query The string representation of the query
	 * @return A query result object
	 */
	public QueryResult query (String query) {
		
		Quantifier queries = new Quantifier (); 
		queries.parseQuantifier(query);
		
		Data ret = new Data (this.types.length);
		
		for (int curDataRow = 0; curDataRow < data.numRows(); curDataRow++) {
			
			String [] curEntity = this.data.getEntity(curDataRow);
			
			if (queries.test(this.mappings, curEntity)) {
				ret.addEntity(curEntity);
			}
			
		}
		
		return new QueryResult(ret, queries, this.mappings, this.types);
		
	}
	
	//TODO
	public String buildReport () {
		
		String ret = "";
	
		
		return ret;
		
	}
	
	/**
	 * @return The count of entries in this Holder
	 */
	public int numEntities () {
		return data.numRows();
	}
	
	/**
	 * @param directoryPath String representation of the path to the directory the target files are currently in.  No trailing /
	 * @param mainFileName Name of file to search for splits of.  The splits will be of the form mainFileName_\d+
	 * @return An List of all the files matching the parameters
	 */
	private List <File> findAllFiles () {
		
		File searchDir = new File (this.directoryPath);
		File [] allFilesInDir = searchDir.listFiles();
		
		String pattern = String.format("^%s(_\\d+){0,1}.csv$", this.fileName);
		List <File> matchedFiles = new LinkedList <File> ();
		
		for (File curFile: allFilesInDir) {
			
			if (curFile.getName().matches(pattern)) {
				matchedFiles.add(curFile);
			}
			
		}
		
		return matchedFiles;
		
	}
	
	
	public String asCSV () {
		
		String ret = "";
		
		ret += String.join(",", this.mappings()) + "\n";
		
		for (int curRow = 0; curRow < this.data.numRows(); curRow++) {
			ret += data.asCSV(curRow);
		}
		
		return ret;
		
	}

	
	protected Data getData () {
		return data;
	}
	
	protected String [] getTop () {
		return data.numRows() == 0 ? null : data.getEntity(0);
	}
	
	/**
	 * 
	 * @return The mappings in sorted order
	 */
	public String [] mappings () {
		
		String [] ret = new String [mappings.keySet().size()];
		
		for (String curField: mappings.keySet()) {
			ret[mappings.get(curField)] = curField;
		}
		
		return ret;
		
	}
	
	/**
	 * @param curField Name of the field
	 * @return True if the map has this field, false otherwise
	 */
	public boolean containsField (String curField) {
		return this.mappings.containsKey(curField);
	}
	
	/**
	 * 
	 * @param index The index of the Entity that is desired
	 * @return A copy of the string array representation of the Entity's data
	 */
	public String [] getEntityVal (int index) {
		return data.numRows() == 0  || index >= data.numRows() ? null : data.getEntity(index);
	}
	
	/**
	 * Sums the columns of this holder corresponding to the string passed in
	 * @param str Of the form BEGIN_REGEX([\w+])([,\w+])*END_REGEX
	 * @return A string of a length of the amount of fields in the Holder
	 */
	public String [] summarize (String str) {
		
		String [] ret = new String [mappings.size()];
		
		if (str.length() == 0) {
			return ret;
		}
		
		String [] fields = str.split(","); 
		
		//outerloop over the fields so we only have to access the index once
		for (String curField: fields) {
			
			int dataIndex = mappings.get(curField);
			double total = 0; 
			String [] col = this.data.getColumn(dataIndex);
			
			for (int curRow = 0; curRow < col.length; curRow++) {
				total += Double.parseDouble(col[curRow]);
			}
			
			ret[dataIndex] = types[dataIndex].equals(Type.DOUBLE) ? String.format("%.2f", total) : String.format("%d", (int)total);
			
		}
		
		return ret;
		
	}
	
	/**
	 * Selects the specified fields
	 * @param str Of the form BEGIN_REGEX([\w+])([,\w+])*END_REGEX
	 * @return A new Holder object that has entities with fields only specified by str
	 */
	public Holder select (String str) {
		
		String [] newFields = str.split(",");
		Map <String, Integer> newMappings = new HashMap <String, Integer> ();
		Data newData = new Data (this.data.numRows(), newFields.length);
		Type [] newTypes = new Type [newFields.length];
		
		//select the new types
		for (int newTypeIndex = 0; newTypeIndex < newFields.length; newTypeIndex++) {
			String curField = newFields[newTypeIndex];
			int curFieldIndex = this.mappings.get(curField);
			Type curFieldType = this.types[curFieldIndex];
			newTypes[newTypeIndex] = curFieldType;
		}
		
		//build the new mapping map
		for (int newMappingsIndex = 0; newMappingsIndex < newTypes.length; newMappingsIndex++) {
			String curField = newFields[newMappingsIndex];
			newMappings.put(curField, newMappingsIndex);
		}
		
		//build the new entities list
		for (int curFieldIndex = 0; curFieldIndex < newFields.length; curFieldIndex++) {
			
			String curField = newFields[curFieldIndex];
			int colIndex = this.mappings.get(curField);
			String [] curFields = this.data.getColumn(colIndex);
			newData.writeColumn(curFieldIndex, curFields);
			
		}
		
		return new Holder (newMappings, newData, newTypes);
		
	}
	
	public int recommendedTableWidth () {
		return this.types.length * 25;
	}
	
	/**
	 * 
	 * @return The amount of fields in the data
	 */
	public int numFields () {
		return this.mappings.size();
	}
	
	/**
	 * Gets the type corresponding to the given column
	 * @param index The index
	 * @return The type, or null if the index is OOB
	 */
	public Type getType (int index) {
		return index < 0 || index >= types.length ? null : types[index];
	}
	
	/**
	 * @param field Name of the field searching for
	 * @return Index that field is mapped to in this Holder
	 */
	public int getFieldIndex (String field) {
		return this.mappings.containsKey(field) ? this.mappings.get(field) : -1;
	}
	
	public Holder join (Holder other, String fieldOn) throws IllegalArgumentException {
		
		if (!this.containsField(fieldOn) || !other.containsField(fieldOn)) {
			throw new IllegalArgumentException ("Field not contained in table.");
		}
		
		int joinIndex = this.getFieldIndex(fieldOn);
		int otherJoinIndex = other.getFieldIndex(fieldOn);
		
		//create the new types array
		int numNewFields = this.numFields() + other.numFields();
		Type [] joinedTypes = new Type [numNewFields];
		
		for (int typeIndex = 0; typeIndex < this.numFields(); typeIndex++) {
			joinedTypes[typeIndex] = getType(typeIndex);
		}
		
		for (int typeIndex = 0; typeIndex < other.numFields(); typeIndex++) {
			joinedTypes[typeIndex+this.numFields()] = other.getType(typeIndex);
		}
		
		//create the mappings hashmap
		//String [] orderedFields = this.mappings();
		//String [] otherOrderedFields = other.mappings();
		
		String [] joinedOrderedFields = this.createNewOrderedFields(other);
		Map <String, Integer> joinedMappings = new HashMap <String, Integer> (); 
		
		for (int curJoinedOrderedIndex = 0; curJoinedOrderedIndex < joinedOrderedFields.length; curJoinedOrderedIndex++) {
			joinedMappings.put(joinedOrderedFields[curJoinedOrderedIndex], curJoinedOrderedIndex);
		}
		
		//for (int curOrderedIndex = 0; curOrderedIndex < orderedFields.length; curOrderedIndex++) {
		//	joinedMappings.put(orderedFields[curOrderedIndex], curOrderedIndex);
		//}
		
		//for (int curOrderedIndex = 0; curOrderedIndex < otherOrderedFields.length; curOrderedIndex++) {
		//	joinedMappings.put(otherOrderedFields[curOrderedIndex], orderedFields.length+curOrderedIndex);	
		//}
		
		Data joinedData = new Data (joinedTypes.length);
		
		for (int curDataIndex = 0; curDataIndex < this.data.numRows(); curDataIndex++) {
			
			String [] dataEnt = this.data.getEntity(curDataIndex);
			
			for (int curOtherDataIndex = 0; curOtherDataIndex < other.data.numRows(); curOtherDataIndex++) {
				
				String [] otherDataEnt = other.data.getEntity(curOtherDataIndex);
				
				if (dataEnt[joinIndex].equals(otherDataEnt[otherJoinIndex])) {
					joinedData.enterJoinEntity(dataEnt, otherDataEnt);
				}
			}
			
		}
		
		return new Holder (joinedMappings, joinedData, joinedTypes);
		
	}
	
	/**
	 * Generate the mappings for this and other being joined together. The fields in this are first and other second.  Any duplicate named fields will have the lowest available number appended to the end of the field name
	 * @param other
	 * @return String array of the new fields
	 */
	public String [] createNewOrderedFields (Holder other) {
		
		String [] orderedFields = this.mappings();
		String [] otherOrderedFields = other.mappings();
		
		for (String curOtherField: otherOrderedFields) {
			orderedFields = Utilities.insert(orderedFields, curOtherField);
		}
		
		return Utilities.shrinkArray(orderedFields);
		
	}
	
	/**
	 * @param field
	 * @return The minimum column width in pixels that will show all data for this column
	 */
	public int recommendedColumnWidth (String field) {
		
		final int mult = 10;
		int entryLen = recommendedColumnWidth(this.mappings.get(field));
		return mult * Math.max(field.length(), entryLen);
		
	}
	
	private int recommendedColumnWidth (int col) {
		return data.lengthOfLongestEntry(col);
	}
	
	public void sort (String [] fieldOn, boolean isAsc) {
		this.data.sortData(fieldOn, this.mappings, this.types, isAsc);
	}
	
	public static void main (String [] args) {
		
		String path = "D:\\Java_Projects\\OOTPViewer\\data";
		String command = "players_game_batting";
		
		Holder game_logs = new Holder (path, command);
		
		try {
			game_logs.loadInfo("player_id = 14");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		game_logs.sort(new String [] {"year", "game_id"}, true);
		
	}
	
}
