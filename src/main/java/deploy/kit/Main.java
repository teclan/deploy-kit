package deploy.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import deploy.kit.check.FileCheck;
import deploy.kit.collect.DataCollecter;
import deploy.kit.module.MonitorModule;
import teclan.guice.Module.ConfigModule;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Inject
	private DataCollecter dataCollecter;
	@Inject
	private FileCheck fileCheck;


	@Inject
	@Named("config.deploy.IntegratedMM.collect_path")
	private String integratedMMCollectPath; // 管理平台采集路径

	@Inject
	@Named("config.deploy.IntegratedMM.deploy_path")
	private String integratedMMDeployPath; // 管理平台部署路径

    public void collectStart() {
		dataCollecter.collect("IntegratedMM", integratedMMCollectPath);
		LOGGER.info("管理平台数据采集完成...");
    }

	public void checkStart() {

		fileCheck.check("IntegratedMM", integratedMMDeployPath);

	}

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(
                new ConfigModule("config.conf", "config"), new MonitorModule());

		Main main = injector.getInstance(Main.class);
		// main.collectStart();
		main.checkStart();
    }

}
