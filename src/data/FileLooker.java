package data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A FileLooker searchers through the data directory in search of matching files
 * @author Eric
 *
 */
public class FileLooker {
	
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
