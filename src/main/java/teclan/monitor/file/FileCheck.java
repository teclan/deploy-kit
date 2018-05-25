package teclan.monitor.file;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import teclan.monitor.db.Database;
import teclan.monitor.db.model.FileRecords;
import teclan.monitor.file.listener.FileChangeListener;

@Singleton
public class FileCheck {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCheck.class);

	@Inject
	@Named("config.file.monitor-dir")
	private String monitorDir;
	@Inject
	@Named("config.file.backup-dir")
	private String backupDir;

	@Inject
	private Database database;
	@Inject
	private FileChangeListener fileChangeListener;

	public void init() {
		File file = new File(backupDir);
		database.openDatabase();
		database.closeDatabase();

	}

	public void check() {
		database.openDatabase();
		List<FileRecords> records = FileRecords.findAll();

		database.closeDatabase();
		

		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		check();
	}


}
