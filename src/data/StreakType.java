package data;

import java.util.Map;

import boolean_logic.Quantifier;

public abstract class StreakType {
	
	protected StreakStats streak;
	protected StreakStats curStreak;
	
	public StreakType (int numFields, Integer length) {
		
		this.streak = new StreakStats (numFields+1, length);
		this.curStreak = new StreakStats (numFields+1, length);
		
	}
	
	public abstract boolean isContinued (Map <String, Integer> mappings, String [] entity);
	public abstract void addToStats (String [] fieldsOver, Map <String, Integer> mappings, String [] entity);
	public abstract void streakEnded (Map <String, Integer> mappings);
	
	public int streakLength () {
		return this.streak.length();
	}
	

	
}
