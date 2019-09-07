package data;

import java.util.Map;

import misc.Utilities;

public class StreakStats {
	
	private Double [][] stats;
	private int numRows;
	
	public StreakStats (int numFields, Integer numRows) {
		
		this.numRows = 0;
		this.stats = new Double [numRows == null ? 10 : numRows][numFields];
	
	}

	/**
	 * Mimics the structure for the stats matrix but doesn't copy the stats
	 * @param sample
	 */
	public StreakStats (StreakStats sample, boolean copyValues) {
		
		this.numRows = 0;
		this.stats = new Double [sample.sizeStats()][sample.numFields()];
		
		if (copyValues) {
			this.stats = sample.stats.clone();
			this.numRows = sample.length();
		}
		
	}
	
	public Double [][] getStreakStats () {
		
		Double [][] ret = new Double [this.numRows][];
		
		for (int curStatIndex = 0; curStatIndex < this.numRows; curStatIndex++) {
			ret[curStatIndex] = this.stats[curStatIndex];
		}
		
		return ret;
		
	}

	/**
	 * 
	 * @return The number of rows in the stats matrix.  NOT the number of entities
	 */
	public int sizeStats () {
		return stats.length;
	}
	
	/**
	 * 
	 * @return The amount of fields tracked
	 */
	public int numFields () {
		return stats[0].length;
	}

	/**
	 * 
	 * @return The number of entities
	 */
	public int length () {
		return numRows;
	}
	
	public Double [] getRow () {
		return this.stats[0];
	}
	
	public Double [] getRow (int row) {
		return row < 0 || row >= stats.length ? null : this.stats[row];
	}
	
	/**
	 * @param entToAdd
	 * @param mappings
	 * @param addPolicy Potential values - Length,Amount
	 */
	public void addStats (String [] entToAdd, String [] fields, Map <String, Integer> mappings, StreakAddPolicy addPolicy, String [] identityFields) {

		//build the stat row that will be added
		Double [] newStats = new Double [fields.length + identityFields.length];
		
		//add the identity fields to the front
		for (int curIdentityFieldIndex = 0; curIdentityFieldIndex < identityFields.length; curIdentityFieldIndex++) {
			newStats[curIdentityFieldIndex] = Double.parseDouble(entToAdd[mappings.get(identityFields[curIdentityFieldIndex])]);
		}
		
		//add the stats we are tracking after the identity fields
		for (int curFieldIndex = 0; curFieldIndex < fields.length; curFieldIndex++) {
			newStats[curFieldIndex + identityFields.length] = Double.parseDouble(entToAdd[mappings.get(fields[curFieldIndex])]);
		}
		
		if (addPolicy.equals(StreakAddPolicy.LENGTH)) {
			
			//we only need to worry about increasing the space for length streaks - a amount streak always has the same tracking
			if (numRows == stats.length) {
				this.stats = (Double [][])Utilities.growMatrix(stats);
			}
			
			this.stats[this.numRows] = newStats;
			this.numRows++;
			
		}
		
		else if (addPolicy.equals(StreakAddPolicy.AMOUNT)) {
			
			//if the stat tracking is full then we need to shift objects index 1 and higher up one spot to create an opening for this new entitity
			if (this.numRows == stats.length) {
				
				//do the shift
				for (int curRowIndex = 1; curRowIndex < stats.length; curRowIndex++) {
					this.stats[curRowIndex-1] = this.stats[curRowIndex];
				}
				
			}
			
			//we still have space so we will not replace an old
			else {
				this.numRows++;
			}
			
			//insert the new set of stats into the open spot
			this.stats[this.numRows-1] = newStats;
			
		}
		
		else {
			System.out.println(addPolicy.toString() + " is unimplemented for the function StreakStats.addStats");
			return;
		}
		
	}
	
	/**
	 * 
	 * @param colIndex The index of the field to summarize
	 * @return Count of the stat
	 */
	public Double summarize (int colIndex) {
		
		Double ret = 0.0;
		
		for (int curStatIndex = 0; curStatIndex < this.numRows; curStatIndex++) {
			ret += stats[curStatIndex][colIndex];
		}
		
		return ret;
		
	}
	
}
