import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import Data.Holder;
import UI.HolderTable;
import UI.MainFrame;
import UI.QueryUI;
import query.*;

public class driver {
	
	public static void main (String [] args) {
				
		//JFrame view = new JFrame ("Player View");
		//view.setVisible(true);
		
		String path = "C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv";
		
		MainFrame view = new MainFrame (500, 800);
		new QueryUI (view, 300, 400);
			
	}
	
}
