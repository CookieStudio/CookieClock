package com.tomtom.cookieclock.repository;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by saramakm on 14-10-15.
 */
public class GammerRepoHelper extends OrmLiteSqliteOpenHelper implements GammerResultRepo{

    private static final String DATABASE_NAME = "gammer.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "GammerRepoHelper";

    public GammerRepoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate");
            createDB(connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    private void createDB(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, GammerResultDAO.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade");
            deleteOldDatabase(connectionSource);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }
    void deleteOldDatabase(ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, GammerResultDAO.class, true);
    }

    public List<GammerResultDAO> getResults(){
        try {
            return getDao(GammerResultDAO.class).queryBuilder().limit(100l).orderBy("timeinms", false).query();
        } catch (SQLException e) {
            Log.e(TAG, "Error when get gammer");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void clear(){
        try {
            getDao(GammerResultDAO.class).deleteBuilder().reset();
        } catch (SQLException e) {
            Log.e(TAG, "Error when get gammer");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void saveGammerResult(GammerResultDAO gammerResultDAO) {
        try {
            gammerResultDAO.setUuid(UUID.randomUUID().toString());
            getDao(GammerResultDAO.class).createOrUpdate(gammerResultDAO);
        } catch (SQLException e) {
            Log.e(TAG, "Error when save gammer");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteGammer(GammerResultDAO gammerResultDAO) {
        try {
            String uuid = gammerResultDAO.getUuid();
            getDao(GammerResultDAO.class).delete(gammerResultDAO);
        } catch (SQLException e) {
            Log.e(TAG, "Error when delete gammer");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
