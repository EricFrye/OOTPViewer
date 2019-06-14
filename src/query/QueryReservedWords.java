package query;

import java.util.*;

public class QueryReservedWords {
	
	private List <String> resWords;
	
	public QueryReservedWords () {
		
		this.resWords = new ArrayList <String> ();
		
		//any keywords should be defined here
		this.resWords.add("SELECT"); //specifies which fields to include
		this.resWords.add("FROM"); //specifies which tables/files to pull data from
		this.resWords.add("ALL"); //include all fields
		this.resWords.add("WHERE"); //sets the conditional for which entities must evaluate TRUE to be included
		
	}
	
	/**
	 * Check if a string is a reserved word
	 */
	public boolean isKeyWord (String check) {
		return this.resWords.contains(check);
	}
	
}
