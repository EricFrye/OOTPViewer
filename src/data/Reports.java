package data;

import UI.DataTable;
import query.QueryResult;

public class Reports {
	
	public static String [] playersHittingStreakFields = new String [] {"h","k","d","t","hr","r","rbi","sb","bb","wpa"};
	
	public static DataTable playerHittingStreak (String playerID, String type, Object ... otherParams) {
		
		String games = String.format("player_id=%s", playerID);
		
		Holder result = new Holder ("data", "players_game_batting");
		
		try {
			result.loadInfo(null, games);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Streak streak = new Streak (result.mappings,playersHittingStreakFields, new String [] {"game_id"}, type, otherParams);
		streak.processStreak(result);
		
		return streak.getStreakResultsUI();
		
	}
	
	public static void main (String [] args) {
		
		
		
	}
	
}
