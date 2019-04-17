package Data;
import java.util.*;

import query.LogicalStatement;
import query.Query;
import query.QueryResult;

import java.io.*;

/***
 * InfoHolder is the main mechanism for loading and accessing the data files
 * @author Eric
 *
 */
public class Holder {
	
	private List <String> mappings; //maps the col names to their index. the index in the list is the index in the report
	private List <Entity> info; //holds the actual data
	private Scanner fd; //input for file
	
	/***
	 * Creates the InfoHolder object
	 * @param filePath the path to the target data file
	 */
	public Holder (String filePath) {
		
		mappings = new ArrayList <String> ();
		info = new ArrayList <Entity> ();
		
		try {
			fd = new Scanner (new File (filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/***
	 * Standard loading function.  All columns are kept
	 */
	public void loadInfo () throws Exception {
		
		String [] headers = fd.nextLine().split(",");
		
		for (String curHeader: headers) {
			mappings.add(curHeader);
		}
		
		while (fd.hasNextLine()) {
			
			String line = fd.nextLine();
			
			Entity toAdd = new Entity (line, mappings);
			info.add(toAdd);
					
		}

	}
	
	/**
	 * Performs a query
	 * @param query The string representation of the query
	 * @return A query result object
	 */
	public QueryResult query (String query) {
		
		List <Query> queries = LogicalStatement.parse(query);
		List <Entity> ret = new LinkedList <Entity> ();
		
		for (Entity curEnt: info) {
			
			boolean success;
			
			try {
				success = curEnt.query(queries);
			}
			catch (Exception e) {
				success = false;
			}
			
			if (success) {
				ret.add(curEnt);
			}
			
		}
		
		return new QueryResult(ret, queries);
		
	}
	
	public String buildReport () {
		
		String ret = "";
		
		for (Entity curEnt: info) {
			
			
		}
		
		return ret;
		
	}
	
	/**
	 * @return The count of entries in this Holder
	 */
	public int numEntities () {
		return mappings.size();
	}
	
}
