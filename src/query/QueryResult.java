package query;

import java.util.*;
import Data.Entity;

/**
 * QueryResult contains the results of a query and any data related to this said data
 * @author Eric
 *
 */
public class QueryResult {
	
	private List <Entity> result;
	private List <Query> query;
	
	/**
	 * 
	 * @param result The result from Holder.query
	 * @param query The list of queries as parsed by LogicalStatement.parse
	 */
	public QueryResult (List <Entity> result, List <Query> query) {
		
		this.result = new ArrayList <Entity> ();
		
		for (Entity toCopy: result) {
			this.result.add(new Entity(toCopy));
		}
		
		this.query = query;
		
	}
	
	/**
	 * Gets the first result found for this query
	 * @return null if nothing found, otherwise the first Entity
	 */
	public Entity getTop () {
		return result.size() == 0 ? null : result.get(0);
	}
	
	/**
	 * Gets the specified field for the first Entity found by the Query
	 * @param field Name of field
	 * @return NaN with explaination on error, or the requested data
	 */
	public String getTopField (String field) {
		
		Entity top = getTop();
		return top == null ? "NaN (No Result)" : top.getData(field);
		
	}
}
