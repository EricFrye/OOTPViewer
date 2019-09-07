package data;

import java.util.Map;

import boolean_logic.Quantifier;

public class StreakType_Length extends StreakType {
	
	private Quantifier filter;
	
	public StreakType_Length (int numFields, String filter) {
		
		super(numFields, null);
		
		this.filter = new Quantifier();
		this.filter.parseQuantifier(filter);
		
	}
	
	@Override
	public boolean isContinued(Map<String, Integer> mappings, String[] entity) {
		return this.filter.test(mappings, entity);
	}

	@Override
	public void addToStats(String[] fieldsOver, Map<String, Integer> mappings, String[] entity, String[] identityFields) {
		this.curStreak.addStats(entity, fieldsOver, mappings, StreakAddPolicy.LENGTH, identityFields);
	}

	public void streakEnded(Map <String, Integer> mappings) {
	
		if (this.curStreak.length() > this.streak.length()) {
			this.streak = this.curStreak;
		}

		this.curStreak = new StreakStats (this.curStreak, false);
		
	}
	
	public int getStreakLength () {
		return super.streakLength();
	}


	public void postHandleEntity(Map <String, Integer> mappings) {
		
	}
	
}
