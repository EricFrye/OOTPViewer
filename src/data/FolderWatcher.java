package data;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FolderWatcher implements Runnable {
	
	private WatchService watcher;
	private WatchKey key;
	
	private Path rootWriteTo; //the folder that folder folderName will be created in
	private Path writeTo; //the folder that exported files from OOTP should be written to.  rootWriteTo/folderName
	private Path readFrom; //the folder the OOTP will export the files to
	private Long folderName; //the folder name in root to differentiate exports
	private Long lastUpdateTime; //time in nano seconds when the last update occured
	
	private final Long timeToNeedNewFolder = (long) (10*Math.pow(10.0, 9.0));//how much time is allowed until a export goes to a new folder
	
	public FolderWatcher (String fromDir, String rootWriteTo) {
		
		this.readFrom = Paths.get(fromDir);
		this.rootWriteTo = Paths.get(rootWriteTo);
		
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.key = this.readFrom.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run () {
		
		for (;;) {
			
			WatchKey key = null;
			
			//get the current batch of file changes
			try {
				key = this.watcher.take();
			} catch (InterruptedException i) {
				i.printStackTrace();
			}
			
			//update the folder to write to if it is appropriate
			if (this.folderName == null || System.nanoTime() - this.lastUpdateTime >= timeToNeedNewFolder) {
				
				this.folderName = System.nanoTime();
				this.writeTo =  this.rootWriteTo.resolve(this.folderName + "");
				this.writeTo.toFile().mkdir();
				
			}
			
			if (key != null) {
				
				for (WatchEvent <?> event: key.pollEvents()) {
					
					WatchEvent.Kind <?> kind = event.kind();
					
					if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
						
						Path fileName = ((WatchEvent<Path>)event).context();
						Path resolvedFile = readFrom.resolve(fileName);
						
						try {
							Files.copy(resolvedFile, this.writeTo.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						this.lastUpdateTime = System.nanoTime();
						
					}
					
					//enter code here
					if (!key.reset()) {
						break;
					}
					
				}
				
			}
			
		}
		
	}
	
	public static void main (String [] args) {
		
		new FolderWatcher ("C:\\Users\\Eric\\Documents\\Out of the Park Developments\\OOTP Baseball 19\\saved_games\\New Game 3.lg\\import_export\\csv","data").run();
		
	}
	
}
