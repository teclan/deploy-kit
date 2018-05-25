package teclan.monitor.file.listener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import teclan.monitor.db.Database;

@Singleton
public class FileChangeListener {
	private final Logger LOGGER = LoggerFactory.getLogger(FileChangeListener.class);

	@Inject
	private ExecutorService executorService;
	@Inject
	private Database database;

	public void recover(final String fromFile, final String toFile) {
		
		executorService.execute(new Runnable() {
			public void run() {
				try {
					Files.copy(new File(fromFile), new File(toFile));
					database.openDatabase();

					database.closeDatabase();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		});
	}

}
