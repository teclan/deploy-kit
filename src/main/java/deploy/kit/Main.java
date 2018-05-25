package deploy.kit;

import java.io.Console;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import deploy.kit.check.FileCheck;
import deploy.kit.collect.DataCollecter;
import deploy.kit.exec.Commander;
import deploy.kit.module.MonitorModule;
import teclan.guice.Module.ConfigModule;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	@Inject
	@Named("config")
	private Config config;

	@Inject
	@Named("config.deploy.IntegratedMM.collect_path")
	private String path;

	@Inject
	private DataCollecter dataCollecter;
	@Inject
	private FileCheck fileCheck;
	@Inject
	private Commander commander;

	public void collectStart(String projectName) {
		LOGGER.info("正在采集项目 {} 数据，采集目录:{}，请稍等 ...", projectName,
				config.getString("deploy." + projectName + ".collect_path"));
		dataCollecter.collect(projectName, config.getString("deploy." + projectName + ".collect_path"));
		LOGGER.info("项目 {} 数据采集完成，采集目录:{} ...", projectName,
				config.getString("deploy." + projectName + ".collect_path"));
	}

	public void checkStart(String projectName) {
		fileCheck.check(projectName, config.getString("deploy." + projectName + ".deploy_path"));
	}

	public void deploy(String projectName) {

		LOGGER.info("正在部署项目 {}，请稍等...", projectName);

		commander.backup(config.getString("deploy." + projectName + ".deploy_path"), projectName);
		commander.delete(config.getString("deploy." + projectName + ".deploy_path"), projectName);

		commander.copy(config.getString("deploy." + projectName + ".war_path"),
				config.getString("deploy." + projectName + ".deploy_path"));
		commander.unPackageWar(config.getString("deploy." + projectName + ".deploy_path"), projectName + ".war");
		commander.copy(config.getString("deploy." + projectName + ".config_file_path") + "\\properties",
				config.getString("deploy." + projectName + ".deploy_path") + "\\" + projectName
						+ "\\WEB-INF\\classes\\properties");
		commander.copy(config.getString("deploy." + projectName + ".config_file_path") + "\\web.xml",
				config.getString("deploy." + projectName + ".deploy_path") + "\\" + projectName + "\\WEB-INF");

		LOGGER.info("\n恭喜！项目 {} 部署完成 ...", projectName);
	}

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ConfigModule("config.conf", "config"), new MonitorModule());

		Main main = injector.getInstance(Main.class);

		List<String> projects = main.config.getStringList("deploy.projects");
		LOGGER.info("检测到的项目有:{}\n", projects);

		String action = "";
		String project = "";

		if (args.length < 2) {
			LOGGER.info("缺少参数，第一个参数应该指定 collect，deploy，check 其中的一个，第二个参数指定项目名称");
			return;
		}
		action = args[0].trim();
		project = args[1].trim();


		if (!"collect".equals(action) && !"deploy".equals(action) && !"check".equals(action)) {
			LOGGER.error("指定的操作参数  {} 有误，必须是 collect，deploy，check 其中的一个...", action);
			return;
		}

		if (!projects.contains(project)) {
			LOGGER.error("指定的项目名称 {} 在配置文件中未找到 ...", project);
			return;
		}

		if ("collect".equals(action)) {
			main.collectStart(project);
		} else if ("deploy".equals(action)) {
			main.deploy(project);
		} else if ("check".equals(action)) {
			main.checkStart(project);
		}

		Console cs = System.console();

		if (cs == null) {
			return;
		}
		System.out.println("\n\n按任意键退出....");

		while (cs.readLine() != null) {
			System.exit(0);
		}
	}
}
