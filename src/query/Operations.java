package query;

import java.util.LinkedList;
import java.util.List;

import Data.Holder;
import misc.ArrayReader;

public class Operations {
	
	private static Holder from (String file) {
		
		Holder newHolder = new Holder ("data", file);
		
		try {
			newHolder.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return newHolder;
		
	}
	
	private static void select (Holder holder, String fields) {
		holder.select(fields);
	}
	
	private static void where (Holder holder, String queryStr) {
		holder.query(queryStr);
	}
	
	private static Holder join (Holder base, Holder joiner, String fieldOn) {
		
		Holder joined = base.join(joiner, fieldOn);
		return joined;
		
	}
	
	public static Holder performOps (List <String []> ops) {
		
		Holder holderToRet = null;
		
		for (String [] curOp: ops) {
			
			ArrayReader reader = new ArrayReader(curOp);
			
			String opType = reader.read();//the first part of an op should indicate to use what we should do
			
			//FROM
			if (QueryReservedWords.isFrom(opType)) {

				//try to load the table info
				String tableName = reader.read();
				holderToRet = from(tableName);
				
				//read any joins that might be there
				while (reader.moreOpToRead()) {
					
					reader.read(); //read JOIN
					
					Holder joinerHolder = null;
					
					//load the joiner table
					String joinerTable = reader.read();
					joinerHolder = from(joinerTable);
				
					reader.read(); //read ON
					String baseField = reader.read();;
					String joinerField = reader.read();;
					holderToRet = join(holderToRet, joinerHolder, baseField);
					
				}
				
			}
			
			//unknown leading token, jsut return what we have
			else {
				return holderToRet;
			}
			
		}
		
		return holderToRet;
		
	}
	
	public static void main (String [] args) {
		
		List <String []> ops = new LinkedList <String []> ();
		ops.add(new String [] {"FROM", "teams", "JOIN", "team_history_record", "ON", "team_id", "team_id"});
		
		Holder res = performOps(ops);
		
	}
	
}

