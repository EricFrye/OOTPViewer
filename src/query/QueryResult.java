package query;

import java.util.*;

import Data.Data;
import Data.Entity;
import Data.Holder;
import Data.Type;

/**
 * QueryResult contains the results of a query and any data related to this said data
 * @author Eric
 *
 */
public class QueryResult extends Holder{
	
	private Queries query;

	/**
	 * 
	 * @param result The result from Holder.query
	 * @param query The list of queries as parsed by LogicalStatement.parse
	 * @param mappings The string to index mappings for the fields
	 * @param types Array containing the types of the fields
	 */
	public QueryResult (Data result, Queries query, Map <String, Integer> mappings, Type [] types) {
		
		super(mappings, result, types);
		this.query = query;
		
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
	
}
