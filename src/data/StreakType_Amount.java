package data;

import java.util.Map;

public class StreakType_Amount extends StreakType {

	private String fieldMaximize;
	private Double bestAmount;
	
	public StreakType_Amount (int numFields, String fieldMaximize, int gameSpan) {
		
		super(numFields, gameSpan);
		this.fieldMaximize = fieldMaximize;
		this.bestAmount = 0.0;
		
	}
	
	@Override
	public boolean isContinued(Map<String, Integer> mappings, String[] entity) {
		return true;
	}

	@Override
	public void addToStats(String[] fieldsOver, Map<String, Integer> mappings, String[] entity) {
		this.curStreak.addStats(entity, fieldsOver, mappings, StreakAddPolicy.AMOUNT);
	}

	public void streakEnded(Map <String, Integer> mappings) {
	
		Double curAmount = this.curStreak.summarize(this.fieldMaximize, mappings);
		
		//update the best seen streak
		if (this.bestAmount < curAmount) {
			this.streak = this.curStreak;
			this.bestAmount = curAmount;
		}
		
		this.curStreak = new StreakStats (this.streak); //get ready for the next streak
		
	}
	
	public int span () {
		return super.streakLength();
	}

}
