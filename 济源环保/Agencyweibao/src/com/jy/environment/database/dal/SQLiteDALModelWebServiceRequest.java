package com.jy.environment.database.dal;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.jy.environment.database.model.ModelWebServiceRequest;
import com.jy.environment.util.MyLog;

public class SQLiteDALModelWebServiceRequest extends
	SQLiteDALBase<ModelWebServiceRequest> {

    public SQLiteDALModelWebServiceRequest(
	    Class<ModelWebServiceRequest> pModelClass) {
	super(pModelClass);
    }

    public ModelWebServiceRequest select(int pUrlHashCode,
	    int pUrlParamHashCode, int pBodyParamHashCode) {
	// String _editTime = getFieldColumnMap().get("editTime");
	String _UrlHashCodeColumnName = getFieldColumnMap().get("mUrlHashCode");
	String _UrlParamHashCodeColumnName = getFieldColumnMap().get(
		"mUrlParamHashCode");
	String _BodyParamHashCodeColumnName = getFieldColumnMap().get(
		"mBodyParamHashCode");
	List<ModelWebServiceRequest> _ModelList = select(_UrlHashCodeColumnName
		+ "=" + pUrlHashCode + " AND " + _UrlParamHashCodeColumnName
		+ "=" + pUrlParamHashCode + " AND "
		+ _BodyParamHashCodeColumnName + "=" + pBodyParamHashCode,
		null, null, null, null);

	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList.get(0);
	}
    }

    public ModelWebServiceRequest select(long edittime, int pUrlHashCode,
	    int pUrlParamHashCode, int pBodyParamHashCode) {
	// String _editTime = getFieldColumnMap().get("editTime");
	String _UrlHashCodeColumnName = getFieldColumnMap().get("mUrlHashCode");
	String _UrlParamHashCodeColumnName = getFieldColumnMap().get(
		"mUrlParamHashCode");
	String _BodyParamHashCodeColumnName = getFieldColumnMap().get(
		"mBodyParamHashCode");
	List<ModelWebServiceRequest> _ModelList = select(_UrlHashCodeColumnName
		+ "=" + pUrlHashCode + " AND " + _UrlParamHashCodeColumnName
		+ "=" + pUrlParamHashCode + " AND "
		+ _BodyParamHashCodeColumnName + "=" + pBodyParamHashCode,
		null, null, null, null);

	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList.get(0);
	}
    }

    @Override
    public boolean update(ModelWebServiceRequest pModelObject) {
	MyLog.i("pModelObject" + pModelObject);

	String _UrlHashCodeColumnName = getFieldColumnMap().get("mUrlHashCode");
	String _UrlParamHashCodeColumnName = getFieldColumnMap().get(
		"mUrlParamHashCode");
	String _BodyParamHashCodeColumnName = getFieldColumnMap().get(
		"mBodyParamHashCode");
	String _editTime = getFieldColumnMap().get("editTime");

	boolean _Result = false;

	beginTransaction();
	try {
	    int _UpdateRowNumbers = update(
		    pModelObject,
		    _UrlHashCodeColumnName + "="
			    + pModelObject.getUrlHashCode() + " AND "
			    + _UrlParamHashCodeColumnName + "="
			    + pModelObject.getUrlParamHashCode() + " AND "
			    + _BodyParamHashCodeColumnName + "="
			    + pModelObject.getBodyParamHashCode() + " AND "
			    + _editTime + "=" + pModelObject.getEditTime(),
		    null);
	    MyLog.i("_UpdateRowNumbers" + _UpdateRowNumbers);
	    if (_UpdateRowNumbers == 1) {
		setTransactionSuccessful();
		_Result = true;
	    }
	} finally {
	    endTransaction();
	}

	return _Result;
    }

    public boolean updateTime(ModelWebServiceRequest pModelObject) {
	String _UrlHashCodeColumnName = getFieldColumnMap().get("mUrlHashCode");
	String _UrlParamHashCodeColumnName = getFieldColumnMap().get(
		"mUrlParamHashCode");
	String _BodyParamHashCodeColumnName = getFieldColumnMap().get(
		"mBodyParamHashCode");
	// String _ResultColumnName = getFieldColumnMap().get("mResult");
	// String _editTimeColumnName = getFieldColumnMap().get("editTime");

	boolean _Result = false;

	beginTransaction();
	try {
	    int _UpdateRowNumbers = update(
		    pModelObject,
		    _UrlHashCodeColumnName + "="
			    + pModelObject.getUrlHashCode() + " AND "
			    + _UrlParamHashCodeColumnName + "="
			    + pModelObject.getUrlParamHashCode() + " AND "
			    + _BodyParamHashCodeColumnName + "="
			    + pModelObject.getBodyParamHashCode(), null);
	    // + " AND " + _ResultColumnName + "=" + pModelObject.getResult()
	    // + " AND " + _editTimeColumnName + "=" +
	    // pModelObject.getEditTime()
	    MyLog.i("_UpdateRowNumbers :" + _UpdateRowNumbers);
	    if (_UpdateRowNumbers == 1) {
		setTransactionSuccessful();
		_Result = true;
	    }
	} finally {
	    endTransaction();
	}

	return _Result;
    }

    @Override
    public boolean insertOrUpdateModel(ModelWebServiceRequest pModelObject) {
	ModelWebServiceRequest _ModelWebServiceGet = select(
		pModelObject.getEditTime(), pModelObject.getUrlHashCode(),
		pModelObject.getUrlParamHashCode(),
		pModelObject.getBodyParamHashCode());
	if (_ModelWebServiceGet == null) {
	    return insert(pModelObject);
	} else {
	    // return update(pModelObject);
	    return updateTime(pModelObject);
	}
    }

    @Override
    protected ContentValues toContentValues(ModelWebServiceRequest pModelObject) {
	Map<String, String> _FieldColumnMap = getFieldColumnMap();
	ContentValues _ContentValues = new ContentValues();

	for (String _FieldName : _FieldColumnMap.keySet()) {
	    if ("mUrlHashCode".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUrlHashCode());
	    } else if ("mUrlParamHashCode".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUrlParamHashCode());
	    } else if ("mBodyParamHashCode".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getBodyParamHashCode());
	    } else if ("mResult".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getResult());
	    } else if ("editTime".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getEditTime());
	    }
	}

	return _ContentValues;
    }

    @Override
    protected ContentValues updataToContentValues(
	    ModelWebServiceRequest pModelObject) {
	Map<String, String> _FieldColumnMap = getFieldColumnMap();
	ContentValues _ContentValues = new ContentValues();

	for (String _FieldName : _FieldColumnMap.keySet()) {
	    if ("mUrlHashCode".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUrlHashCode());
	    } else if ("mUrlParamHashCode".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUrlParamHashCode());
	    } else if ("mBodyParamHashCode".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getBodyParamHashCode());
	    } else if ("mResult".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getResult());
	    } else if ("editTime".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getEditTime());
	    }
	}

	return _ContentValues;
    }

    @Override
    protected ModelWebServiceRequest toModel(Cursor pCursor) {
	ModelWebServiceRequest _ModelObject = new ModelWebServiceRequest();

	_ModelObject.setID(pCursor.getLong(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("mID"))));
	_ModelObject
		.setUrlHashCode(pCursor.getInt(pCursor
			.getColumnIndexOrThrow(getFieldColumnMap().get(
				"mUrlHashCode"))));
	_ModelObject.setUrlParamHashCode(pCursor.getInt(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get(
			"mUrlParamHashCode"))));
	_ModelObject.setBodyParamHashCode(pCursor.getInt(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get(
			"mBodyParamHashCode"))));
	_ModelObject.setResult(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("mResult"))));

	_ModelObject.setEditTime(pCursor.getInt(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("editTime"))));

	return _ModelObject;
    }

}
