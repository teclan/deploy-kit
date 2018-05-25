package deploy.kit.collect;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import deploy.kit.db.Database;
import deploy.kit.db.model.FileRecords;
import teclan.utils.FileUtils;

@Singleton
public class DataCollecter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataCollecter.class);

	@Inject
	private CollectListener collectListener;
	@Inject
	private Database database;

	public void collect(String projectName, String collectPath) {

		File file = new File(collectPath);

		if (!file.exists()) {
			LOGGER.error("文件 {} 不存在 ...", collectPath);
			return;
		}

		if (!file.isDirectory()) {
			LOGGER.error("文件 {} 不是一个有效目录 ...", collectPath);
			return;
		}

		clean(projectName);

		summary(projectName, collectPath, file);
	}

	/**
	 * 采集文件数据
	 * 
	 * @param projectName
	 * @param collectPath
	 * @param file
	 */
	private void summary(String projectName, String collectPath, File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				summary(projectName, collectPath, files[i]);
			}
		} else {
			String md5 = FileUtils.getFileSummary(file, "MD5");
			collectListener.hanlde(projectName, file.getAbsolutePath().replace(collectPath, ""), file.length(), md5);
		}
	}

	/**
	 * 清理指定项目的采集数据，以便重新采集
	 * 
	 * @param projectName
	 */
	private void clean(String projectName) {

		try {
			database.openDatabase();
			FileRecords.delete("project_name = ?", projectName);
			database.closeDatabase();
			LOGGER.info("项目 {} 的历史数据清理完成 ...", projectName);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
