package query;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Data.Holder;
import misc.ArrayReader;

public class Operations {
	
	private static Holder from (String name, Map <String, Holder> currentTables) {
		
		//we are working with a table 
		if (currentTables.containsKey(name)) {
			return currentTables.get(name);
		}
		
		else {

			Holder newHolder = new Holder ("data", name);
			
			try {
				newHolder.loadInfo();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			return newHolder;
	
		}
		
	}
	
	private static Holder select (Holder holder, String fields) {
		return holder.select(fields);
	}
	
	private static Holder where (Holder holder, String queryStr) {
		return holder.query(queryStr);
	}
	
	private static Holder join (Holder base, Holder joiner, String fieldOn, Map <String, Holder> currentTables) {
		
		return base.join(joiner, fieldOn);
		
	}
	
	/**
	 * 
	 * @param ops A list of operations
	 * @param currentTables The tables that exist in the current scope
	 * @return An array of length 2 holding [tableName,table]
	 */
	public static String performOps (List <String []> ops, Map <String, Holder> currentTables) {
		
		Holder holderToRet = null;
		String name = null;
		
		for (String [] curOp: ops) {
			
			ArrayReader reader = new ArrayReader(curOp);
			
			String opType = reader.read();//the first part of an op should indicate to use what we should do
			
			//FROM
			if (QueryReservedWords.isFrom(opType)) {

				//try to load the table info
				name = reader.read();
				holderToRet = from(name, currentTables);
				
				//read any joins that might be there
				while (reader.moreOpToRead()) {
					
					reader.read(); //read JOIN
					
					Holder joinerHolder = null;
					
					//load the joiner table
					String joinerTable = reader.read();
					joinerHolder = from(joinerTable, currentTables);
				
					reader.read(); //read ON
					String baseField = reader.read();;
					String joinerField = reader.read();;
					holderToRet = join(holderToRet, joinerHolder, baseField, currentTables);
					
				}
				
			}
			
			else if (QueryReservedWords.isWhere(opType)) {
				
				String translation = LogicalStatement.translateOpToLogicalStatement(curOp);
				holderToRet = where(holderToRet, translation);
				
			}
			
			else if (QueryReservedWords.isSelect(opType)) {
				
				String fields = reader.read();
				
				if (!QueryReservedWords.isAll(fields)) {
					holderToRet = select(holderToRet, fields);
				}
					
			}
			
			//unknown leading token, just keep swimming
			else {
				continue;
			}
			
		}
		
		currentTables.put(name, holderToRet);
		return name;
		
	}
	
	public static void main (String [] args) {
		
		List <String []> ops = new LinkedList <String []> ();
		ops.add(new String [] {"FROM", "teams", "JOIN", "team_history_record", "ON", "team_id", "team_id"});
		
		//Object [] res = performOps(ops, new HashMap <String, Holder> ());
		
	}
	
}

