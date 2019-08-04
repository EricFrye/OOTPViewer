package boolean_logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import misc.StringWrapper;

public class Quantifier implements QuantifierObject {
	
	private LinkedList <QuantifierObject> components;

	public Quantifier () {
		this.components = new LinkedList <QuantifierObject> ();
	}
	
	public void parseQuantifier (String str) {
		parseQuantifierHelper(new StringWrapper(str));
	}
	
	private Quantifier parseQuantifierHelper (StringWrapper str) {
		
		parseWhiteSpace(str); //we can ignore any leading whitesapce - get to the good stuff
		
		while (str.hasNext()) {
			
			char next = str.peek();
			
			//go a level deeper into the statement - we will return either once the corresponding ) is read
			if (next == '(') {
				str.consume();
				this.components.add(new Quantifier().parseQuantifierHelper(str));
			}
			
			//base case
			//closing of the current quantifier
			else if (next == ')') {
				str.consume();
				return this;
			}
			
			else {
				
				String curWord = parseWord(str);
				
				Operator op = new Operator (curWord);
				
				//this word is an operator
				if (op.hasValue()) {
					this.components.add(op);
				}
				
				//not an operator, must be a query.  need to consume the comparator and value
				else {
					
					String comp = parseComparator(str);
					String value = parseWord(str);
					this.components.add(new Query(curWord, comp, value));
					
				}
				
			}
			
		}
		
		//we ran out of stuff to parse
		return this;
		
	}
	
	private String parseComparator (StringWrapper str) {
		
		String ret = "";
		
		parseWhiteSpace(str);
		
		while (isValidComparer(str.peek())) {
			ret += str.consume();
		}
		
		if (ret.equals("")) {
			
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
		parseWhiteSpace(str);
		
		String word = "";
		
		while (isValidChar(str.peek())) {
			word += str.consume();
		}
		
		parseWhiteSpace(str);
		
		return word;
		
	}
	
	private boolean isValidComparer (char check) {
		return check == '<' || check == '>' || check == '=';
	}
	
	public String toString () {
		
		String ret = "[";
		
		for (QuantifierObject qo: this.components) {
			ret += qo.toString();
		}
		
		return ret + "]";
		
	}
	
	private boolean isValidChar (char check) {
		return (check >= 'a' && check <= 'z') || (check >= 'A' && check <= 'Z') || (check >= '0' && check <= '9') || check == '_';
	}
	
	public boolean test (Map <String, Integer> mappings, String [] ent) throws UnsupportedDataTypeException {
		
		boolean result = true; //we will assume true until we find otherwise
		
		boolean not = false;
		boolean and = false;
		boolean or = false;
		
		for (QuantifierObject curQuanObj: components) {
			
			if (curQuanObj instanceof Operator) {
				
				if (((Operator) curQuanObj).isOr()) {
					or = true;
				}
				
				else if (((Operator) curQuanObj).isAnd()) {
					and = true;
				}
				
				else {
					not = true;
				}
				
			}
			
			else {
				
				boolean curResult;
				
				if (curQuanObj instanceof Quantifier) {
					curResult = ((Quantifier)curQuanObj).test(mappings, ent);
				}
				
				else if (curQuanObj instanceof Query) {
					
					try {
						curResult = ((Query)curQuanObj).evaluateQuery(mappings, ent);
					} catch (Exception e) {
						System.out.println("Error during query evaluation");
						return false;
					}
					
				}
				
				else {
					throw new IllegalStateException ();
				}
				
				curResult = not ? !curResult : curResult; //flip if the not bit is flipped
				
				//perform two term operations if needed
				if (and || or) {
					result = doTwoTermOperator(and, or, result, curResult);
				}
				
				else {
					result = curResult;
				}
				
				not = false;
				and = false;
				or = false;
				
			}
			
		}
		
		return result;
		
	}
	
	private boolean doNot (boolean notActive, boolean result) {
		return notActive ? !result : result;
	}
	
	private boolean doTwoTermOperator (boolean and, boolean or, boolean first, boolean second) {
		
		if (and) {
			return first && second;
		}
		
		else if (or) {
			return first || second;
		}
		
		else {
			throw new IllegalStateException();
		}
		
	}
	
	public static void main (String [] args) {
		
		String str = "c > 3 AND (a=8 OR b=9)";
		
		Map <String, Integer> mappings = new HashMap <String, Integer> ();
		mappings.put("a", 0);
		mappings.put("b", 1);
		mappings.put("c", 2);
		String [] ent =  new String [] {"8", "9", "3"};
		
		Quantifier q = new Quantifier ();
		q.parseQuantifier(str);
		System.out.println(q);
		
		try {
			System.out.println(q.test(mappings, ent));
		} catch (UnsupportedDataTypeException e) {
			e.printStackTrace();
		}
		
	}
	
}
