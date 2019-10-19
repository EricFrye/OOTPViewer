import java.awt.Dimension;
import java.util.Map;

import UI.DataTable;
import UI.MainFrame;
import data.Reports;
import data.Streak;

public class driver {
	
	public static void main (String [] args) {
		
		Dimension dim = new Dimension (500,800);
		
		MainFrame view = new MainFrame (dim);
		
		Map <Integer, Streak> results = Reports.playerStreakByTeam("2029", "26", Reports.playersHittingStreakFields, new String [] {"game_id"}, "Length", "h > 0"); 
		Streak res = results.get(15675);
		
		//DataTable result = Reports.playerHittingStreak("Heriberto", "Sierra");
		view.addHolderTable("results", res.getStreakResultsUI(), dim);
		
		//view.addComp("StreakUI", strk);
		
		//new QueryUI (view, 300, 400);
			
	}
	
	public static Object test (Object test) {
		return test;
	}
	
}
