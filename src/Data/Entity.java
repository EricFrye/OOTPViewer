package Data;
import java.util.*;

import query.Query;

/***
 * A single line from a data file 
 * @author Eric
 *
 */
public class Entity {

	//the key is the string representation of the field name
	//the value is the string representation of the value for a field for this entity
	private Map <String, String> data;
	
	/***
	 * Create the entity object
	 * @param in A single line from a datafile
	 * @param mappings The mappings created during the Holder constructer
	 */
	public Entity (String in, List <String> mappings) {
		
		this.data = new HashMap <String, String> ();
		
		String [] vals = in.split(",");
		
		for (int index = 0; index < mappings.size(); index++) {
			String curStat = mappings.get(index);
			String toInsert = vals[index].replaceAll("\"","");
			data.put(curStat, toInsert);
		}
		
	}
	
	/**
	 * Copy constructor
	 * @param copy Reference to copy
	 */
	public Entity (Entity copy) {
		
		this.data = new HashMap <String, String> ();
		
		for (String curKey: copy.getFields()) {
			this.data.put(curKey, copy.getData(curKey));
		}
		
	}
	
	/***
	 * @param key Field name
	 * @return Value corresponding to field name for this entity
	 */
	public String getData (String key) {
		return data.containsKey(key) ? data.get(key) : "NaN (No Field)";
	}
	
	/**
	 * Check if this Entity should be in the query.  All queries are compounded with && (logical and)
	 * @param queries List of queries as parsed by LogicalStatement.parse
	 * @return True if this entity should be in the query, false if not
	 * @throws Exception If a field is queried that is not included in this entity
	 */
	public boolean query (List <Query> queries) throws Exception {
		
		for (Query curQuery: queries) {

			String field = curQuery.getField();
			
			//the field included in the query is invalid
			if (!data.containsKey(field)) {
				throw new Exception ("The field " + field + " does not exist in this record");
			}
			
			else {
				
				String thisVal = data.get(field);
				
				if (!curQuery.comp(thisVal)) {
					return false;
				}
				
			}
			
		}
		
		return true;
		
	}

	@Override
	public String toString() {
		return "Entity [data=" + data + "]";
	}
	
	public String asCSV (List <String> mappings) {
		
		String ret = "";
		
		for (String curField: mappings) {
			ret += data.get(curField) + ",";
		}
		
		return ret.substring(0, ret.length()-1) + "\n";
		
	}
	
	/**
	 * @param field The name of the field
	 * @return True if the field exists in this entity, falss otherwise
	 */
	public boolean containsField (String field) {
		return data.containsKey(field);
	}
	
	/**
	 * @return A set of the keys contained in this Entity
	 */
	public Set <String> getFields () {
		return data.keySet();
	}

	
}
