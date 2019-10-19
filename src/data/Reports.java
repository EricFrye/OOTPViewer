package data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import UI.DataTable;

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
	
	public static Map <Integer, Streak> playerStreakByTeam (String year, String teamID, String [] fieldsOver, String [] identityFields, String type, Object ... otherParams) {
		
		Map <Integer, Streak> results = new HashMap <Integer, Streak> ();
		
		String dataDir = String.format("data\\dump_%s_yearly", year);
		String fileName = "players_game_batting";
		
		//load the data
		Holder data = new Holder (dataDir, fileName);
		data.loadInfo(null, String.format("team_id=%s", teamID));
		data.sort(new String [] {"player_id","game_id"}, true); //we need to sort in a manner that enables a single pass over to calculate all the streaks
		
		String identityField = "player_id";
		
		Streak curStreak = null;
		int curID = -1; //specifies the entity that the streak is for
		
		//first pass over the data to collect all the identifier values
		for (int curEntIndex = 0; curEntIndex < data.numEntities(); curEntIndex++) {
			
			int curEntID = Integer.parseInt(data.getEntityVal(curEntIndex, identityField));
			
			//create a new streak
			if (curID != curEntID) {
				
				//finalize the old streak before replacing it
				if (curStreak != null) {
					
					curStreak.end();
					results.put(curID, curStreak);
					
				}
				
				curID = curEntID;
				curStreak = new Streak (data.mappings, fieldsOver, identityFields, type, otherParams);
				
			}
			
			curStreak.handleEntity(data.getEntity(curEntIndex));
			
		}
		
		//the last streak being processed is left unended due to nature of loop
		curStreak.end();
		results.put(curID, curStreak);
		
		return results;
		
	}
	
	public static void main (String [] args) {
		
		
	}
	
}
