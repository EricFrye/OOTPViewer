package misc;
import java.util.*;
import java.io.*;

import Data.Type;

public class Utilities {
	
	/**
	 * Read from the file storing info about the types of the data files
	 * @param fileDataPath The full file path to the fileData file
	 * @param file The file that the types are to be loaded for
	 * @return An array containing the types for the specified Holder
	 */
	public static Type [] loadTypes (String fileDataPath, String file) {
		
		Scanner input = null;
		
		try {
			input = new Scanner (new File(fileDataPath));
		} catch (FileNotFoundException e) {
			return null;
		}
		
		while (input.hasNextLine()) {
			
			String curLine = input.nextLine();
			String [] name = curLine.split(":");
			
			//correct entry
			if (name[0].equals(file)) {
				
				String [] types = name[1].split(",");
				Type [] ret = new Type [types.length];
				
				for (int typeIndex = 0; typeIndex < types.length; typeIndex++) {
					ret[typeIndex] = Type.convert(types[typeIndex]);
				}
				
				return ret;
				
			}
			
		}
		
		return null;
		
	}
	
	
}
