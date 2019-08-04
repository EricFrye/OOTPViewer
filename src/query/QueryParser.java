package query;

import java.util.LinkedList;
import java.util.List;

import misc.StringWrapper;
import misc.Utilities;

public class QueryParser {
	
	private static final char none = (char)0;
	private int charsRead;
	private List <String []> ops;
	private ParseException error;
	private String lastQueryString;
	
	public QueryParser () {
		reset();
	}
	
	private void reset () {
		this.charsRead = 0;
		this.ops = new LinkedList <String []> ();
		this.error = null;
		this.lastQueryString = null;
	}
	
	public ParseException getError () {
		return this.error;
	}
	
	/**
	 * Parse user input into a List of of String arrays that can be used to fulfill the query string
	 */
	public List <String []> parseQuery (String str) {
		
		reset();
		
		try {
			this.lastQueryString = str;
			parseQuery(new StringWrapper(str));
		}
		catch (ParseException e) {
			this.error = e;
			return null;
		}
		
		for (String [] curParsedPhrase: ops) {
			
			for (String curStr: curParsedPhrase) {
				System.out.print(curStr + " ");
			}
			
			System.out.println();
			
		}
		
		return this.ops;
	}
	
	private String parseComparator (StringWrapper str, boolean nocount) throws ParseException {
		
		String ret = "";
		
		parseWhiteSpace(str, nocount);
		
		while (isValidComparer(str.peek())) {
			ret += str.consume();
			this.charsRead += nocount ? 0 : 1;
		}
		
		if (ret.equals("")) {
			
		}
		
		parseWhiteSpace(str, nocount);
		
		return ret;
		
	}
	
	private void parseWhiteSpace (StringWrapper str, boolean nocount) throws ParseException {
		
		while (str.peek() == ' ') {
			str.consume();
			this.charsRead += nocount ? 0 : 1;
		}
		
	}
	
	//word := [ ]*[a-z|A-Z|0-9]+[ ]*
	private String parseWord (StringWrapper str, boolean nocount) throws ParseException {
		
		//clear the leading spaces
		parseWhiteSpace(str, nocount);
		
		String word = "";
		
		while (isValidChar(str.peek())) {
			word += str.consume();
			this.charsRead += nocount ? 0 : 1;
		}
		
		parseWhiteSpace(str, nocount);
		
		return word;
		
	}
	
	//list_of_words := [word][,word]*
	private String parseListOfWords (StringWrapper str) throws ParseException {
		
		String words = parseWord(str, false);
				
		while (str.peek() == ',') {
			
			words += str.consume(); // read the comma
			this.charsRead++;
			words += parseWord(str, false); // read the following word
			
		}
		
		return words;
		
	}
	
	//table_selection := [FROM][word][join]*
	private void parseTableSelection (StringWrapper str) throws ParseException {
		
		String FROM = parseWord(str, false); //read the FROM
		checkParseError(FROM, "FROM");
		
		String tableName = parseWord(str, false); //read the table name
		checkParseError(tableName, null);
		
		String [] selection = new String [] {FROM, tableName};
		
		while (nextWord(str).equals("JOIN")) {
			selection = Utilities.appendArray(selection, parseJoinStatement(str));
		}
		
		this.ops.add(selection);
		
	}
	
	//join := [JOIN][word][ON][word.word][=][word.word]
	private String [] parseJoinStatement (StringWrapper str) throws ParseException {
		
		String JOIN = parseWord(str, false); //read the JOIN
		checkParseError(JOIN, "JOIN");
		
		String rightTable = parseWord(str, false); //read the name of the first table in the join statement
		checkParseError(rightTable, null);
		
		String ON = parseWord(str, false); //read the ON
		checkParseError(ON, "ON");
		
		//reading the left half of the join condition
		String leftTableField = parseWord(str, false);
		checkParseError(leftTableField, null);
		
		String joinComparator = parseComparator(str, false); //read the method or join comparison
		checkParseError(joinComparator, "<", ">", "=");
		
		//reading the right half of the join condition
		String rightTableField = parseWord(str, false);
		checkParseError(rightTableField, null);
		
		return new String [] {JOIN, rightTable, ON, leftTableField, rightTableField};
		
	}
	
	//quantifier := [WHERE][conditional]([AND|OR][conditional])*
	private void parseQuantifier (StringWrapper str) throws ParseException {
		
		String WHERE = parseWord(str, false); //read where
		checkParseError(WHERE, "WHERE");
				
		String [] quantifier = new String [] {WHERE};
		quantifier = Utilities.appendArray(quantifier, parseConditional(str)); //parse the conditional
		
		String nextWord = nextWord(str);
		
		//parse any conditionals that follow
		while (nextWord.equals("AND") || nextWord.equals("OR")) {
			
			String curLogicalConjunction = parseWord(str, false);			
			checkParseError(curLogicalConjunction, "AND", "OR");
			
			quantifier = Utilities.appendString(curLogicalConjunction, quantifier);
			quantifier = Utilities.appendArray(quantifier, parseConditional(str));
			
			nextWord = nextWord(str); //need to see if another conditional is coming
			
		}
		
		this.ops.add(quantifier);
		
	}
	
	//conditional := [word][=|<=|>=|<|>|<>][word]
	private String [] parseConditional (StringWrapper str) throws ParseException {
		
		String field = parseWord(str, false);
		checkParseError(field, null);
		String comparator = parseComparator(str, false);
		checkParseError(comparator, null);
		String value = parseWord(str, false);
		checkParseError(value, null);
		
		return new String [] {field, comparator, value};
		
	}
	
	//query := [table_selection][SELECT][list_of_words][quantifier]
	private void parseQuery (StringWrapper str) throws ParseException {
		
		parseTableSelection(str);
		
		String SELECT = parseWord(str, false);
		checkParseError(SELECT, "SELECT");

		String fields = parseListOfWords(str);
		checkParseError(fields, null);
		
		this.ops.add(new String [] {SELECT, fields});
		
		if (str.peek() != none) {
			parseQuantifier(str);
		}
			
	}
	
	//read the next word in this StringWrapper.  the parameter is unaltered
	private String nextWord (StringWrapper str) throws ParseException {
		
		StringWrapper tempStr = new StringWrapper (str);
		return parseWord(tempStr, true);
		
	}
	
	private boolean isValidComparer (char check) {
		return check == '<' || check == '>' || check == '=';
	}
	
	private boolean isValidChar (char check) {
		return (check >= 'a' && check <= 'z') || (check >= 'A' && check <= 'Z') || (check >= '0' && check <= '9') || check == '_';
	}
	
	public int getCharsRead () {
		return this.charsRead;
	}
	
	/**
	 * Throws a ParseException if one is detected.  If compareTo is null, we are checking if parsedWord is empty, otherwise if parsedWord=compareTo
	 * @param parsedWord
	 * @param expected This value should be null if any value other than empty string is a valid parsedWord value
	 */
	public void checkParseError (String parsedWord, String... expected) throws ParseException {
		
		//check if anything was parsed
		if (expected == null) {
			
			if (parsedWord.equals("")) {
				throw new ParseException (this.charsRead, "Error on character " + this.charsRead + " - nothing was parse");
			}
			
		}
		
		else {
			
			//check all given values for a match
			for (String curPossibility: expected) {
				if (parsedWord.equals(curPossibility)) {
					return;
				}
			}
			
			//no match found, a parseexception has occured
			throw new ParseException (this.charsRead, "Error on parsed word " + parsedWord + " at " + this.charsRead + " - " + String.join(",", expected) + " was expected.");
			
		}
		
	}
	
	/**
	 * @return A 3-tuple where 0 = the string parsed 1 = beginning index 2 = end index
	 */
	public Object [] getLastParseStr () {
		
		String ret = "";
		int curToParseIndex = charsRead-1;
				
		while (curToParseIndex >= 0) {
			
			char curParseVal = this.lastQueryString.charAt(curToParseIndex);
			
			//whitespace eithers signals the beginning or end
			if (curParseVal == ' ') {
				
				//there is an indeterminate amount of white space to begin with, get rid of it
				if (ret.length() == 0) {
					
					//loop down until a space it found
					do {
						curToParseIndex--;
						ret = ret + ' ';
					} while (this.lastQueryString.charAt(curToParseIndex) == ' '); 
					
				}
				
				//if this is not the beginning of our read, then a whitespace signals the end of it
				else {
					break;
				}
				
			}
			
			else {
				ret = curParseVal + ret;
				curToParseIndex--;
			}
			
		}
		
		return new Object [] {ret, curToParseIndex+1, this.charsRead};
		
	}

	public static void main (String [] args) {
		
		String toParse = "FROM teams JOIN team_history_record ON = team_id SELCT ALL WHERE team_id >= 0 AND hr > 10";
		StringWrapper pntr = new StringWrapper (toParse);

		QueryParser parser = new QueryParser ();
		List <String []> parsedQuery = parser.parseQuery(toParse);
		System.out.println((String)parser.getLastParseStr()[0]);
		
	}
	
}

