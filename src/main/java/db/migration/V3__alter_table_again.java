package db.migration;

import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
 * @author Moritz Halbritter
 */
public class V3__alter_table_again extends BaseJavaMigration {
	@Override
	public void migrate(Context context) throws Exception {
		try (Statement statement = context.getConnection().createStatement()) {
			statement.execute("""
					   ALTER TABLE test	ADD COLUMN street VARCHAR NOT NULL DEFAULT '';  
					""");
		}
	}
}
