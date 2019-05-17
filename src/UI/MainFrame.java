package UI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainFrame extends JFrame {
	
	public MainFrame (int width, int height) {
		
		super();
		this.setSize(width, height);
		this.setVisible(true);
		
	}
	
	public void addHolderTable (HolderTable table, Dimension size) {
		
		JPanel container = new JPanel ();
		container.setVisible(true);
		container.setPreferredSize(size);
		
		JScrollPane toAdd = new JScrollPane (table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		toAdd.setVisible(true);
		
		container.add(toAdd);
		
		this.add(container);
		
	}
	
}
