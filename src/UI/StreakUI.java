package UI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

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
import data.Streak;
import data.StreakType;
import data_obj.Player;
import data_obj.Team;

public class StreakUI extends JPanel implements ActionListener{
	
	private static final String LENGTH = "Length";
	private static final String AMOUNT = "Amount";
	private static final String NONE = "";
	
	private JFrame parent;
	
	private JPanel streakTypeContainer;
	private JComboBox <String> streakType;
	
	private JPanel streakLengthContainer;
	private JTextField streakLengthCondition;
	
	private JPanel streakSpanContainer;
	private JComboBox <String> streakSpanField;
	private JTextField streakSpanLength;
	
	private JPanel streakSelectablesContainer;
	private JComboBox <Team> teams;
	private JComboBox <Player> players;
	
	private JPanel streakSubmitContainer;
	private JButton submit;
	
	private JScrollPane streakOutputTable;
	private JPanel streakOutputContainer;
	
	private Map <Integer, Streak> loadedStreaks;
	
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
		
		//inititalize the sub panel for span
		streakSpanContainer = new JPanel ();
		streakSpanField = new JComboBox<String>(FileLooker.loadFieldNames(String.format("players_game_%s", "batting"), "data", 2));
		streakSpanField.setVisible(true);
		streakSpanLength = new JTextField (4);
		streakSpanLength.setVisible(true);
		streakSpanContainer.add(streakSpanField);
		streakSpanContainer.add(streakSpanLength);
		streakSpanContainer.setVisible(false);
		
		//initialize the sub panel for length
		streakLengthContainer = new JPanel ();
		streakLengthCondition = new JTextField (20);
		streakLengthCondition.setVisible(true);
		streakLengthContainer.add(streakLengthCondition);
		streakLengthContainer.setVisible(false);
		
		streakType = new JComboBox<String>(new String [] {NONE,LENGTH,AMOUNT});
		streakType.setVisible(true);
		streakType.addActionListener(this);
		
		//add to type container
		streakTypeContainer.add(streakType);
		streakTypeContainer.add(streakSpanContainer);
		streakTypeContainer.add(streakLengthContainer);
		
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

	public void actionPerformed(ActionEvent e) {
		
		//load the players for the team
		if (teams == e.getSource()) {
			
			Team selectedTeam = (Team)this.teams.getSelectedItem();
			
			String type = this.streakType.getSelectedItem().toString();
			
			if (type.equals(LENGTH)) {
				this.loadedStreaks = Reports.playerStreakByTeam("2029", selectedTeam.getID()+"", Reports.playersHittingStreakFields, new String [] {"game_id"}, type, streakLengthCondition.getText());
			}
			
			else if (type.equals(AMOUNT)) {
				this.loadedStreaks = Reports.playerStreakByTeam("2029", selectedTeam.getID()+"", Reports.playersHittingStreakFields, new String [] {"game_id"}, type, streakSpanField.getSelectedItem().toString(), Integer.parseInt(streakSpanLength.getText()));
			}
						
			//delete an old table
			if (streakOutputTable != null) {
				streakSubmitContainer.remove(streakOutputTable);
			}
			
			loadPlayers("team_id="+selectedTeam.getID());
		
		}
		
		//show the submit button
		else if (players == e.getSource()) {
			submit.setVisible(true);
			streakSubmitContainer.add(this.submit);
			repaint();
			revalidate();
			parent.pack();
		}
		
		//when submit is pressed we want to process the query
		else if (submit == e.getSource()) {
			
			//delete an old table
			if (streakOutputTable != null) {
				streakSubmitContainer.remove(streakOutputTable);
			}
			

			
				DataTable result = null;
				String playerID = ((Player)this.players.getSelectedItem()).getID() + "";
				
				System.out.println(playerID);
				
				result = this.loadedStreaks.get(Integer.parseInt(playerID)).getStreakResultsUI();

				//add output table
				if (result != null) {
					streakOutputTable = new JScrollPane (result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					streakOutputTable.setVisible(true);
					streakSubmitContainer.add(streakOutputTable);
					repaint();
					revalidate();
					parent.pack();
				}
				
			}
			
		
		
		//show or hide the span field
		else if (streakType == e.getSource()) {
			
			streakSpanContainer.setVisible(false);
			streakLengthContainer.setVisible(false);
			
			String streakTypeValue = streakType.getSelectedItem().toString();
			
			if (streakTypeValue.equals(AMOUNT)) {
				streakSpanContainer.setVisible(true);
			}
			
			else if (streakTypeValue.equals(LENGTH)) {
				streakLengthContainer.setVisible(true);
			}
			
			repaint();
			revalidate();
			parent.pack();
			
		}
		
	}

	public static void main (String [] args) {
		
		Dimension dim = new Dimension (500, 300);
		MainFrame view = new MainFrame (dim);
		
		StreakUI ui = new StreakUI(view, dim);
		view.addComp("StreakUI",ui);
		ui.loadTeams("league_id = 100");
		
	}

	
}
