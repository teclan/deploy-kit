package deploy.kit.check;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import deploy.kit.db.Database;
import deploy.kit.db.model.FileRecords;
import teclan.utils.FileUtils;

@Singleton
public class FileCheck {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCheck.class);

	@Inject
	private Database database;

	public void check(String projectName, String deployPath) {

		String deployDir = deployPath + File.separator + projectName;

		File file = new File(deployDir);

		if (!file.exists()) {
			LOGGER.error("文件 {} 不存在 ...", deployDir);
			return;
		}

		if (!file.isDirectory()) {
			LOGGER.error("文件 {} 不是一个有效目录 ...", deployDir);
			return;
		}

		try {
			database.openDatabase();

			check(file.getAbsolutePath(), file);

			database.closeDatabase();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error("检验项目:{}出错，请调整后重试 ...", projectName);
		}

		LOGGER.info("项目:{} 校验完成 ...", projectName);
	}

	private void check(String deployDir, File file) {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				check(deployDir, files[i]);
			}
		} else {

			String checkPath = file.getAbsolutePath().replace(deployDir, "");

			FileRecords fileRecord = FileRecords.findFirst("path=?", checkPath);

			if (fileRecord == null) {
				LOGGER.error("未发现采集数据,文件 {} ", checkPath);
				return;
			}

			if (fileRecord.getLong("length") != file.length()) {
				LOGGER.error("文件 {} 与采集的数据不匹配，源文件大小:{},真实大小:{}", checkPath, fileRecord.getLong("length"),
						file.length());
				return;
			}
			String md5 = FileUtils.getFileSummary(file, "MD5");
			if (!fileRecord.getString("summary").equals(md5)) {
				LOGGER.error("文件 {} 与采集的数据不匹配，源文件MD5:{},真实MD5:{}", fileRecord.getLong("summary"), md5);
				return;
			}
		}
	}
}
