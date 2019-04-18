import java.util.*;

import Data.Holder;
import query.*;

public class driver {
	
	public static void main (String [] args) {
		
		//JFrame view = new JFrame ("Player View");
		//view.setVisible(true);
		
		String path = "data";
		
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
		
		QueryResult res = players.query("first_name=Ivan&&last_name=Zamora");
		String id = res.getTopField("player_id");
		
		String newQuery = String.format("player_id=%s%%split_id=%s", id, "1");
		QueryResult ivanZamoraBattingStats = playersBatting.query(newQuery);
		
	}
	
}
