package data_obj;

import java.util.Map;

public class Team extends DataObj {
	public Team (String [] ent, Map <String, Integer> mappings) {
		super(Integer.parseInt(ent[mappings.get(DataObj.TEAM_ID)]), ent[mappings.get(DataObj.NAME)] + " " + ent[mappings.get(DataObj.NICKNAME)]);
	}
}
