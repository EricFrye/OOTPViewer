package boolean_logic;

public enum OperatorVal {
	NOT,AND,OR;
	
	public static OperatorVal convert (String str) {
		
		switch (str) {
			case "NOT": return OperatorVal.NOT;
			case "AND": return OperatorVal.AND;
			case "OR": return OperatorVal.OR;
			default: return null;
		}
		
	}
	
}
