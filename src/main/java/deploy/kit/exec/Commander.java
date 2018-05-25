package deploy.kit.exec;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import teclan.exec.Executor;

@Singleton
public class Commander {
	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);

	/**
	 * 复制文件
	 * 
	 * @param from
	 * @param to
	 */
	public void copy(String from, String to) {
		try {
			Executor.exec("cmd", "/c", "copy", from, to);
			LOGGER.info("复制成功,{}--->{}", from, to);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 解压 war 包
	 * 
	 * @param deployDir
	 * @param war
	 */
	public void unPackageWar(String deployDir, String war) {
		String currentPath = System.getProperty("user.dir");
		String haoZipDir = currentPath + File.separator + "HaoZip";

		try {
			Executor.exec("cmd", "/c", haoZipDir + "/HaoZipC", "x", deployDir + File.separator + war,
					"-o" + deployDir + File.separator + war.substring(0, war.lastIndexOf(".")));
			LOGGER.info("解压 {} 至 {} 完成 ...", war, war.substring(0, war.lastIndexOf(".")));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

}
