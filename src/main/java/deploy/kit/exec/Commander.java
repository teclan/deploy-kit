package deploy.kit.exec;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import teclan.exec.Executor;

@Singleton
public class Commander {
	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

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
	 * 备份文件
	 * 
	 * @param deployDir
	 * @param project
	 */
	public void backup(String deployDir, String project) {
		try {
			String from = deployDir + File.separator + project;
			String to = deployDir + File.separator + project + "_" + DATE_FORMAT.format(new Date());
			Executor.exec("cmd", "/c", "move", from,to);
			LOGGER.info("备份 {} 至 {} 完成 ...", from, to);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void delete(String deployDir, String project) {
		try {
			String path = deployDir + File.separator + project + ".war";
			Executor.exec("cmd", "/c", "delete", path);
			LOGGER.info("删除 {} 完成...", path);
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
