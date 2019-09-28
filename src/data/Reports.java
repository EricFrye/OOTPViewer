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
		
		Streak streak = new Streak (result.mappings,playersHittingStreakFields, new String [] {"game_id"}, "Length", otherParams);
		result.processStreak(streak);
		
		return streak.getStreakResultsUI();
		
	}
	
	public static Double [][] playerHomersInSpanStreak (String first, String last) {
		
		String players = String.format("first_name=%s AND last_name=%s", first, last);
		String playerID = QueryResult.getTopField("data", "players", "player_id", players);
		
		String games = String.format("player_id=%s AND split_id=0", playerID);
		
		Holder result = new Holder ("data", "players_game_batting");
		
		try {
			result.loadInfo(null, games);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Streak streak = new Streak (result.mappings, playersHittingStreakFields, new String [] {"game_id"}, "Amount", "h", 4);
		result.processStreak(streak);
		
		return streak.getStreakStats();
		
	}
	
	public static void main (String [] args) {
		
		Double [][] result = playerHomersInSpanStreak("Mark","Silfies");
		
		System.out.print(String.format("%-8s", "game") + " ");
		
		for (String cur: playersHittingStreakFields) {
			System.out.print(String.format("%-8s", cur) + " ");
		}
		
		System.out.println();
		
		for (int i = 0; i < result.length; i++) {
			
			for (int j = 0; j < result[0].length; j++) {
				System.out.print(String.format("%06.2f", result[i][j]) + " ");
			}
			
			System.out.println("\n");
			
		}
		
	}
	
}
