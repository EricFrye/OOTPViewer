package query;

public class ParseException extends Throwable{
	
	private int index;
	private String error;
	
	public ParseException (int index, String message) {
		super(message);
		this.error = message;
		this.index = index;
	}
	
	public int getIndex () {
		return this.index;
	}
	
	public String getMessage () {
		return this.error;
	}
	
	public void setMessage (String msg) {
		this.error = msg;
	}
	
}
