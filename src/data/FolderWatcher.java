package data;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import misc.Utilities;

public class FolderWatcher implements Runnable {
	
	private WatchService watcher;
	private WatchKey key;
	private Path writeTo;
	private Path readFrom;
	
	public FolderWatcher (String fromDir, String toDir) {
		
		this.readFrom = Paths.get(fromDir);
		this.writeTo = Paths.get(toDir);
		
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
			
			try {
				key = this.watcher.take();
			} catch (InterruptedException i) {
				i.printStackTrace();
			}
			
			if (key != null) {
			
				for (WatchEvent <?> event: key.pollEvents()) {
					
					WatchEvent.Kind <?> kind = event.kind();
					
					if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
						
						Path fileName = ((WatchEvent<Path>)event).context();
						
						String folderName = System.nanoTime() + "";
						String newFolder = Utilities.createPathString(this.writeTo.toString(), folderName);
						Path newFolderPath = Paths.get(newFolder);
						new File(newFolderPath.toString()).mkdir();
						
						Path resolvedFile = readFrom.resolve(fileName);
						Path newFile = newFolderPath.resolve(fileName);
						
						try {
							Files.copy(resolvedFile, newFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
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
