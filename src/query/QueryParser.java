package query;

import java.util.LinkedList;
import java.util.List;

import misc.Utilities;

public class QueryParser {
	
	private static QueryReservedWords reservedWords = new QueryReservedWords ();
	private final char none = (char)0;
	private List <String []> ops;
	
	public QueryParser () {
		this.ops = new LinkedList <String []> ();
	}
	
	/**
	 * Parse user input into a List of of String arrays that can be used to fulfill the query string
	 */
	public List <String []> parseQuery (String str) {
		parseQuery(new StringWrapper(str));
		
		for (String [] curParsedPhrase: ops) {
			
			for (String curStr: curParsedPhrase) {
				System.out.print(curStr + " ");
			}
			
			System.out.println();
			
		}
		
		return this.ops;
	}
	
	private String parseComparator (StringWrapper str) {
		
		String ret = "";
		
		parseWhiteSpace(str);
		
		while (isValidComparer(str.peek())) {
			ret += str.consume();
		}
		
		parseWhiteSpace(str);
		return ret;
		
	}
	
	private void parseWhiteSpace (StringWrapper str) {
		
		while (str.peek() == ' ') {
			str.consume();
		}
		
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
	private void parseTableSelection (StringWrapper str) {
		
		String FROM = parseWord(str); //read the FROM
		
		if (!FROM.equals("FROM")) {
			throw new IllegalArgumentException("Incorrect parse - FROM expected");
		}
		
		String tableName = parseWord(str); //read the table name
		
		String [] selection = new String [] {FROM, tableName};
		
		while (nextWord(str).equals("JOIN")) {
			selection = Utilities.appendArray(selection, parseJoinStatement(str));
		}
		
		this.ops.add(selection);
		
	}
	
	//join := [JOIN][word][ON][word.word][=][word.word]
	private String [] parseJoinStatement (StringWrapper str) {
		
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
		String leftTableField = parseWord(str);
		
		//read the equal sign
		if (str.consume() != '=') {
			throw new IllegalArgumentException("Incorrect parse - = expected");
		}
		
		//reading the right half of the join condition
		String rightTableField = parseWord(str);
		
		return new String [] {JOIN, rightTable, ON, leftTableField, rightTableField};
		
	}
	
	//quantifier := [WHERE][conditional]([AND|OR][conditional])*
	private void parseQuantifier (StringWrapper str) {
		
		String WHERE = parseWord(str); //read where
		
		if (!WHERE.equals("WHERE")) {
			throw new IllegalArgumentException("Incorrect parse - WHERE expected");
		}
				
		String [] quantifier = new String [] {WHERE};
		quantifier = Utilities.appendArray(quantifier, parseConditional(str)); //parse the conditional
		
		String nextWord = nextWord(str);
		
		//parse any conditionals that follow
		while (nextWord.equals("AND") || nextWord.equals("OR")) {
			
			String curLogicalConjunction = parseWord(str);			
			
			quantifier = Utilities.appendString(curLogicalConjunction, quantifier);
			quantifier = Utilities.appendArray(quantifier, parseConditional(str));
			
			nextWord = nextWord(str); //need to see if another conditional is coming
			
		}
		
		this.ops.add(quantifier);
		
	}
	
	//conditional := [word][=|<=|>=|<|>|<>][word]
	private String [] parseConditional (StringWrapper str) {
		
		String field = parseWord(str);
		String comparator = parseComparator(str);
		String value = parseWord(str);
		
		return new String [] {field, comparator, value};
		
	}
	
	//query := [table_selection][SELECT][list_of_words][quantifier]
	private void parseQuery (StringWrapper str) {
		
		parseTableSelection(str);
		
		String SELECT = parseWord(str);
		
		if (!SELECT.equals("SELECT")) {
			throw new IllegalArgumentException ("Incorrect parse - SELECT expected");
		}

		String fields = parseListOfWords(str);

		this.ops.add(new String [] {SELECT, fields});
		
		if (str.peek() != none) {
			parseQuantifier(str);
		}
			
	}
	
	//read the next word in this StringWrapper.  the parameter is unaltered
	private String nextWord (StringWrapper str) {
		
		StringWrapper tempStr = new StringWrapper (str);
		return parseWord(tempStr);
		
	}
	
	private boolean isValidComparer (char check) {
		return check == '<' || check == '>' || check == '=';
	}
	
	private boolean isValidChar (char check) {
		return (check >= 'a' && check <= 'z') || (check >= 'A' && check <= 'Z') || (check >= '0' && check <= '9') || check == '_';
	}
	
	public static void main (String [] args) {
		
		String toParse = "FROM teams JOIN team_history_record ON team_id = team_id SELECT ALL";
		StringWrapper pntr = new StringWrapper (toParse);

		QueryParser parser = new QueryParser ();
		List <String []> parsedQuery = parser.parseQuery(toParse);
		
		for (String [] curParsedPhrase: parsedQuery) {
			
			for (String curStr: curParsedPhrase) {
				System.out.print(curStr + " ");
			}
			
			System.out.println();
			
		}
		
	}
	
}

