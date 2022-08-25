package flywaynativeimage;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        FluentConfiguration configuration = new FluentConfiguration()
                .dataSource("jdbc:h2:mem:test", "user", "password")
                .encoding(StandardCharsets.UTF_8)
                .locations("classpath:db/migration");

        if (isRunningInNativeImage()) {
            configuration.resourceProvider(new GraalVMResourceProvider(configuration.getLocations()));
        }

        Flyway flyway = configuration.load();
        MigrateResult result = flyway.migrate();

        LOGGER.info("Migration successful: {}", result.success);
        LOGGER.info("Executed migrations: {}", result.migrationsExecuted);
    }

    private static boolean isRunningInNativeImage() {
        return System.getProperty("org.graalvm.nativeimage.imagecode") != null;
    }
}
