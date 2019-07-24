package data;

import java.util.List;
import java.util.Map;

import misc.Utilities;
import query.Query;

public class Data {
	
	private String [][] data;
	private int numRows;
	private int numCols;
	
	/**
	 * The default number of entities is 100
	 *	@param numCols The number of columns
	 */
	public Data (int numCols) {
		this.numRows = 0;
		this.numCols = numCols;
		this.data = new String [100][this.numCols];
	}
	
	/**
	 * 
	 * @param numRows Number of entities
	 * @param numCols Number of fields
	 */
	public Data (int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.data = new String [this.numRows][this.numCols];
	}
	
	/**
	 * Increase the size of the matrix
	 */
	private void growMatrix () {
		
		String [][] newMatrix = new String [this.numRows*2][this.numCols];
		
		for (int curRow = 0; curRow < this.numRows; curRow++) {
			newMatrix[curRow] = this.data[curRow];
		}
		
		this.data = newMatrix;
		
	}
	
	/**
	 * Check if a row in this entity passes the query.  All queries are compounded with && (logical and)
	 * @param The row to query
	 * @param queries List of queries as parsed by LogicalStatement.parse
	 * @return The array corrresponding to the row if it passed the query, or null
	 * @throws Exception If a field is queried that is not included in this entity
	 */
	public String [] query (int row, Map <String, Integer> mappings, List <Query> queries) throws Exception {
		
		String [] entity = this.data[row];
		
		for (Query curQuery: queries) {

			String field = curQuery.getField();
			
			//the field included in the query is invalid
			if (!mappings.containsKey(field)) {
				throw new Exception ("The field " + field + " does not exist in this record");
			}
			
			else {
				
				String thisVal = entity[mappings.get(field)];
				
				if (!curQuery.comp(thisVal)) {
					return null;
				}
				
			}
			
		}
		
		return entity;
		
	}
	
	/**
	 * @return The number of filled out rows in the matrix
	 */
	public int numRows () {
		return this.numRows;
	}
	
	public int numCols() {
		return this.numCols;
	}
	
	public void addEntity (String [] row) {
		
		if (numRows == data.length) {
			growMatrix();
		}
		
		this.data[this.numRows] = row;
		this.numRows++;
		
	}
	
	/**
	 * @param row The index of the row to get
	 * @return A String array of the data. Null if row out of bounds
	 */
	public String [] getEntity (int row) {
		return (row < 0 ||row >= numRows) ? null : data[row];
	}
	
	/**
	 * @param row The row of data to return
	 * @return A String representing the row as a CSV
	 */
	public String asCSV (int row) {
		return String.join(",", data[row]) + "\n";
	}
	
	/**
	 * Performs a matrix tranpose of the data
	 * @return A new Data object with the data transposed
	 */
	public Data transpose () {
		
		Data transposed = new Data (this.numRows);
		
		//build each individual column as its own row
		for (int curColIndex = 0; curColIndex < this.numCols; curColIndex++) {
			
			String [] rowToAdd = new String [this.numRows];
			
			for (int curRowIndex = 0; curRowIndex < this.numRows; curRowIndex++) {
				rowToAdd[curRowIndex] = this.data[curRowIndex][curColIndex];
			}
			
			//add each column as an entity in the data
			transposed.addEntity(rowToAdd);
			
		}
		
		return transposed;
		
	}
	
	public String [] getColumn (int colNum) {
		
		String [] ret = new String [this.numRows];
		
		for (int i = 0; i < ret.length; i++) {
			ret[i] = this.data[i][colNum];
		}
		
		return ret;
		
	}
	
	//TODO write a column
	public void writeColumn (int colNum, String [] col) {
		
		for (int i = 0; i < col.length; i++) {
			this.data[i][colNum] = col[i];
		}
		
	}
	
	/**
	 * Insertion of data for a join operation
	 * @param data String array of main joiner
	 * @param otherData String arary of other joiner
	 * @param skipIndex Index from otherData that should be skipped
	 */
	public void enterJoinEntity (String [] data, String [] otherData) {
		
		String [] ent = new String [data.length + otherData.length];
		
		for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
			ent[dataIndex] = data[dataIndex];
		}
				
		for (int otherDataIndex = 0; otherDataIndex < otherData.length; otherDataIndex++) {
			ent[otherDataIndex + data.length] = otherData[otherDataIndex];	
		}
		
		this.addEntity(ent);
		
	}
	
	/**
	 * @param col
	 * @return The length of the longest field value for a specified column
	 */
	public int lengthOfLongestEntry(int col) {
		
		int max = 0;
		
		for (int curRowIndex = 0; curRowIndex < this.numRows; curRowIndex++) {
			max = Math.max(max, data[curRowIndex][col].length());
		}
		
		return max;
		
	}
	
	/**
	 * Performs a sort on a single field
	 * @param fieldOn
	 * @param isDesc
	 */
	public void sortData (String fieldOn, Map <String, Integer> mappings, Type [] types, boolean isAsc) {
		sortHelper(fieldOn, mappings, types, 0, this.numRows-1, isAsc);
	}
	
	private void sortHelper (String fieldOn, Map <String, Integer> mappings, Type [] types, int low, int high, boolean isAsc) {
		
		if (low < high) {
			
			int partIndex = partition(fieldOn, mappings, types, low, high, isAsc);
			sortHelper(fieldOn, mappings, types, low, partIndex-1, isAsc);
			sortHelper(fieldOn, mappings, types, partIndex+1, high, isAsc);
			
		}
		
	}
	
	private int partition (String fieldOn, Map <String, Integer> mappings, Type [] types, int low, int high, boolean isAsc) {
		
		int i = low-1;
		
		for (int j = low; j < high; j++) {
			
			int colIndex = mappings.get(fieldOn);
			
			if (Utilities.shouldSwap(this.data[high][colIndex], this.data[j][colIndex], isAsc, types[colIndex].equals(Type.convert("S")))) {
				
				i++;
				
				String [] temp = this.data[i];
				this.data[i] = this.data[j];
				this.data[j] = temp;
				
			}
			
		}
		
		String [] temp = this.data[i+1];
		this.data[i+1] = this.data[high];
		this.data[high] = temp;
		
		return i+1;
		
	}
	
}
