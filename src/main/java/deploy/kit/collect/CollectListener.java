package deploy.kit.collect;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import deploy.kit.db.Database;
import deploy.kit.db.model.FileRecords;

@Singleton
public class CollectListener {
	private final Logger LOGGER = LoggerFactory.getLogger(CollectListener.class);

	@Inject
	private ExecutorService executorService;
	@Inject
	private Database database;

	public void hanlde(final String projectName, final String path, final long length, final String summary) {

		executorService.execute(new Runnable() {
			public void run() {
				try {
					database.openDatabase();
					FileRecords.create("project_name", projectName, "path", path, "length", length, "summary", summary)
							.saveIt();
					database.closeDatabase();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		});
	}

}
