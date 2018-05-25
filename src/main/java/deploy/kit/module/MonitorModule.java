package deploy.kit.module;

import java.util.concurrent.ExecutorService;

import com.google.inject.AbstractModule;

import deploy.kit.db.Database;
import deploy.kit.provider.DatabaseProvider;
import deploy.kit.provider.ExecutorServiceProvider;

public class MonitorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Database.class).toProvider(DatabaseProvider.class);
        bind(ExecutorService.class).toProvider(ExecutorServiceProvider.class);
    }
}
