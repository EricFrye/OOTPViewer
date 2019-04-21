import java.util.*;

import Data.Holder;
import query.*;

public class driver {
	
	public static void main (String [] args) {
		
		//JFrame view = new JFrame ("Player View");
		//view.setVisible(true);
		
		String path = "C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv";
		
		String playersPath = "players";
		Holder players = new Holder (path, playersPath);
		
		try {
			players.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String playersBattingPath = "players_career_batting_stats";
		Holder playersBatting = new Holder (path ,playersBattingPath);
		
		try {
			playersBatting.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		QueryResult res = players.query("first_name=German&&last_name=Castro");
		String id = res.getTopField("player_id");
		
		String newQuery = String.format("player_id=%s%%split_id=%s&&league_id=%s", id, "1", "100");
		QueryResult playerBattingStats = playersBatting.query(newQuery);
		
		String newQuery1 = String.format("player_id=%s%%split_id=%s", id, "1");
		QueryResult playerBattingStats1 = playersBatting.query(newQuery1);
		
		
		System.out.println(playerBattingStats.asCSV());
		System.out.println(playerBattingStats1.asCSV());
		
	}
	
}
