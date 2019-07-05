package query;

import java.util.*;

public class QueryReservedWords {
	
	private static final String SELECT = "SELECT";
	private static final String JOIN = "JOIN";
	private static final String ON = "ON";
	private static final String WHERE = "WHERE";
	private static final String FROM = "FROM";
	private static final String AND = "AND";
	private static final String OR = "OR";
	private static final String ALL = "ALL";
	
	public static boolean isSelect (String check) {
		return check.equals(SELECT);
	}
	
	public static boolean isJoin (String check) {
		return check.equals(JOIN);
	}
	
	public static boolean isOn (String check) {
		return check.equals(ON);
	}
	
	public static boolean isWhere (String check) {
		return check.equals(WHERE);
	}
	
	public static boolean isFrom (String check) {
		return check.equals(FROM);
	}
	
	public static boolean isAnd (String check) {
		return check.equals(AND);
	}
	
	public static boolean isOr (String check) {
		return check.equals(OR);
	}
	
	public static boolean isAll (String check) {
		return check.equals(ALL);
	}
	
}
