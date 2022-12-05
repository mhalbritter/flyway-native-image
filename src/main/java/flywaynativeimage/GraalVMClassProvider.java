package flywaynativeimage;

import java.util.Collection;
import java.util.Set;

import db.migration.V3__alter_table_again;
import org.flywaydb.core.api.ClassProvider;
import org.flywaydb.core.api.migration.JavaMigration;

/**
 * @author Moritz Halbritter
 */
class GraalVMClassProvider implements ClassProvider<JavaMigration> {
	@Override
	public Collection<Class<? extends JavaMigration>> getClasses() {
		// TODO: Find a way to discover those classes at runtime
		return Set.of(
				V3__alter_table_again.class
		);
	}
}
