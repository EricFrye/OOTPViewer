package data_obj;

import java.awt.Dimension;

import UI.MainFrame;
import UI.StreakUI;

/**
 * These are for use in conjunction with JComboBoxes
 * @author Eric
 *
 */
public class DataObj {
	
	public static final String PLAYER_ID = "player_id";
	public static final String TEAM_ID = "team_id";
	public static final String LEAGUE_ID = "league_id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String NAME = "name";
	public static final String NICKNAME = "nickname";
	
	private int ID;
	private String displayName;
	
	public DataObj (int ID, String displayName) {
		this.ID = ID;
		this.displayName = displayName;
	}
	
	public int getID () {
		return this.ID;
	}
	
	public String getDisplayName () {
		return this.displayName;
	}
	
	public String toString () {
		return this.displayName;
	}

}
