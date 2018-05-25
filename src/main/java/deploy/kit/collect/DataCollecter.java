package deploy.kit.collect;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import teclan.utils.FileUtils;

@Singleton
public class DataCollecter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataCollecter.class);

	@Inject
	private CollectListener collectListener;


	public void collect(String projectName, String collectPath) {

		File file = new File(collectPath);

		if (!file.exists()) {
			LOGGER.error("文件 {} 不存在 ...", collectPath);
			return;
		}
		
		if(!file.isDirectory()) {
			LOGGER.error("文件 {} 不是一个有效目录 ...", collectPath);
			return;
		}
		summary(projectName, collectPath, file);
	}

	private void summary(String projectName,String collectPath, File file) {
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
}
