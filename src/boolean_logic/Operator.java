package boolean_logic;

public class Operator implements QuantifierObject{
	
	private OperatorVal value;
	
	public Operator (String value) {
		this.value = OperatorVal.convert(value);
	}
	
	public boolean isNot () {
		return this.value.equals(OperatorVal.NOT);
	}
	
	public boolean isAnd () {
		return this.value.equals(OperatorVal.AND);
	}
	
	public boolean isOr () {
		return this.value.equals(OperatorVal.OR);
	}
	
	public boolean hasValue () {
		return value != null;
	}
	
	public String toString () {
		return "(" + this.value.toString() + ")";
	}
	
}
