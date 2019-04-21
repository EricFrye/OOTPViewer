package query;

import java.util.*;
import Data.Entity;
import Data.Holder;

/**
 * QueryResult contains the results of a query and any data related to this said data
 * @author Eric
 *
 */
public class QueryResult extends Holder{
	
	private List <Query> query;

	/**
	 * 
	 * @param result The result from Holder.query
	 * @param query The list of queries as parsed by LogicalStatement.parse
	 */
	public QueryResult (List <Entity> result, List <Query> query, List <String> mappings) {
		
		super(mappings, result);
		this.query = query;
		
	}
	
	/**
	 * Gets the first result found for this query
	 * @return null if nothing found, otherwise the first Entity.  This is a copy of the Entity, not a reference
	 */
	public Entity getTop () {
		return new Entity(super.getTop());
	}
	
	/**
	 * Gets the specified field for the first Entity found by the Query
	 * @param field Name of field
	 * @return NaN with explaination on error, or the requested data
	 */
	public String getTopField (String field) {
		
		Entity top = super.getTop();
		return top == null ? "NaN (No Result)" : top.getData(field);
		
	}	
	
}
