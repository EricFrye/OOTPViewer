package Data;

public enum Type {
	INT,DOUBLE,STRING;
	
	/**
	 * 
	 * @param val String relating to the type.  One of I,D,S
	 * @return Corresponding enum, or null if invalid val given
	 */
	public static Type convert (String val) {
		switch (val) {
		
			case "I":
				return Type.INT;
			case "D":
				return Type.DOUBLE;
			case "S":
				return Type.STRING;
			default:
				return null;
		
		}
	}
	
}
