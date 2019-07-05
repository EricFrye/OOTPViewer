package query;
import java.util.*;
import java.util.regex.*;

import misc.ArrayReader;

/***
 * A Statement is a single line of text that can be parsed into a set of logical queries
 * @author Eric
 *
 */
public class LogicalStatement {
	
	/**
	 * Parses a string into a list of queries
	 * @param line regex string of the form ((\w+)(=|>|>=|<|<=)(\w+)(&&|%%))*(\w+)(=|>|>=|<|<=)(\w+) where %% corresponds to logical OR
	 * @return List of queries corresponding to the regex line
	 */
	public static List <Query> parse (String line) {
		
		List <Query> ret = new LinkedList <Query> ();
		String pattern = "(\\w+)(=|>|>=|<|<=)(\\w+)(&&|%%)*";
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher(line);
		int index = 0;
		
		while (m.find(index)) {
		
			Query q1 = new Query (m.group(1), m.group(2), m.group(3));
			ret.add(q1);
			index = m.end();		
				
		}
		
		return ret;
		
	}
	
	/**
	 * Serves as the middleman for an operation to a string that can be understood by LogicalStatement.parse
	 * @param op
	 * @return
	 */
	public static String translateOpToLogicalStatement (String [] op) {
		
		String ret = "";
		ArrayReader reader = new ArrayReader (op);
		
		String WHERE = reader.read();
		assert WHERE.equals("WHERE") : " An operation must begin with WHERE to be translated to a logical statement.";
		
		while (reader.moreOpToRead()) {
			
			ret+=reader.read()+reader.read()+reader.read(); //read field,comparator,value
			
			//check if there is more to the op.  if this condition holds then the while loop -should- continue
			if (reader.moreOpToRead()) {
				String compounder = reader.read();
				
				if (compounder.equals("AND")) {
					ret+="&&";
				}
				
				else if (compounder.equals("OR")) {
					ret+="%%";
				}
				
				else {
					throw new IllegalArgumentException("Invalid keyword " + compounder + " read.");
				}
				
			}
			
		}
		
		return ret;
		
	}
	
	public static void main (String [] args) {
		
		List <Query> query = LogicalStatement.parse("player_id>=1234s&&team_id<=12312");
		System.out.println(query);
		
		
	}
	
}