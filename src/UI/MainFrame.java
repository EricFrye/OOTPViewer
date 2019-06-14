package UI;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainFrame extends JFrame {
	
	private JPanel container;
	private Map <String, Component> items;
	
	public MainFrame (int width, int height) {
		
		super();
		this.container = new JPanel ();
		this.container.setVisible(true);
		this.getContentPane().add(container);
		
		this.setSize(new Dimension (width, height));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.items = new HashMap <String, Component> ();
		
	}
	
	/**
	 * Add a table to this frame with a given dimension
	 * @param table
	 * @param size
	 */
	public void addHolderTable (HolderTable table, Dimension size) {
		
		JScrollPane toAdd = new JScrollPane (table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		toAdd.setVisible(true);
		
		this.addComp("DisplayedTable", toAdd);
				
	}
	
	public Component addComp (String ID, Component add) {
		
		if (this.items.containsKey(ID)) {
			throw new IllegalArgumentException (ID + " is already in this MainFrame");
		}
		
		this.container.add(add);
		this.items.put(ID, add);
		
		repaint();
		revalidate();
		
		return add;
		
	}
	
	public Component removeComp (String ID) {
		
		if (!this.items.containsKey(ID)) {
			throw new IllegalArgumentException (ID + " is not in this MainFrame");
		}
		
		Component remove = items.get(ID);
		this.container.remove(remove);
		this.items.remove(ID);
		
		repaint();
		revalidate();
		
		return remove;
		
	}
	
	/**
	 * Initializes 
	 */
	public void initializeQueryInterface () {
		
		
		
	}
	
}
