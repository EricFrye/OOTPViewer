package data;

import java.util.Date;
import java.util.Map;

public class Streak {

	private int startGameID;
	private int endGameID;
	private String [] fieldsOver; //the name of the fields to capture over this streak
	private StreakType type;
	private boolean done;
	private Map <String, Integer> mappings;
	
	public Streak (Map <String, Integer> mappings, String [] fieldsOver, String type, Object ...otherParams) {
		
		this.done = false;
		this.fieldsOver = fieldsOver;
		this.mappings = mappings;
		
		if (type.equals("Amount")) {
			
			if (otherParams.length != 2) {
				throw new IllegalArgumentException();
			}
			
			this.type = new StreakType_Amount(fieldsOver.length, (String)otherParams[0], (Integer)otherParams[1]);
			
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
		
		System.out.println("Streak is at: " + type.curStreak.length());
		
		//check for the continuation of the streak
		if (this.type.isContinued(mappings, ent)) {
			System.out.println("Streak continued!");
			this.type.addToStats(this.fieldsOver, this.mappings, ent);
		}
		
		else {
			System.out.println("Streak ended...");
			this.type.streakEnded(mappings);
		}
		
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
	
}
