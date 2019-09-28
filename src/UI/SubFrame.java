package UI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SubFrame extends JPanel {

	private JFrame parent;
	
	public SubFrame (JFrame parent, Dimension dim) {
		
		super();
		setSize(dim);
		setVisible(true);
		
	}
	
	public void resize () {
		this.repaint();
		this.revalidate();
		this.parent.pack();
	}
	
}
