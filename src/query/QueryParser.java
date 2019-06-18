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
	
	//word := [ ]*[a-z|A-Z|0-9]+[ ]*
	private String parseWord (StringWrapper str) {
		
		//clear the leading spaces
		while (str.peek() == ' ') {
			str.consume();
		}
		
		String word = "";
		
		while (isValidChar(str.peek())) {
			word += str.consume();
		}
		
		while (str.peek() == ' ') {
			str.consume();
		}
		
		return word;
		
	}
	
	//list_of_words := [word][,word]*
	private String parseListOfWords (StringWrapper str) {
		
		String words = parseWord(str);
				
		while (str.peek() == ',') {
			
			words += str.consume(); // read the comma
			words += parseWord(str); // read the following word
			
		}
		
		return words;
		
	}
	
	//table_selection := [FROM][word][join]*
	private String parseTableSelection (StringWrapper str) {
		
		String ret = "";
		
		String FROM = parseWord(str); //read the FROM
		
		if (!FROM.equals("FROM")) {
			throw new IllegalArgumentException("Incorrect parse - FROM expected");
		}
		
		ret += FROM;
		
		String tableName = parseWord(str); //read the table name
		ret += ' ' + tableName;
		
		return ret;
		
	}
	
	//join := [JOIN][word][ON][word.word][=][word.word]
	private void parseJoinStatement (StringWrapper str) {
		
		String ret = "";
		
		String JOIN = parseWord(str); //read the JOIN
		
		if (!JOIN.equals("JOIN")) {
			throw new IllegalArgumentException("Incorrect parse - JOIN expected");
		}
		
		String rightTable = parseWord(str); //read the name of the first table in the join statement
		
		String ON = parseWord(str); //read the ON
		
		if (!ON.equals("ON")) {
			throw new IllegalArgumentException("Incorrect parse - ON expected");
		}
		
		//reading the left half of the join condition
		String leftTableRef = parseWord(str);
		
		if (str.consume() != '.') {
			throw new IllegalArgumentException("Incorrect parse - . expected after table reference");
		}
		
		String leftTableField = parseWord(str);
		
		//read the equal sign
		if (str.consume() != '=') {
			throw new IllegalArgumentException("Incorrect parse - = expected");
		}
		
		//reading the right half of the join condition
		String rightTableRef = parseWord(str);
				
		if (str.consume() != '.') {
			throw new IllegalArgumentException("Incorrect parse - . expected after table reference");
		}
				
		String rightTableField = parseWord(str);
		
		System.out.println(JOIN);
		System.out.println(rightTable);
		System.out.println(ON);
		System.out.println(leftTableRef);
		System.out.println(leftTableField);
		System.out.println(rightTableRef);
		System.out.println(rightTableField);
		
		
	}
	
	private boolean isValidChar (char check) {
		return (check >= 'a' && check <= 'z') || (check >= 'A' && check <= 'Z') || (check >= '0' && check <= '9') || check == '_';
	}
	
	public static void main (String [] args) {
		
		String toParse = "  JOIN   players  ON   cities .  name  =   players .playerid";
		StringWrapper pntr = new StringWrapper (toParse);

		QueryParser parser = new QueryParser ();
		parser.parseJoinStatement(pntr);
		
	}
	
}

