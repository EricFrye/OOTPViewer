package query;

import java.util.Map;

import boolean_logic.Quantifier;
import data.Data;
import data.Holder;
import data.Type;

/**
 * QueryResult contains the results of a query and any data related to this said data
 * @author Eric
 *
 */
public class QueryResult extends Holder{
	
	private Quantifier query;

	/**
	 * 
	 * @param result The result from Holder.query
	 * @param query The list of queries as parsed by LogicalStatement.parse
	 * @param mappings The string to index mappings for the fields
	 * @param types Array containing the types of the fields
	 */
	public QueryResult (Data result, Quantifier query, Map <String, Integer> mappings, Type [] types) {
		
		super(mappings, result, types);
		this.query = query;
		
	}
	
	public QueryResult (String path, String file) {
		super(path,file);
	}
	
	/**
	 * Gets the first result found for this query
	 * @return null if nothing found, otherwise the first Entity.  This is a copy of the Entity, not a reference
	 */

	public String [] getTop () {
		return super.getTop();
	}
	
	/**
	 * Gets the specified field for the first Entity found by the Query
	 * @param field Name of field
	 * @return NaN with explaination on error, or the requested data
	 */
	public String getTopField (String field) {
		
		String [] top = getTop();
		
		return top == null ? "NaN (No Result)" : top[(this.mappings.get(field))];
		
	}	
	
	/**
	 * Get a single value from a table
	 * @param path
	 * @param file
	 * @param field
	 * @param loadCondition
	 * @return
	 */
	public static String getTopField (String path, String file, String field, String loadCondition) {
		
		Holder table = new QueryResult (path, file);
		
		try {
			table.loadInfo(null, loadCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ((QueryResult)table).getTopField(field);
		
	}

	
	public static void main (String [] args) {
		
		System.out.println(QueryResult.getTopField("data", "teams", "team_id", "name=Philadelphia"));
		
	}
	
}
