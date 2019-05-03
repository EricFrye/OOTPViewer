package Data;
import java.util.*;
import java.util.regex.Pattern;

import query.LogicalStatement;
import query.Query;
import query.QueryResult;

import misc.Utilities;

import java.io.*;

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
	 * @param fileName the name of the file to load
	 */
	public Holder (String directoryPath, String fileName) {
		
		this.mappings = new HashMap <String, Integer> ();
		this.directoryPath = directoryPath;
		this.fileName = fileName;
		this.types = Utilities.loadTypes("settings\\fileData", "team_record_history.csv");
		
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
	 */
	public void loadInfo () throws Exception {
		
		List <File> files = findAllFiles();
		Thread [] workers = new Thread [files.size()];
		
		long startTime = System.currentTimeMillis();
		
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
			
			FileLoader curWorker = new FileLoader (curFile, curInput, this.data, this.mappings);
			workers[i] = new Thread (curWorker);
			
		}
		
		
		for (Thread curWorker: workers) {
			curWorker.start();
		}

		for (Thread curWorker: workers) {
			curWorker.join();
		}
		
		
		System.out.println(String.format("Run time for loading %s was %f seconds.", this.fileName, (System.currentTimeMillis()-startTime)/1000.0));

	}
	
	/**
	 * Performs a query
	 * @param query The string representation of the query
	 * @return A query result object
	 */
	public QueryResult query (String query) {
		
		List <Query> queries = LogicalStatement.parse(query);
		Data ret = new Data (this.types.length);
		
		for (int curDataRow = 0; curDataRow < data.numRows(); curDataRow++) {
			
			try {
				
				String [] ent = data.query(curDataRow, mappings, queries);
				
				if (ent != null) {
					ret.addEntity(ent);
				}
				
			}
			catch (Exception e) {
				return null;
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
	
	public static void main (String [] args) {
		
		String dir = "C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv";
		String file = "players_game_batting";
		
		Holder players = new Holder (dir, file);
		try {
			players.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
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
		
		String [] ret = new String [mappings.size()];
		
		for (String curField: mappings.keySet()) {
			ret[mappings.get(curField)] = curField;
		}
		
		return ret;
		
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
		
		String [] fields = str.split(","); 
		String [] ret = new String [mappings.size()];
		
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
	
	
}
