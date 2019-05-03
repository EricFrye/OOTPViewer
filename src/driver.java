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
		
		QueryResult recordsQuery = records.query("team_id=1");
		Holder selected = recordsQuery.select("year,w,l,pos,gb,pct");
		
		HolderTable phillySabresRecord = HolderTable.generateHolderTable(selected, "w,l");
		JScrollPane s = new JScrollPane (phillySabresRecord);
		
		JFrame frame = new JFrame ();
		frame.setSize(recordsQuery.recommendedTableWidth(), 600);
		frame.setVisible(true);
		
		s.setVisible(true);
		frame.add(s);
		
	}
	
}
