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
		
		String fileName = "players_career_batting_stats";
		String fileName1 = "players";
		String fileName2 = "teams";
		
		Holder playersStats = new Holder(path, fileName);
		Holder players = new Holder(path, fileName1);
		Holder teams = new Holder(path, fileName2);
		
		try {
			playersStats.loadInfo();
			players.loadInfo();
			teams.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String [] out = players.createNewOrderedFields(playersStats);
		
		for (String cur: out) {
			System.out.println(cur);
		}
		
		players = players.select("player_id,team_id,first_name,last_name");
		
		players = players.query("first_name=Bobby&&last_name=Betances");
		playersStats = playersStats.query("league_id=100&&split_id=1");
		
		playersStats = playersStats.join(players, "player_id");
		playersStats = playersStats.select("first_name,last_name,team_id,hr,war");
		playersStats = playersStats.join(teams, "team_id");
		
		Holder finalTable = playersStats.select("nickname,first_name,last_name,hr,war");
		
		HolderTable table = HolderTable.generateHolderTable(finalTable, "");
		
		JFrame frame = new JFrame ();
		
		frame.add(table);
		frame.setVisible(true);
		
	}
	
}
