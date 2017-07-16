/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.internal.metadata.sql;

import java.nio.file.Files;
import java.nio.file.Path;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.apache.sis.util.Debug;
import org.apache.sis.internal.system.DataDirectory;

import static org.junit.Assume.*;


/**
 * Utility methods for creating temporary databases with Derby.
 * The databases are in-memory only.
 *
 * <div class="section">Inspecting the database content in a debugger</div>
 * Make sure that the classpath contains the {@code derbynet.jar} file in addition to {@code derby.jar}.
 * Then, specify the following options to the JVM (replace the 1527 port number by something else if needed):
 *
 * {@preformat text
 *   -Dderby.drda.startNetworkServer=true
 *   -Dderby.drda.portNumber=1527
 * }
 *
 * When the application is running, one can verify that the Derby server is listening:
 *
 * {@preformat text
 *   netstat -an | grep "1527"
 * }
 *
 * To connect to the in-memory database, use the {@code "jdbc:derby://localhost:1527/dbname"} URL
 * (replace {@code "dbname"} by the actual database name.
 *
 * <p><b>References:</b>
 * <ul>
 *   <li><a href="https://db.apache.org/derby/docs/10.2/adminguide/radminembeddedserverex.html">Embedded server example</a></li>
 * </ul>
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 0.7
 * @since   0.7
 * @module
 */
public final strictfp class TestDatabase {
    /**
     * Data source to an alternative database to use for testing purpose.
     * If {@code null}, an in-memory Derby or JavaDB database will be used.
     *
     * This field is occasionally set to a non-null value (e.g. a connection to a PostgreSQL database) only for
     * debugging purpose. In such case, it is developer responsibility to ensure that the appropriate driver is
     * registered in his development environment (we may not declare them in the {@code pom.xml} file).
     */
    @Debug
    private static SQLiteDatabase TEST_DATABASE = null;

    /**
     * Do not allow (for now) instantiation of this class.
     */
    private TestDatabase() {
    }

    /**
     * Returns the path to the directory of the given name in {@code $SIS_DATA/Databases}.
     * If the directory is not found, then the test will be interrupted by an {@code org.junit.Assume} statement.
     *
     * @param  name  the name of the sub-directory.
     * @return the path to the given sub-directory.
     */
    public static Path directory(final String name) {
        Path dir = DataDirectory.DATABASES.getDirectory();
        assumeNotNull("$SIS_DATA/Databases directory not found.", dir);
        dir = dir.resolve(name);
        assumeTrue(Files.isDirectory(dir));
        return dir;
    }

    /**
     * Creates a SQLite database in memory. If no Derby or JavaDB driver is not found,
     * then the test will be interrupted by an {@code org.junit.Assume} statement.
     *
     * @param  context  the {@link Context} of the app
     * @return the data source.
     * @throws Exception if an error occurred while creating the database.
     */
    public static SQLiteDatabase create(final Context context) throws Exception {
        return new Database(context).getWritableDatabase();
    }

    /**
     * Drops an in-memory Derby database after usage.
     *
     * @param  db  the data source created by {@link #create(Context)}.
     * @throws Exception if an error occurred while dropping the database.
     */
    public static void drop(final SQLiteDatabase db) throws Exception {
        db.close();
    }

    /**
     * Test database. This database will be a on-memory database.
     */
    private static class Database extends SQLiteOpenHelper {

        public Database(Context context) {
            super(context, null, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
