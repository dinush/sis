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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.apache.sis.internal.system.DefaultFactories;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * SQLite database connection initialization class.
 *
 * @author  Sisinda Dinusha
 * @version 0.8
 * @since   0.8
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Creates the database for the first time. This method uses classes
     * from sis-referencing module.
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * EPSG dataset installer
         */
        Installer installer;
        sqLiteDatabase.beginTransaction();
        try {
            installer = getInstaller();
            installer.setDatabase(sqLiteDatabase);
            installer.setSchema(null);
            installer.run(null, Locale.getDefault());
            sqLiteDatabase.setTransactionSuccessful();
        } catch (NoSuchElementException | IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /**
         * Metadata installer
         */
        org.apache.sis.metadata.sql.Installer metaInstaller = new org.apache.sis.metadata.sql.Installer(sqLiteDatabase);
        try {
            metaInstaller.run();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.enableWriteAheadLogging();
    }

    /**
     * Gets database installer which is in sis-referencing module.
     * This method assumes only one provider is available.
     * @return
     * @throws NoSuchElementException
     */
    private Installer getInstaller() throws NoSuchElementException {
        ServiceLoader<Installer> installers = DefaultFactories.createServiceLoader(Installer.class);
        return installers.iterator().next();
    }
}
