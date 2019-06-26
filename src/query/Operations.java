package query;

import Data.Holder;

public class Operations {
	
	public static Holder from (String file) {
		
		Holder newHolder = new Holder ("data", file);
		
		try {
			newHolder.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return newHolder;
		
	}
	
	public static void select (Holder holder, String fields) {
		holder.select(fields);
	}
	
	public static void where (Holder holder, String queryStr) {
		holder.query(queryStr);
	}

}
