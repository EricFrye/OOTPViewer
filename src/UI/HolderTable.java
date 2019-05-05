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
		int numExtraRows = 1; //summarization row
		int numHeaderRows = 1;
		Object [][] data = new Object [numEntries+numExtraRows+numHeaderRows][colNames.length];
		
		data[0] = table.mappings();
		
		for (int dataIndex = 0; dataIndex < numEntries; dataIndex++) {
			data[dataIndex+numHeaderRows] = table.getEntityVal(dataIndex);
		}
		
		data[numEntries+numHeaderRows] = table.summarize(fieldsSummarize);
		
		return new HolderTable (data, colNames);
		
	}
	
}
