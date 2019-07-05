package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import Data.Holder;
import query.Operations;
import query.QueryParser;

public class QueryUI {
	
	private JTextField search;
	private Map <String,Holder> currentTables;
	private String curDisplayedTableName;
	private Dimension tableSize;
	
	public QueryUI (MainFrame parent, int width, int height) {
		
		super();

		this.tableSize = new Dimension (width, height);
		this.curDisplayedTableName = null;
		this.currentTables = new HashMap <String, Holder> ();
		
		this.search = new JTextField(20);
		this.search.setVisible(true);

		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				QueryParser parser = new QueryParser();
				List <String []> ops;
				
				try {
					ops = parser.parseQuery(search.getText());
				}
				
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					return;
				}
					
				String name = Operations.performOps(ops, currentTables);
				Holder curTable = currentTables.get(name);
				
				//remove the old table if one was there
				if (curDisplayedTableName != null) {
					parent.removeComp(curDisplayedTableName);
				}
				
				HolderTable holderTab = HolderTable.generateHolderTable(curTable, "");
				curDisplayedTableName = name;
				parent.addHolderTable(name, holderTab, new Dimension(400,300));
				
			}
			
		};
		
		this.search.addActionListener(action);
		parent.addComp("queryInput", this.search);
		
	}

}
