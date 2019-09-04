package data;

import java.util.Map;

import query.QueryResult;

public class Reports {
	
	public static Double [][] playerHittingStreak (String first, String last) {
		
		String players = String.format("first_name=%s AND last_name=%s", first, last);
		String playerID = QueryResult.getTopField("data", "players", "player_id", players);
		
		String games = String.format("player_id=%s", playerID);
		
		Holder result = new Holder ("data", "players_game_batting");
		
		try {
			result.loadInfo(games);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Streak streak = new Streak (result.mappings,"h,k,d,t,hr,r,rbi,sb,bb,wpa".split(","), "Length", "k >= 1");
		result.processStreak(streak);
		
		return streak.getStreakStats();
		
	}
	
	public static void main (String [] args) {
		
		Double [][] result = playerHittingStreak("Mark","Silfies");
		
		for (String cur: "h,k,d,t,hr,r,rbi,sb,bb,wpa".split(",")) {
			System.out.print(String.format("%4s", cur) + " ");
		}
		
		System.out.println();
		
		for (int i = 0; i < result.length; i++) {
			
			for (int j = 0; j < result[0].length; j++) {
				System.out.print(String.format("%1.2f", result[i][j]) + " ");
			}
			
			System.out.println("\n");
			
		}
		
	}
	
}
