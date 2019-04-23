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
	
	public static HolderTable generateHolderTable (Holder table) {
		
		String [] colNames = table.mappings();
		int num = table.numEntities();
		Object [][] data = new Object [num][colNames.length];
		
		for (int dataIndex = 0; dataIndex < num; dataIndex++) {
			data[dataIndex] = table.getEntityVal(dataIndex);
		}
		
		return new HolderTable (data, colNames);
		
	}
	
}
