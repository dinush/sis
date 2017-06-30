package org.apache.sis.internal.metadata.sql;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import org.apache.sis.setup.InstallationResources;

import java.io.IOException;
import java.util.Locale;

/**
 * Database installer service. Created to map to EPSGInstaller class
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
