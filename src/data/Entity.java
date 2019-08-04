package data;
import java.util.*;

import boolean_logic.Query;

/***
 * A single line from a data file.  An Entity cannot be interpreted outside of the Holder that it belongs to as it does not have the data mappings 
 * @author Eric
 *
 */
public class Entity {

	//the key is the string representation of the field name
	//the value is the string representation of the value for a field for this entity
	private String [] data;
	
	/***
	 * Create the entity object
	 * @param in A single line from a datafile
	 * @param mappings The mappings created during the Holder constructer
	 */
	public Entity (String in) {
		
		this.data = in.split(",");
		
		//clean data of ""
		for (int index = 0; index < data.length; index++) {
			data[index] = data[index].replaceAll("\"", "");
		}
		
	}
	
	/**
	 * Copy constructor
	 * @param copy Reference to copy
	 */
	public Entity (Entity copy) {
		this.data = copy.getData();
	}
	
	/**
	 * @param data Data for this Entity
	 */
	public Entity (String [] data) {
		this.data = data;
	}
	
	/**
	 * Selects a subset of the fields in this entity
	 * @param types Array of the types of the 
	 * @return
	 */
	public Entity select (String [] newFields, Map <String, Integer> mappings) {
		
		String [] newData = new String [newFields.length];
		
		for (int fieldIndex = 0; fieldIndex < newFields.length; fieldIndex++) {
			newData[fieldIndex] = this.data[mappings.get(newFields[fieldIndex])];
		}
		
		return new Entity (newData);
		
	}
	
	/***
	 * @param key Field name
	 * @return Value corresponding to field name for this entity
	 */
	public String getData (int index) {
		return index < data.length ? data[index] : "NaN (No Field)";
	}
	
	/**
	 * Check if this Entity should be in the query.  All queries are compounded with && (logical and)
	 * @param queries List of queries as parsed by LogicalStatement.parse
	 * @return True if this entity should be in the query, false if not
	 * @throws Exception If a field is queried that is not included in this entity
	 */
	public boolean query (Map <String, Integer> mappings, List <Query> queries) throws Exception {
		
		for (Query curQuery: queries) {

			String field = curQuery.getField();
			
			//the field included in the query is invalid
			if (!mappings.containsKey(field)) {
				throw new Exception ("The field " + field + " does not exist in this record");
			}
			
			else {
				
				String thisVal = data[mappings.get(field)];
				
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
	
	/**
	 * 
	 * @return The data in this Entity as a CSV line
	 */
	public String asCSV () {
		return String.join(",", data) + "\n";
	}

	/**
	 * 
	 * @return A copy of this Entity's data
	 */
	public String [] getData () {
		return data.clone();
	}
	
}
