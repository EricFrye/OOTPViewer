package query;

//a work around to allow strings to be passed by reference
public class StringWrapper {
	
	public final char none = (char)0;
	private String value;
	
	public StringWrapper (String str) {
		this.value = str;
	}
	
	public StringWrapper (StringWrapper str) {
		this.value = str.value;
	}
	
	/**
	 * look at the character 1 ahead
	 * @return
	 */
	public char peekAhead () {
		
		if (value.length() <= 1) {
			return none;
		}
		
		else {
			return value.charAt(1);
		}
		
	}
	
	/**
	 * Look ahead by a specified amount
	 * @param by
	 * @return
	 */
	public char peekAhead (int by) {
		
		if (value.length() <= (by)) {
			return none;
		}
		
		else {
			return value.charAt(by);
		}
		
	}
	
	/**
	 * look at the current character
	 * @return
	 */
	public char peek () {
		
		if (value.length() == 0) {
			return none;
		}
		
		else {
			return value.charAt(0);
		}
		
	}
	
	public boolean hasNext () {
		return value.length() != 0;
	}
	
	
	/**
	 * Advances the string by one index and returns the passed over character
	 * @return
	 */
	public char consume () {
		
		//tried to read something that wasnt there. the user must have given us a bad string (assuming proper implementation, big if for me)
		if (!hasNext()) {
			throw new IllegalStateException("");
		}
		
		else {
			char ret = value.charAt(0);
			value = value.substring(1);
			return ret;
		}
		
	}
	
}


