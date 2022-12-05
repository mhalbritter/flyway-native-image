package flywaynativeimage;

import java.nio.charset.StandardCharsets;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Main {
	public static void main(String[] args) {
		configureLogging(args);
		new Run().run();
	}

	private static void configureLogging(String[] args) {
		boolean debug = false;
		for (String arg : args) {
			if (arg.equals("--debug")) {
				debug = true;
				break;
			}
		}
		System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", debug ? "debug" : "info");
		LoggerFactory.getLogger(Main.class).debug("Debug log enabled");
	}

	private static class Run {
		private static final Logger LOGGER = LoggerFactory.getLogger(Run.class);

		void run() {
			FluentConfiguration configuration = new FluentConfiguration()
					.dataSource("jdbc:h2:mem:test", "user", "password")
					.encoding(StandardCharsets.UTF_8)
					.locations("classpath:db/migration");

			if (isRunningInNativeImage()) {
				configuration.resourceProvider(new GraalVMResourceProvider(configuration.getLocations()));
				configuration.javaMigrationClassProvider(new GraalVMClassProvider());
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
}
