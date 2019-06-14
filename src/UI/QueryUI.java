package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import Data.Holder;

public class QueryUI {
	
	private JTextField search;
	private HolderTable dispalyedData;
	private Map <String,Holder> currentTables;
	private Dimension tableSize;
	
	public QueryUI (MainFrame parent, int width, int height) {
		
		super();

		this.tableSize = new Dimension (width, height);
		this.dispalyedData = null;
		this.currentTables = new HashMap <String, Holder> ();
		
		this.search = new JTextField(20);
		this.search.setVisible(true);

		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Holder tab = new Holder ("data", search.getText());
				
				try {
					tab.loadInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				HolderTable holderTab = HolderTable.generateHolderTable(tab, "");
				parent.addHolderTable(holderTab, new Dimension(400,300));
				
			}
			
		};
		
		this.search.addActionListener(action);
		parent.addComp("queryInput", this.search);
		
	}

}
