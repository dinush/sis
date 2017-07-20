package org.apache.sis.internal.metadata.sql;

import android.database.sqlite.SQLiteDatabase;
import org.apache.sis.setup.InstallationResources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Database installer service. This is a map to EPSGInstaller class methods
 * in sis-referencing module.
 *
 * @author Sisinda Dinusha
 * @version 0.8
 * @since 0.8
 */
public interface Installer {

    public void setSchema(final String schema) throws SQLException, IOException;

    public void setDatabase(final SQLiteDatabase database);

    public void run(InstallationResources scriptProvider, final Locale locale) throws SQLException, IOException;
}
