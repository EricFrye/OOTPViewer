package misc;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Type;

import java.io.*;

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
	
	/**
	 * Checks if test is in the array comp
	 * @param test The String to check
	 * @param comp The array to check for containment
	 * @return True if test is in comp, False otherwise
	 */
	public static boolean in (String test, String [] comp) {
		
		for (String cur: comp) {
			
			if (test.equals(cur)) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * Finds the amount of unique strings in the two arrays
	 */
	public static int numUnique (String [] first, String [] second) {
		
		Map <String, Object> test = new HashMap <String, Object> ();
		
		for (String cur: first) {
			
			if (!test.containsKey(cur)) {
				test.put(cur, new Object());
			}
			
		}
		
		for (String cur: second) {
			
			if (!test.containsKey(cur)) {
				test.put(cur, new Object());
			}
			
		}
		
		return test.size();
		
	}
	
	/**
	 * Adds the string toInsert at the index given.  toInsert will have an integer appended to the end if it is not unique in  arr.  arr will be expanded if there is not enough space
	 * @param arr
	 * @param index
	 * @param toInsert
	 * @return Array of strings with toInsert added
	 */
	public static String [] insert (String [] arr, String toInsert) {
		
		int index = firstNonNullIndex(arr)+1;
		
		//check if our array needs to get bigger
		if (index == arr.length) {
			arr = growArray(arr);
		}
		
		Pattern p = Pattern.compile("^"+toInsert+"(\\d*)$");
		int max = 0;
		
		for (int curIndex = 0; curIndex < index; curIndex++) {
			
			Matcher m = p.matcher(arr[curIndex]);
			
			//field name match found
			if (m.find()) {
				String val = m.group(1);
				
				if (val.length() != 0) {
					max = Math.max(max, Integer.parseInt(val)+1);
				}
				
				else {
					max = 1;
				}
				
			}
			
		}
		
		arr[index] = toInsert + (max == 0 ? "" : max);
		return arr;
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return The index corresponding to the first non null index in arr
	 */
	public static int firstNonNullIndex (String [] arr) {
		
		int size = arr.length-1;
		
		//determine the rightmost index where null is not found
		while (size >= 0 && arr[size] == null) {
			size--;
		}
		
		return size;
		
	}
	
	/**
	 * @param cur
	 * @return cur doubled in size
	 */
	public static String [] growArray (String [] cur) {
		
		String [] ret = new String [cur.length * 2]; 
		
		for (int i = 0; i < cur.length; i++) {
			ret[i] = cur[i];
		}
		
		return ret;
		
	}
	
	/**
	 * Removes elements from arr that are null
	 * @param arr The array
	 * @param size The true size of the array
	 * @return
	 */
	public static String [] shrinkArray (String [] arr) {
		
		int size = firstNonNullIndex(arr)+1;
		
		String [] ret = new String [size];
		
		for (int i = 0; i < size; i++) {
			ret[i] = arr[i];
		}
		
		return ret;
		
	}
	
	public static void main (String [] args) {
		
		String [] arr = new String [2];
				
		arr = insert(arr, "player_id");
		arr = insert(arr, "team_id");
		arr = insert(arr, "team_id");
		arr = insert(arr, "league_id");
		arr = insert(arr, "team_id");
		arr = insert(arr, "t");
		arr = insert(arr, "t");
		
		arr = shrinkArray(arr);
		
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
		
	}
	
	/**
	 * Append s to the end of arr
	 * @param s
	 * @param arr
	 * @return
	 */
	public static String [] appendString (String s, String [] arr) {
		
		String [] newArr = new String [arr.length+1];
		
		for (int curStrIndex = 0; curStrIndex < arr.length; curStrIndex++) {
			newArr[curStrIndex] = arr[curStrIndex];
		}
		
		newArr[arr.length] = s;
		return newArr;
		
	}
	
	/**
	 * Append s2 to the end of s1
	 * @param s1
	 * @param s2
	 * @return A single array
	 */
	public static String [] appendArray (String [] s1, String [] s2) {
		
		String [] newArr = new String [s1.length+s2.length];
		
		int lenS1 = s1.length;
		int lenS2 = s2.length;
		
		for (int curS1Index = 0; curS1Index < lenS1; curS1Index++) {
			newArr[curS1Index] = s1[curS1Index];
		}
		
		for (int curS2Index = 0; curS2Index < lenS2; curS2Index++) {
			newArr[lenS1+curS2Index] = s2[curS2Index];
		}
		
		return newArr;
		
	}
	
	/**
	 * Finds the highest index that is less than start which is a space character.  Ignores leading spaces
	 * @param str
	 * @param start
	 * @return
	 */
	public static int [] backwardsSpaceIndex (String str, int start) {
		
		if (start < 0 || start >= str.length()) {
			return null;
		}
		
		else {
			
			int curIndex = start;
			
			//consume as many ending spaces as possible 
			while (curIndex >= 0 && str.charAt(curIndex) == ' ') {
				curIndex--;
			}
			
			int endIndex = curIndex+1;
			
			//consume characters until a space, indicative of the end of the parse, is read
			while (curIndex >= 0 && str.charAt(curIndex) != ' ') {
				curIndex--;
			}
			
			return new int [] {curIndex+1,endIndex};
			
		}
		
	}
	
	public static boolean shouldSwap (String left, String right, boolean isAsc, boolean nonString) {
		
		boolean result;
		
		if (nonString) {
			result = left.compareTo(right) > 0;
		}
		
		else {
			result = Double.parseDouble(left) > Double.parseDouble(right);
		}
		
		return isAsc ? result : !result;
		
	}
	
}
