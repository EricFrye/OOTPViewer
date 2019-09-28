package data_obj;

import java.util.Map;

public class Player extends DataObj {
	
	private int teamID;
	private int leagueID;
	
	public Player (String [] ent, Map <String, Integer> mappings) {
		
		super(Integer.parseInt(ent[mappings.get(DataObj.PLAYER_ID)]), ent[mappings.get(DataObj.FIRST_NAME)] + " " + ent[mappings.get(DataObj.LAST_NAME)]);
		this.teamID = Integer.parseInt(ent[mappings.get(DataObj.TEAM_ID)]);
		this.leagueID = Integer.parseInt(ent[mappings.get(DataObj.LEAGUE_ID)]);
		
	}
	
}