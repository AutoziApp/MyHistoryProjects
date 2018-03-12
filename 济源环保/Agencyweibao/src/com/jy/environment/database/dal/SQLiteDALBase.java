package com.jy.environment.database.dal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jy.environment.database.DatabaseConfig;
import com.jy.environment.database.SQLiteOpenAssistant;
import com.jy.environment.util.MyLog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteDALBase<M>
{
    private Class<M>            mModelClass;
    private SQLiteOpenAssistant mSQLiteOpenAssistant;
    private SQLiteDatabase      mSQLiteDatabase;
    private String              mTableName;
    private String[]            mAllColumnName;
    private Map<String, String> mFieldColumnMap;
    private Map<String, String> mColumnFieldMap;

    public SQLiteDALBase(Class<M> pModelClass)
    {
        mModelClass = pModelClass;
        mSQLiteOpenAssistant = SQLiteOpenAssistant.getInstance();
        mSQLiteDatabase = mSQLiteOpenAssistant.getReadableDatabase();

        mTableName = DatabaseConfig.getTableName(mModelClass);
        mAllColumnName = DatabaseConfig.getAllColumnName(mModelClass).toArray(new String[1]);
        mFieldColumnMap = DatabaseConfig.getFieldColumnMap(mModelClass);
        mColumnFieldMap = DatabaseConfig.getColumnFieldMap(mModelClass);
    }

    protected SQLiteOpenAssistant getSQLiteOpenAssistant()
    {
        return mSQLiteOpenAssistant;
    }

    protected SQLiteDatabase getSqLiteDatabase()
    {
        if (mSQLiteDatabase == null) {
            mSQLiteDatabase = mSQLiteOpenAssistant.getReadableDatabase();
        }
        return mSQLiteDatabase;
    }

    protected String getTableName()
    {
        return mTableName;
    }

    protected String[] getAllColumnName()
    {
        return mAllColumnName;
    }

    protected String getPrimaryColumnName()
    {
        return mAllColumnName[0];
    }

    protected long getPrimaryFieldValue(M pModelObject)
    {
        try {
            Field _PrimaryField = mModelClass.getDeclaredField(mColumnFieldMap.get(getPrimaryColumnName()));
            _PrimaryField.setAccessible(true);

            return _PrimaryField.getLong(pModelObject);

        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    protected void setPrimaryFieldValue(M pModelObject, long pValue)
    {
        try {
            Field _PrimaryField = mModelClass.getDeclaredField(mColumnFieldMap.get(getPrimaryColumnName()));
            _PrimaryField.setAccessible(true);
            _PrimaryField.setLong(pModelObject, pValue);

        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    protected Map<String, String> getFieldColumnMap()
    {
        return mFieldColumnMap;
    }

    protected Map<String, String> getColumnFieldMap()
    {
        return mColumnFieldMap;
    }

    public List<M> select()
    {
        return select(null, null, null, null, null);
    }
    public List<M> select_noupload()
    {
    	return select("isupload = '0'", null, "time", null, null);
    }
    public List<M> selectL()
    {
    	return select(null, null, "time", null, "time desc limit 20");
    }
    public List<M> selectBypage(int i)
    {
    	return select(null, null, "time", null, "time desc limit "+20*i+",20");
    }
    public List<M> selectM(String nowtime,String seventime)
    {
    	return select("time between '"+seventime+"' and '"+nowtime+"'", null, null, null, "Result");
    }
    public List<M> selectOrderBy(String _ResultOrderBy)
    {
    	return select(null, null, "time", null, _ResultOrderBy);
    }
    public M select(long pID)
    {
        String _PrimaryKeyName = getPrimaryColumnName();
        List<M> _ModelList = select(_PrimaryKeyName + "=" + pID, null, null, null, null);

        if (_ModelList.size() <= 0) {
            return null;
        } else {
            return _ModelList.get(0);
        }
    }
	public boolean insert(M pModelObject)
	{
	    long _RowID = mSQLiteDatabase.insert(mTableName, null, toContentValues(pModelObject));
	
	    MyLog.i("_RowID" + _RowID);
	    if (_RowID != -1) {
	        setPrimaryFieldValue(pModelObject, _RowID);
	        return true;
	    } else {
	        return false;
	    }
	
	}

	public List<Boolean> insert(List<M> pModelObjectList)
	{
	    List<Boolean> _ResultList = new ArrayList<Boolean>();
	
	    for (M _ModelObject : pModelObjectList) {
	    	_ResultList.add(insert(_ModelObject));
	    }
	
	    return _ResultList;
	}

    public boolean update(M pModelObject)
    {
        String _PrimaryKeyName = getPrimaryColumnName();
        long _PrimaryKeyValue = getPrimaryFieldValue(pModelObject);

        boolean _Result = false;
        
        beginTransaction();
        try {
            int _UpdateRowNumbers = update(pModelObject, _PrimaryKeyName + "=" + _PrimaryKeyValue, null);
            if (_UpdateRowNumbers == 1) {
                setTransactionSuccessful();
                _Result = true;
            }
        } finally {
            endTransaction();
        }
        
        return _Result;
    }

    public boolean insertOrUpdateModel(M pModelObject)
    {
        if (getPrimaryFieldValue(pModelObject) <= 0) {
            return insert(pModelObject);
        } else {
            return update(pModelObject);
        }
    }

    public boolean deleteModel(long pID)
    {
        String _PrimaryKeyName = getPrimaryColumnName();
        String _Selection = _PrimaryKeyName + "=" + pID;

        boolean _Result = false;
        
        beginTransaction();
        try {
            int _DeleteRowNumbers = delete(_Selection, null);
            if (_DeleteRowNumbers == 1) {
                setTransactionSuccessful();
                _Result = true;
            }
        } finally {
            endTransaction();
        }
        
        return _Result;
    }

    public boolean deleteModel(M pModelObject)
    {
    	boolean _Reslut = deleteModel(getPrimaryFieldValue(pModelObject));
    	
    	if (_Reslut) {
    		setPrimaryFieldValue(pModelObject, -1);
		}
    	
        return _Reslut;
    }
    
    public void clearTable()
    {
    	MyLog.i(">>>>>>>>>>mTab"+mTableName);
    	mSQLiteDatabase.delete(mTableName, null, null);
    }
    
	protected List<M> select(String pSelection, String[] pSelectionArgs, String pGroupBy, String pHaving, String pOrderBy)
	{
	    List<M> _ModelObjectList = new ArrayList<M>();
	    Cursor _Cursor = mSQLiteDatabase.query(mTableName, mAllColumnName, pSelection, pSelectionArgs, pGroupBy, pHaving, pOrderBy);
	    MyLog.i("===============Cursor"+_Cursor);
	    for (; _Cursor.moveToNext();) {
	        _ModelObjectList.add(toModel(_Cursor));
	    }
	
	    _Cursor.close();
	
	    return _ModelObjectList;
	}

	protected int getCount(String pSelection, String[] pSelectionArgs)
	{
	    Cursor _Cursor = this.getSqLiteDatabase().query(mTableName, mAllColumnName, pSelection, pSelectionArgs, null, null, null);
	    int _Count = _Cursor.getCount();
	    _Cursor.close();
	    return _Count;
	}

	protected int update(M pModelObject, String pSelection, String[] pSelectionArgs)
	{
	    return mSQLiteDatabase.update(mTableName, updataToContentValues(pModelObject), pSelection, pSelectionArgs);
	    
	}
//	protected int update(M pModelObject, String pSelection, String[] pSelectionArgs)
//	{
////	    return mSQLiteDatabase.update(mTableName, toContentValues(pModelObject), pSelection, pSelectionArgs);
//	    MyLog.i("mTableName :" + mTableName);
//	    MyLog.i("pSelection :" + pSelection);
//	    MyLog.i("pSelectionArgs :" + pSelectionArgs);
//	    return mSQLiteDatabase.update(mTableName, updataToContentValues(pModelObject), pSelection, pSelectionArgs);
//	    
//	}

    protected int delete(String pSelection, String[] pSelectionArgs)
    {
        return this.getSqLiteDatabase().delete(mTableName, pSelection, pSelectionArgs);
    }

    public boolean execSQL(String pSQL)
    {
        boolean _Result = false;

        try {
            mSQLiteDatabase.execSQL(pSQL);
            _Result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _Result;
    }

    /**
     * 开始数据库事务。
     * Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or
     * rolled back. The changes will be rolled back if any transaction is ended without being marked as clean (by calling setTransactionSuccessful). Otherwise they will be committed.
     * 
     * <p>
     * Here is the standard idiom for transactions:
     * 
     * <pre>
     *   DAL.beginTransaction();
     *   try {
     *     ...
     *     DAL.setTransactionSuccessful();
     *   } finally {
     *     DAL.endTransaction();
     *   }
     * </pre>
     */
    protected void beginTransaction()
    {
        mSQLiteDatabase.beginTransaction();
    }

    protected void setTransactionSuccessful()
    {
        mSQLiteDatabase.setTransactionSuccessful();
    }

    protected void endTransaction()
    {
        mSQLiteDatabase.endTransaction();
    }

    protected abstract ContentValues toContentValues(M pModelObject);
    
    protected abstract ContentValues updataToContentValues(M pModelObject);

    protected abstract M toModel(Cursor pCursor);

}
