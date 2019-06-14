package query;

import java.util.LinkedList;
import java.util.List;

public class QueryParser {
	
	private List <Operation> ops = new LinkedList <Operation> ();
	private static QueryReservedWords reservedWords = new QueryReservedWords ();
	private final char none = (char)0;

	
	/**
	 * Parse user input into a List of Operations to be performed on a Holder
	 */
	public void parseQuery (String str) {


		
	}
	
	//[ ]*[a-z|A-Z|0-9]+[ ]*
	private String ParseWord (StringWrapper str) {
		
		//clear the leading spaces
		while (str.peek() == ' ') {
			str.consume();
		}
		
		String token = "";
		
		while (isValidChar(str.peek())) {
			token += str.consume();
		}
		
		while (str.peek() == ' ') {
			str.consume();
		}
		
		return token;
		
	}
	
	private boolean isValidChar (char check) {
		return (check >= 'a' && check <= 'z') || (check >= 'A' && check <= 'Z') || (check >= '0' && check <= '9');
	}
	
	public static void main (String [] args) {
		
		String toParse = "  er ic  ";
		StringWrapper pntr = new StringWrapper (toParse);
		
		QueryParser parser = new QueryParser ();
		System.out.println(parser.ParseWord(pntr));
		
	}
	
}

