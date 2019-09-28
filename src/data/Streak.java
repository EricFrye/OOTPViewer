package data;

import java.util.Map;

import UI.DataTable;
import misc.Utilities;

public class Streak {

	private String [] fieldsOver; //the name of the fields to capture over this streak
	private StreakType type; //governs the streak continued and stat adding policy
	private boolean done;
	private Map <String, Integer> mappings; //mappings from the holder that the streak runs over
	private String [] identityFields; //fields used to uniquely identify a streak entity.  should be included with stats
	
	public Streak (Map <String, Integer> mappings, String [] fieldsOver, String [] identityFields, String type, Object ...otherParams) {
		
		this.done = false;
		this.fieldsOver = fieldsOver;
		this.mappings = mappings;
		this.identityFields = identityFields;
		
		if (type.equals("Amount")) {
			
			if (otherParams.length != 2) {
				throw new IllegalArgumentException();
			}
			
			int colIndex = -1;
			
			for (int curFieldOverIndex = 0; curFieldOverIndex < fieldsOver.length; curFieldOverIndex++) {
				
				if (fieldsOver[curFieldOverIndex].equals((String)otherParams[0])) {
					colIndex = curFieldOverIndex;
					curFieldOverIndex = fieldsOver.length;
				}
				
			}
			
			this.type = new StreakType_Amount(fieldsOver.length + identityFields.length, (String)otherParams[0], colIndex + identityFields.length, (Integer)otherParams[1]);
			
		}
		
		else if (type.equals("Length")) {
			
			this.type = new StreakType_Length(fieldsOver.length, (String)otherParams[0]);
			
		}
		
		else {
			this.type = null;
		}
		
	}
	
	/**
	 * Process the entity as it relates to this streak.  DOES NOT check if the identity for this entity matches with this Streak.  This should be done before the call.  A map of StreakIdentity to Streak would ensure the right function calls on the proper objects
	 */
	public void handleEntity (String [] ent) {
		
		//check for the continuation of the streak
		if (this.type.isContinued(mappings, ent)) {
			this.type.addToStats(this.fieldsOver, this.mappings, ent, this.identityFields);
		}
		
		else {
			this.type.streakEnded(mappings);
		}
		
		this.type.postHandleEntity(mappings);
		
	}
	
	public void end () {
		this.type.streakEnded(this.mappings);
		this.done = true;
	}
	
	/**
	 * 
	 * @return The stats for this streak or null if Streak.end has not been called
	 */
	public Double [][] getStreakStats () {
		return this.done ? type.streak.getStreakStats() : null;
	}
	
	/**
	 * 
	 * @return The results of the streak ready to be displayed
	 */
	public DataTable getStreakResultsUI () {
		
		String [] headers = new String [fieldsOver.length + identityFields.length];
		
		Utilities.copyArrayContent(headers, identityFields, 0);
		Utilities.copyArrayContent(headers, fieldsOver, identityFields.length);
		
		Object [][] data = this.type.streak.getStreakStats();
		
		return new DataTable(data, headers);
		
	}
	
}
