import java.awt.Toolkit;
import java.util.*;

import javax.swing.*;

import Data.Holder;
import UI.HolderTable;
import query.*;

public class driver {
	
	public static void main (String [] args) {
		
		//JFrame view = new JFrame ("Player View");
		//view.setVisible(true);
		
		String path = "C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv";
		
		/*
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
		
		String newQuery = String.format("player_id=%s&&split_id=1&&league_id=100", id);
		QueryResult playerBattingStats = playersBatting.query(newQuery);	
		
		HolderTable phillySabresRecord = HolderTable.generateHolderTable(playerBattingStats,"h,d,t,hr,r,rbi,k,g,gs,sb,cs,bb,ibb,ab,war*");
		*/
		
		String fileName = "team_history_record";
		
		Holder records = new Holder(path, fileName);
		try {
			records.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		fileName = "teams";
		Holder teams = new Holder(path, fileName);
		
		try {
			teams.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		teams = teams.query("nickname=Sabres&&name=Philadelphia");
		teams = teams.select("team_id,name,abbr,nickname");
		records = records.select("year,team_id,w,l,pct");
		Holder recordsTeams = teams.join(records, "team_id");
		
		HolderTable s = HolderTable.generateHolderTable(recordsTeams, "w,l");
		
		JFrame frame = new JFrame ();
		frame.setSize(recordsTeams.recommendedTableWidth(), 600);
		frame.setVisible(true);
		
		s.setVisible(true);
		frame.add(s);
		
	}
	
}
