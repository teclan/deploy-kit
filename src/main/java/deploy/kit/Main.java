package deploy.kit;

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
	@Named("deploy")
	private static Config config;

	@Inject
	@Named("deploy.projects")
	private static List<String> projects;

	@Inject
	private DataCollecter dataCollecter;
	@Inject
	private FileCheck fileCheck;
	@Inject
	private Commander commander;



	public void collectStart(String projectName) {
		dataCollecter.collect(projectName, config.getString(projectName + ".collect_path"));
		LOGGER.info("项目 {} 数据采集完成，采集目录:{} ...", projectName, config.getString(projectName + ".collect_path"));
    }

	public void checkStart(String projectName) {
		fileCheck.check(projectName, config.getString(projectName + ".deploy_path"));
	}

	public void deploy(String projectName) {
		commander.copy(config.getString(projectName + ".war_path"), config.getString(projectName + ".deploy_path"));
		commander.unPackageWar(config.getString(projectName + ".deploy_path"), projectName + ".war");
		commander.copy(config.getString(projectName + ".config_file_path") + "\\properties",
				config.getString(projectName + ".deploy_path") + "\\" + projectName + "\\WEB-INF\\classes\\properties");
		commander.copy(config.getString(projectName + ".config_file_path") + "\\web.xml",
				config.getString(projectName + ".deploy_path") + "\\" + projectName + "\\WEB-INF");

		LOGGER.info("\n项目 {} 部署完成 ...", projectName);
	}

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ConfigModule("config.conf", "config"), new MonitorModule());

		Main main = injector.getInstance(Main.class);

		List<String> projects = config.getStringList("projects");
		LOGGER.info("检测到的项目有:{}\n", projects);

		for (String project : projects) {
			// main.collectStart(project);
			main.deploy(project);
			main.checkStart(project);
		}
    }

}
