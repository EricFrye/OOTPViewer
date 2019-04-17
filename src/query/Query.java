package query;

/***
 * A Query is a logical statement that can be used to lookup inforamtion from the datafiles
 * @author Eric
 *
 */
public class Query {

	private String field;
	private Comparer comp;
	private String value;

	/***
	 * 
	 * @param g0 The field
	 * @param g1 The comparater
	 * @param g2 The value
	 */
	public Query (String g0, String g1, String g2) {
		this.field = g0;
		this.comp = Comparer.getVal(g1);
		this.value = g2;
	}

	public String toString() {
		return "Query [field=" + field + ", comp=" + comp + ", value=" + value + "]";
	}

	public String getField () {
		return field;
	}

	/**
	 * Checks if the given value fulfills this query 
	 * @param otherValue The value to check
	 * @return
	 */
	public boolean comp (String otherValue) {

		if (this.comp.equals(Comparer.EQ)) {
			return otherValue.equals(value);
		}
		
		int valueInt = Integer.parseInt(value);
		int otherValueInt = Integer.parseInt(otherValue);

		if (this.comp.equals(Comparer.LT)) {
			return otherValueInt < valueInt; 
		}

		else if (this.comp.equals(Comparer.LTE)) {
			return otherValueInt <= valueInt;
		}

		else if (this.comp.equals(Comparer.GT)) {
			return otherValueInt > valueInt;
		}

		else if (this.comp.equals(Comparer.GTE)) {
			return otherValueInt >= valueInt;
		}

		else {
			return false;
		}

	}

}
