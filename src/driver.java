import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;

import javax.swing.*;

import Data.Holder;
import UI.HolderTable;
import UI.MainFrame;
import query.*;

public class driver {
	
	public static void main (String [] args) {
		
		//JFrame view = new JFrame ("Player View");
		//view.setVisible(true);
		
		String path = "C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv";
		final int frameWidth = 800;
		final int frameHeight = 600;
		
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
		String fileName3 = "leagues";
		
		Holder playersStats = new Holder(path, fileName);
		Holder players = new Holder(path, fileName1);
		Holder teams = new Holder(path, fileName2);
		Holder leagues = new Holder(path, fileName3);
		
		try {
			playersStats.loadInfo();
			players.loadInfo();
			teams.loadInfo();
			leagues.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		players = players.select("player_id,team_id,first_name,last_name");
		
		players = players.query("first_name=German&&last_name=Castro");
		playersStats = playersStats.query("split_id=1");
		
		playersStats = playersStats.join(players, "player_id");
		playersStats = playersStats.join(teams, "team_id");
		playersStats = playersStats.join(leagues, "league_id");
		
		Holder finalTable = playersStats.select("abbr1,nickname,first_name,last_name,hr,war");
		
		HolderTable table = HolderTable.generateHolderTable(playersStats, "hr,war");
		
		MainFrame view = new MainFrame (frameWidth, frameHeight);
		view.addHolderTable(table, new Dimension(400,300));
		
	}
	
}
