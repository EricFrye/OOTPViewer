package query;

//a work around to allow strings to be passed by reference
public class StringWrapper {
	
	private final char none = (char)0;
	String value;
	
	public StringWrapper (String str) {
		this.value = str;
	}
	
	//look at the character 1 ahead
	public char peekAhead () {
		
		if (value.length() < 2) {
			return none;
		}
		
		else {
			return value.charAt(1);
		}
		
	}
	
	//look at the next character
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


