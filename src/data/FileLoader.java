package data;

import java.io.*;
import java.util.*;

import boolean_logic.Query;
import query.Queries;

/**
 * FileLoader concurrently reads the data for a file that has been split up, which is an option in the export menu for OOTP 
 * @author Eric
 *
 */
public class FileLoader implements Runnable {

	private Scanner fd;
	private Data data;
	private Map <String, Integer> mappings;
	private File src;
	private int entitiesLoaded; 
	private Queries queries;
	
	/**
	 * @param src The file that fd will be reading from
	 * @param fd Reference to the Scanner object which this loader will read from as generated by Holder.findAllFiles
	 * @param data Reference to Holder.data
	 * @param mappings Reference to Holder.mappings
	 */
	public FileLoader (File src, Scanner fd, Data data, Map <String, Integer> mappings, Queries queries) {
		this.fd = fd;
		this.data = data;
		this.mappings = mappings;
		this.src = src;
		this.queries = queries;
	}

	public void run () {

		System.out.println(String.format("Beginning load for %s.", src.getAbsolutePath()));
		
		while (fd.hasNextLine()) {

			String line = fd.nextLine();
			line = line.replaceAll("\"\"", " ");
			line = line.replaceAll("\"", "");
			String [] toAdd = line.split(",");

			//check if there is a condition to this load.  if there is check that this entity even passes
			if (this.queries == null || this.queries.test(this.mappings, toAdd)) {
			
				synchronized (this.data) {
					data.addEntity(toAdd);
					entitiesLoaded++;
				}

			}

			
		}
		
		System.out.println(String.format("Ending load for %s.", src.getAbsolutePath()));
		
	}
	
	/**
	 * @return Returns the amount of entities that this worker loaded
	 */
	public int getEntitiesLoaded () {
		return entitiesLoaded;
	}

}
