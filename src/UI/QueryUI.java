package UI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import org.jsoup.Jsoup;

import data.Holder;
import misc.Utilities;
import query.Operations;
import query.QueryParser;

public class QueryUI {
	
	private JTextPane search;
	private Map <String,Holder> currentTables;
	private String curDisplayedTableName;
	private Dimension tableSize;
	private boolean underlineUsed;
	
	public QueryUI (MainFrame parent, int width, int height) {
		
		super();

		this.tableSize = new Dimension (width, height);
		this.curDisplayedTableName = null;
		this.currentTables = new HashMap <String, Holder> ();
		this.underlineUsed = false;
		
		this.search = new JTextPane();
		//this.search.setContentType("text/html");
		this.search.setVisible(true);
		this.search.setContentType("text/html");
		
		KeyListener action = new KeyListener() {

			private void performEnter() {
				
				String htmlText = search.getText();
				String searchText = Jsoup.parse(htmlText).text();
				
				QueryParser parser = new QueryParser();
				List <String []> ops;
				
				ops = parser.parseQuery(searchText);
				
				if (ops == null) {
					
					JOptionPane.showMessageDialog(null, parser.getError().getMessage());
					
					int [] underlineIndicies = Utilities.backwardsSpaceIndex(searchText, parser.getCharsRead()-1);
					
					String newSearchTextBegin = searchText.substring(0, underlineIndicies[0]);
					String underline = searchText.substring(underlineIndicies[0], underlineIndicies[1]); 
					String newSearchTextEnd = underlineIndicies[1] == searchText.length() ? "" : searchText.substring(underlineIndicies[1]);
					
					search.setText(String.format("%s<u>%s</u>%s", newSearchTextBegin, underline, newSearchTextEnd));
					underlineUsed = true;
					
				}
				
				else {
				
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
				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

				if (underlineUsed) {
					
					String htmlText = search.getText();
					String searchText = Jsoup.parse(htmlText).text();
					searchText = searchText.replaceAll("<u>|</u>", "");
					
					search.setText(searchText);
					
					underlineUsed = false;
				
				}
				
				if (arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					performEnter();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
		};
		
		this.search.addKeyListener(action);
		parent.addComp("queryInput", this.search);
		
	}

}
