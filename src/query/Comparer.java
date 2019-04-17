package query;

public enum Comparer {
	EQ,GT,GTE,LT,LTE;
	
	public static Comparer getVal (String val) {
		
		switch (val) {
			case "=":
				return EQ;
			case ">":
				return GT;
			case ">=":
				return GTE;
			case "<":
				return LT;
			case "<=":
				return LTE;
			default:
				return null;
		}
		
	}
	
}
