package Data;

import java.util.List;
import java.util.Map;

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
	
}
