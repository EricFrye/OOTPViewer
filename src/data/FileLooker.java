package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A FileLooker searchers through the data directory in search of matching files
 * @author Eric
 *
 */
public class FileLooker {
	
	/**
	 * 
	 * @param file
	 * @param source No .csv
	 * @param mode 0 = all fields 1 = ids otherwise non identifier fields
	 * @return
	 */
	public static String [] loadFieldNames (String file, String source, int mode) {

		File foundFile = findFirstFileByName(file, source);
		
		if (foundFile != null) {
					
			//read the top line and return the field names
			try {
				
				Scanner csvHeaderReader = new Scanner (foundFile);
				String [] headers = csvHeaderReader.nextLine().split(",");
				csvHeaderReader.close();
				
				//mode has a value, if set to 1 then we only want ids, otherwise we only want non ids
				if (mode != 0) {
				
					List <String> ret = new LinkedList <String> ();
					
					for (String curHeaderToTest: headers) {
						
						boolean addHeader = curHeaderToTest.contains("_id");
						
						if (mode == 1 ? addHeader : !addHeader) {
							ret.add(curHeaderToTest);
						}
						
					}
					
					return ret.toArray(new String[ret.size()]);
					
				}
					
				return headers;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
					
		}
		
		return null;
				
	}
	
	/**
	 * Search the directory for the latest yearly data dump for a specific file
	 * @param file
	 * @param directory
	 * @return Null if no year folder was found or if the file does not exist in the highest year dump
	 */
	public static List <File> getLatestFileVersion (String directory, String file) {
		
		File [] files = new File(directory).listFiles();
		File containingFolder = null;
		
		Pattern r = Pattern.compile(String.format("^dump_(\\d+)_yearly$"));
		int highestYearSaved = 0;
		
		for (File curFile: files) {
			
			if (curFile.isDirectory()) {
				
				Matcher m = r.matcher(curFile.getName());
				
				if (m.find()) {
					
					int curYear = Integer.parseInt(m.group(1));
					
					if (curYear > highestYearSaved) {
						highestYearSaved = curYear;
						containingFolder = curFile;
					}
					
				}
				
			}
			
		}
		
		//try to grab the file in the highest year folder
		return containingFolder != null ? FileLooker.findFiles(file, containingFolder) : null;

	}
	
	public static List <File> getLatestFileVersion (String file) {
		return getLatestFileVersion("data", file);
	}

	/**
	 * Search for the named file in the given folder
	 * We want the base file with no number extensions such as players_1.csv.  Only the base file with no number extension has the csv headers
	 * @param file
	 * @param folder
	 * @return The File object, or null if it is not found
	 */
	private static File findFirstFileByName (String file, String folder) {

		File [] filesToCheck = new File(folder).listFiles();
		
		String pattern = String.format("^%s.csv$", file);
		
		for (File curFile: filesToCheck) {
			
			if (curFile.isDirectory()) {
				
				File returnedFile = findFirstFileByName(file, curFile.getPath());
				
				if (returnedFile != null) {
					return returnedFile;
				}
				
			}
			
			else {
		
				if (curFile.getName().matches(pattern)) {
					return curFile;
				}
				
			}
			
		}
		
		return null;
		
	}
	
	public static List <File> findFiles (String fileName, File dir) {
		
		List <File> toRet = new LinkedList <File> ();
		String pattern = String.format("^%s(_\\d+){0,1}.csv$", fileName);
		
		for (File cur: dir.listFiles()) {
			
			if (cur.isDirectory()) {
				
				List <File> retFiles = findFiles(fileName, cur.getAbsoluteFile());
				
				for (File toAdd: retFiles) {
					toRet.add(toAdd);
				}
						
			}
			
			else {
				
				if (cur.getName().matches(pattern)) {
					toRet.add(Paths.get(dir.toString(), cur.getName()).toFile());
				}
				
			}
			
		}
		
		return toRet;
		
	}
	
	public static void main (String [] args) {
		
		for (File curFile: findFiles("players", new File("D:\\Java_Projects\\OOTPViewer\\data"))) {
			System.out.println(curFile.getAbsolutePath());
		}
		
	}
	
}
