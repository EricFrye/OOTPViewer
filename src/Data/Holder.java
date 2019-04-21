package Data;
import java.util.*;
import java.util.regex.Pattern;

import query.LogicalStatement;
import query.Query;
import query.QueryResult;

import java.io.*;

/***
 * InfoHolder is the main mechanism for loading and accessing the data files
 * @author Eric
 *
 */
public class Holder {
	
	private List <String> mappings; //maps the col names to their index. the index in the list is the index in the report
	private List <Entity> data; //holds the actual data
	private String directoryPath;
	private String fileName;
	
	/***
	 * Creates the InfoHolder object
	 * @param filePath the path to the target data file
	 */
	public Holder (String directoryPath, String fileName) {
		
		this.mappings = new ArrayList <String> ();
		this.data = new ArrayList <Entity> ();
		this.directoryPath = directoryPath;
		this.fileName = fileName;
		
	}
	
	/**
	 * Call from child
	 * @param directoryPath path to directory fileName is in
	 * @param fileName name of file 
	 * @param mappings
	 * @param data
	 */
	public Holder (List <String> mappings, List <Entity> data) {
		this.directoryPath = "";
		this.fileName = "";
		this.mappings = mappings;
		this.data = data;
	}
	
	/**
	 * Standard loading function.  All columns are kept
	 */
	public void loadInfo () throws Exception {
		
		List <File> files = findAllFiles();
		Thread [] workers = new Thread [files.size()];
		
		for (int i = 0; i < files.size(); i++) {
			
			File curFile = files.get(i);
			Scanner curInput = new Scanner (curFile);
			
			//the first file has header info, we need this before anything can start
			if (i == 0) {
				
				String header = curInput.nextLine();
				String [] headers = header.split(",");

				for (String curHeader: headers) {
					this.mappings.add(curHeader);
				}
				
			}
			
			FileLoader curWorker = new FileLoader (curFile, curInput, this.data, this.mappings);
			workers[i] = new Thread (curWorker);
			
		}
		
		for (Thread curWorker: workers) {
			curWorker.start();
		}

		for (Thread curWorker: workers) {
			curWorker.join();
		}

	}
	
	/**
	 * Performs a query
	 * @param query The string representation of the query
	 * @return A query result object
	 */
	public QueryResult query (String query) {
		
		List <Query> queries = LogicalStatement.parse(query);
		List <Entity> ret = new LinkedList <Entity> ();
		
		for (Entity curEnt: data) {
			
			boolean success;
			
			try {
				success = curEnt.query(queries);
			}
			catch (Exception e) {
				success = false;
			}
			
			if (success) {
				ret.add(curEnt);
			}
			
		}
		
		return new QueryResult(ret, queries, this.mappings);
		
	}
	
	//TODO
	public String buildReport () {
		
		String ret = "";
		
		for (Entity curEnt: data) {
			
			
		}
		
		return ret;
		
	}
	
	/**
	 * @return The count of entries in this Holder
	 */
	public int numEntities () {
		return mappings.size();
	}
	
	/**
	 * @param directoryPath String representation of the path to the directory the target files are currently in.  No trailing /
	 * @param mainFileName Name of file to search for splits of.  The splits will be of the form mainFileName_\d+
	 * @return An List of all the files matching the parameters
	 */
	public List <File> findAllFiles () {
		
		File searchDir = new File (this.directoryPath);
		File [] allFilesInDir = searchDir.listFiles();
		
		String pattern = String.format("^%s(_\\d+){0,1}.csv$", this.fileName);
		List <File> matchedFiles = new LinkedList <File> ();
		
		for (File curFile: allFilesInDir) {
			
			if (curFile.getName().matches(pattern)) {
				matchedFiles.add(curFile);
			}
			
		}
		
		return matchedFiles;
		
	}
	
	
	public String asCSV () {
		
		String ret = "";
		
		for (String curMapping: mappings) {
			ret += curMapping + ",";
		}
		
		ret = ret.substring(0, ret.length()-1) + "\n";
		
		for (Entity curEnt: data) {
			ret += curEnt.asCSV(mappings);
		}
		
		return ret;
		
	}
	
	public static void main (String [] args) {
		
		String dir = "C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv";
		String file = "players_game_batting";
		
		Holder players = new Holder (dir, file);
		try {
			players.loadInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	protected List <Entity> getData () {
		return data;
	}
	
	protected Entity getTop () {
		return data.size() == 0 ? null : data.get(0);
	}
	
}
