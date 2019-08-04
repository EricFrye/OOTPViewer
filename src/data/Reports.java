package data;

import java.util.Map;

import query.QueryResult;

public class Reports {
	
	public static Map <String, Object> teamRecordReport (String city, String nickname) {
		
		String teamLoadCondition = String.format("name=%s&&nickname=%s", city, nickname);
		String teamID = QueryResult.getTopField("data", "teams", "team_id", teamLoadCondition);
		
		String games = String.format("home_team=%s%%away_team=%s", teamID, teamID);
		
		Holder result = new Holder ("data", "games");
		try {
			result.loadInfo(games);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result = result.select("game_id,home_team,away_team,runs0,runs1,hits0,hits1,errors0,errors1");
		result.sort("game", true);
		
		return null;
		
	}
	
	public static void main (String [] args) {
		
		teamRecordReport("Philadelphia", "Sabres");
		
	}
	
}
