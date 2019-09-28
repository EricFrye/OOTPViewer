package UI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import data.FileLooker;
import data.Holder;
import data.Reports;
import data.StreakType;
import data_obj.Player;
import data_obj.Team;

public class StreakUI extends JPanel implements ActionListener{
	
	private JFrame parent;
	
	private JPanel streakTypeContainer;
	private JTextField streakInput;
	private JComboBox <String> streakType;
	
	private JPanel streakSelectablesContainer;
	private JComboBox <Team> teams;
	private JComboBox <Player> players;
	
	private JPanel streakSubmitContainer;
	private JButton submit;
	
	private JPanel streakOutputContainer;
	
	public StreakUI (JFrame parent, Dimension size) {
		
		super();
		
		this.parent = parent;
		setSize(size);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//initialize the containers
		streakTypeContainer = new JPanel ();
		streakTypeContainer.setVisible(true);
		streakSelectablesContainer = new JPanel ();
		streakSelectablesContainer.setVisible(true);
		streakSubmitContainer = new JPanel ();
		streakSubmitContainer.setVisible(true);
		
		//add them
		add(streakTypeContainer);
		add(streakSelectablesContainer);
		add(streakSubmitContainer);
		
		//initialize type and condition
		streakInput = new JTextField(20);
		streakInput.setVisible(true);
		streakType = new JComboBox<String>(new String [] {"Length","Amount"});
		streakType.setVisible(true);
		
		//add to type container
		streakTypeContainer.add(streakInput);
		streakTypeContainer.add(streakType);

		//add teams
		teams = new JComboBox <Team> ();
		teams.setVisible(true);
		streakSelectablesContainer.add(this.teams);
		
		submit = new JButton ("Submit");
		submit.addActionListener(this);
		
		streakOutputContainer = new JPanel ();
		streakOutputContainer.setVisible(true);
		add(streakOutputContainer);
		
		super.setVisible(true);
		this.parent.pack();
		
	}
	

	public void loadTeams (String condition) {
		
		if (this.teams != null) {
			this.streakSelectablesContainer.remove(this.teams);
		}
		
		Holder data = new Holder ("", "");

		try {
			data.loadInfo(FileLooker.getLatestFileVersion("data", "teams"), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Team [] comboBoxValues = new Team [data.numEntities()];
		
		for (int curEntIndex = 0; curEntIndex < data.numEntities(); curEntIndex++) {
			comboBoxValues[curEntIndex] = new Team(data.getEntityVal(curEntIndex), data.getMappings());
		}
		
		this.teams = new JComboBox <Team> (comboBoxValues);
		this.teams.setVisible(true);
		this.teams.addActionListener(this);
		
		this.streakSelectablesContainer.add(this.teams);
		this.repaint();
		this.revalidate();
		this.parent.pack();
		
	}

	public void loadPlayers (String condition) {
		
		if (this.players != null) {
			this.streakSelectablesContainer.remove(this.players);
		}
		
		Holder data = new Holder ("", "");

		try {
			data.loadInfo(FileLooker.getLatestFileVersion("data", "players"), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Player [] comboBoxValues = new Player [data.numEntities()];
		
		for (int curEntIndex = 0; curEntIndex < data.numEntities(); curEntIndex++) {
			comboBoxValues[curEntIndex] = new Player(data.getEntityVal(curEntIndex), data.getMappings());
		}
		
		this.players = new JComboBox <Player> (comboBoxValues);
		this.players.setVisible(true);
		this.players.addActionListener(this);
		
		this.streakSelectablesContainer.add(this.players);
		this.repaint();
		this.revalidate();
		this.parent.pack();
		
	}
	
	public static void main (String [] args) {
		
		Dimension dim = new Dimension (500, 300);
		MainFrame view = new MainFrame (dim);
		
		StreakUI ui = new StreakUI(view, dim);
		view.addComp("StreakUI",ui);
		ui.loadTeams("league_id = 100");
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (teams == e.getSource()) {
			Team selectedTeam = (Team)this.teams.getSelectedItem();
			loadPlayers(String.format("team_id=%s", selectedTeam.getID()));
		}
		
		else if (players == e.getSource()) {
			submit.setVisible(true);
			streakSubmitContainer.add(this.submit);
			repaint();
			revalidate();
			parent.pack();
		}
		
		else if (submit == e.getSource()) {
			
			String type = this.streakType.getSelectedItem().toString();
			
			if (type.equals("Length")) {
				
				String playerID = ((Player)this.players.getSelectedItem()).getID() + "";
				DataTable result = Reports.playerHittingStreak(playerID, type, streakInput.getText());
				
				JScrollPane toAdd = new JScrollPane (result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				toAdd.setVisible(true);
				streakSubmitContainer.add(toAdd);
				repaint();
				revalidate();
				parent.pack();
				
			}
			
		}
		
		
	}
	
}
