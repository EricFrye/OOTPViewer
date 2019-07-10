package query;

import java.util.List;
import java.util.Map;

public class Queries {

	private List <Query> queries;
	
	public Queries (List <Query> queries) {
		this.queries = queries;
	}
	
	public boolean test (Map <String, Integer> mappings, String [] ent) {
		
		//try all queries
		for (Query curQuery: this.queries) {
			
			boolean result = false;
			
			try {
				 result = curQuery.evaluateQuery(mappings, ent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//if the condition is false, or there is an exception, then bail
			if (!result) {
				return false;
			}
			
		}
		
		return true;
		
	}
	
}
