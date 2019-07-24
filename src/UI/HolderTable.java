package UI;

import javax.swing.*;

import Data.*;

/**
 * UI component that loads the info contained in a Holder into a JTable display
 * @author Eric
 *
 */
public class HolderTable extends JTable {
	
	private HolderTable (Object [][] data, String [] colNames) {
		super(data, colNames);
	}
	
	/**
	 * Creates a stats table that displays ingame statistics
	 * @param table The holder to draw the data from
	 * @param fieldsSummarize str Of the form BEGIN_REGEX([\w+][\\*])([,\w+][\\*])*END_REGEX where a * literal means this field should be interpreted as a double.  The fields under which to be summarized and added at the end. 
	 * @return A HolderTable object containing the corresponding entries and a summary row at the end
	 */
	public static HolderTable generateHolderTable (Holder table, String fieldsSummarize) {
		
		String [] colNames = table.mappings();
		int numEntries = table.numEntities(); 
		//int numExtraRows = 1; //summarization row
		//int numHeaderRows = 1;
		Object [][] data = new Object [numEntries][colNames.length];
		
		for (int dataIndex = 0; dataIndex < numEntries; dataIndex++) {
			data[dataIndex] = table.getEntityVal(dataIndex);
		}
		
		//data[numEntries] = table.summarize(fieldsSummarize);
		
		HolderTable ret = new HolderTable (data, colNames);
		ret.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		/*
		for (int curColNameIndex = 0; curColNameIndex < colNames.length; curColNameIndex++) {
			int width = table.recommendedColumnWidth(colNames[curColNameIndex]);
			System.out.println(width);
			ret.getColumnModel().getColumn(curColNameIndex).setPreferredWidth(width);
		}
		*/
		
		return ret;
		
	}
	
	
}
