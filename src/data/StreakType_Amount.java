package data;

import java.util.Map;

public class StreakType_Amount extends StreakType {

	private String fieldMaximize;
	private int colMaximize;
	private Double bestAmount;
	
	public StreakType_Amount (int numFields, String fieldMaximize, int colMaximize, int gameSpan) {
		
		super(numFields, gameSpan);
		this.fieldMaximize = fieldMaximize;
		this.colMaximize = colMaximize;
		this.bestAmount = 0.0;
		
	}
	
	@Override
	public boolean isContinued(Map<String, Integer> mappings, String[] entity) {
		return true;
	}

	@Override
	public void addToStats(String[] fieldsOver, Map<String, Integer> mappings, String[] entity, String[] identityFields) {
		this.curStreak.addStats(entity, fieldsOver, mappings, StreakAddPolicy.AMOUNT, identityFields);
	}

	//should never be called
	public void streakEnded(Map <String, Integer> mappings) {
	
		
		
	}
	
	public int span () {
		return super.streakLength();
	}

	
	public void postHandleEntity(Map <String, Integer> mappings) {
		
		Double curAmount = this.curStreak.summarize(this.colMaximize);
		
		//update the best seen streak
		if (this.bestAmount < curAmount) {
			this.streak = this.curStreak;
			this.bestAmount = curAmount;
			this.curStreak = new StreakStats(this.streak, true);
			System.out.println("Best:" + bestAmount);
		}
		
	}

}
