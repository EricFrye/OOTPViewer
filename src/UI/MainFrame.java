package UI;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainFrame extends JFrame {
	
	private JPanel container;
	private Map <String, Component> items;
	
	public MainFrame (Dimension dim) {
		
		super();
		this.container = new JPanel ();
		this.container.setVisible(true);
		this.getContentPane().add(container);
		
		//this.setSize(dim);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.items = new HashMap <String, Component> ();
		
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		
	}
	
	/**
	 * Add a table to this frame with a given dimension
	 * @param table
	 * @param size
	 */
	public void addHolderTable (String name, DataTable table, Dimension size) {
		
		JScrollPane toAdd = new JScrollPane (table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		toAdd.setVisible(true);
		
		this.addComp(name, toAdd);
				
	}
	
	public Component addComp (String ID, Component add) {
		
		if (this.items.containsKey(ID)) {
			throw new IllegalArgumentException (ID + " is already in this MainFrame");
		}
		
		this.container.add(add);
		this.items.put(ID, add);
		
		repaint();
		revalidate();
		pack();
		
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
