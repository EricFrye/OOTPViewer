import java.awt.Dimension;

import UI.DataTable;
import UI.MainFrame;
import data.Reports;

public class driver {
	
	public static void main (String [] args) {
		
		Dimension dim = new Dimension (500,800);
		
		MainFrame view = new MainFrame (dim);
		
		DataTable result = Reports.playerHittingStreak("Heriberto", "Sierra");
		view.addHolderTable("results", result, dim);
		
		//view.addComp("StreakUI", strk);
		
		//new QueryUI (view, 300, 400);
			
	}
	
	public static Object test (Object test) {
		return test;
	}
	
}
