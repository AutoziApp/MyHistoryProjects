package com.jy.environment.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.jy.environment.controls.WeiBaoApplication;
import com.thoughtworks.xstream.XStream;

import android.content.Context;

public class DatabaseConfig
{
    private static Database mDatabase;
    
    static {
        Context _ContextApp = WeiBaoApplication.getInstance();

        XStream _XStream = new XStream();
        _XStream.processAnnotations(Database.class);
        try {
        	InputStream _InputStream = _ContextApp.getAssets().open("database.xml");
            mDatabase = (Database) _XStream.fromXML(_InputStream);
            _InputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DatabaseConfig()
    {
    }

    public static String getDatabaseName()
    {
        return mDatabase.getDatabaseName();
    }

    public static int getVersion()
    {
        return mDatabase.getVersion();
    }

    public static List<String> getCreatDatabaseSql()
    {
        return mDatabase.getCreatDatabaseSql();
    }

    public static List<String> getUpgrateDatabaseSql(int pOldVersion, int pNewVersion)
    {
        return mDatabase.getUpgrateDatabaseSql(pOldVersion, pNewVersion);
    }

    public static String getTableName(Class<?> pModelClass)
    {
        return mDatabase.getTableName(pModelClass.getName());
    }

    public static List<String> getAllColumnName(Class<?> pModelClass)
    {
        String _TableName = getTableName(pModelClass);
        return mDatabase.getAllColumnName(_TableName);
    }

    public static Map<String, String> getFieldColumnMap(Class<?> pModelClass)
    {
        String _TableName = getTableName(pModelClass);
        return mDatabase.getFieldColumnMap(_TableName);
    }

    public static Map<String, String> getColumnFieldMap(Class<?> pModelClass)
    {
        String _TableName = getTableName(pModelClass);
        return mDatabase.getColumnFieldMap(_TableName);
    }

}