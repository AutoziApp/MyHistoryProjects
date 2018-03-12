package com.jy.environment.database.dal;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.jy.environment.database.model.ModelNoiseHistory;
import com.jy.environment.util.MyLog;

public class SQLiteDALModelNoiseHistory extends SQLiteDALBase<ModelNoiseHistory>{

    public SQLiteDALModelNoiseHistory(Class<ModelNoiseHistory> pModelClass) {
	super(pModelClass);
    }

    /**
     * 根据用户查询第一条
     * 
     * @param userName
     * @param userId
     * @return
     */
    public ModelNoiseHistory select(String userName, String userId) {
	String _UserNameColumnName = getFieldColumnMap().get("userName");
	String _UserIdColumnName = getFieldColumnMap().get("userId");
	List<ModelNoiseHistory> _ModelList = select(_UserNameColumnName + "="
		+ userName + " AND " + _UserIdColumnName + "=" + userId, null,
		null, null, null);
	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList.get(0);
	}
    }

    /**
     * 根据用户查询所有用户下保存的数据
     * 
     * @param userName
     * @param userId
     * @return
     */
    public List<ModelNoiseHistory> selectList(String userName, String userId) {
	String _UserNameColumnName = getFieldColumnMap().get("userName");
	String _UserIdColumnName = getFieldColumnMap().get("userId");
	List<ModelNoiseHistory> _ModelList = select(_UserNameColumnName + "="
		+ userName + " AND " + _UserIdColumnName + "=" + userId, null,
		null, null, null);
	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList;
	}
    }
    /**
     * 读取全部数据
     * 
     * @param userName
     * @param userId
     * @return
     */
    public List<ModelNoiseHistory> selectAllHistoryMuch() {
	List<ModelNoiseHistory> _ModelList = select();
	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList;
	}
    }
    public List<ModelNoiseHistory> selectAllHistoryupload() {
    	List<ModelNoiseHistory> _ModelList = select_noupload();
    	MyLog.i(">>>>>>>size"+_ModelList.size());
    	if (_ModelList.size() <= 0) {
    		return null;
    	} else {
    		return _ModelList;
    	}
    }
    /**
     * 读取前20条
     * 
     * @param userName
     * @param userId
     * @return
     */
    public List<ModelNoiseHistory> selectAllHistory() {
	List<ModelNoiseHistory> _ModelList = selectL();
	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList;
	}
    }
    /**
     * 读取前20条
     * 
     * @param userName
     * @param userId
     * @return
     */
    public List<ModelNoiseHistory> selectAllHistoryBypage(int i) {
    	List<ModelNoiseHistory> _ModelList = selectBypage(i);
    	if (_ModelList.size() <= 0) {
    		return null;
    	} else {
    		return _ModelList;
    	}
    }
    /**
     * 查询出表中数值的最大值和最小值
     * 
     */
    public List<ModelNoiseHistory> selectMaxMin(String nowtime,String seventime)
    {
		List<ModelNoiseHistory> _ModelList = selectM(nowtime,seventime);
		if(_ModelList.size() <=0 ){
			return null;
		}else{
			return _ModelList;
		}
    	
    }
    /**
     * 根据时间查询，插入时防止重复
     */
    public ModelNoiseHistory select(String time) {
	String _timeColumnName = getFieldColumnMap().get("time");
	List<ModelNoiseHistory> _ModelList = select(_timeColumnName + "="
		+"'"+ time+"'", null, null, null, null);
	if (_ModelList.size() <= 0) {
	    return null;
	} else {
	    return _ModelList.get(0);
	}
    }

    @Override
    public boolean update(ModelNoiseHistory pModelObject) {
	MyLog.i("pModelObject" + pModelObject);
	String _timeColumnName = getFieldColumnMap().get("time");
	String _phoneNumberColumnName = getFieldColumnMap().get("phoneNumber");
	String _locationColumnName = getFieldColumnMap().get("location");
	String _imeiColumnName = getFieldColumnMap().get("imei");
	String _mResultColumnName = getFieldColumnMap().get("mResult");
	String _userNameColumnName = getFieldColumnMap().get("userName");
	String _userIdColumnName = getFieldColumnMap().get("userId");
	String _latitudeColumnName = getFieldColumnMap().get("latitude");
	String _longitudeColumnName = getFieldColumnMap().get("longitude");
	String _isuploadColumnName = getFieldColumnMap().get("isupload");
	boolean _Result = false;

	beginTransaction();
	try {
	    int _UpdateRowNumbers = update(
		    pModelObject,
		    _timeColumnName + "=" + pModelObject.getTime() + " AND "
			    + _phoneNumberColumnName + "="
			    + pModelObject.getPhoneNumber() + " AND "
			    + _locationColumnName + "="
			    + pModelObject.getLocation() + " AND "
			    + _imeiColumnName + "="
			    + pModelObject.getLocation() + " AND "
			    + _mResultColumnName + "="
			    + pModelObject.getmResult() + " AND "
			    + _imeiColumnName + "="
			    + pModelObject.getLocation() + " AND "
			    + _userNameColumnName + "="
			    + pModelObject.getUserName() + " AND "
			    + _userIdColumnName + "="
			    + pModelObject.getUserId() + "AND"
			    + _latitudeColumnName + "="
			    + pModelObject.getLatitude() + "AND"
			    + _longitudeColumnName + "="
			    + pModelObject.getLongitude() + "AND"
			    + _isuploadColumnName + "="
			    + pModelObject.getIsupload(), null);
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

    @Override
    public boolean insertOrUpdateModel(ModelNoiseHistory pModelObject) {
	ModelNoiseHistory _ModelWebServiceGet = select(pModelObject.getTime());
	if (_ModelWebServiceGet == null) {
	    return insert(pModelObject);
	} else {
	    return update(pModelObject);
	}
    }

    @Override
    protected ContentValues toContentValues(ModelNoiseHistory pModelObject) {
	Map<String, String> _FieldColumnMap = getFieldColumnMap();
	ContentValues _ContentValues = new ContentValues();

	for (String _FieldName : _FieldColumnMap.keySet()) {
	    if ("time".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getTime());
	    } else if ("phoneNumber".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getPhoneNumber());
	    } else if ("location".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getLocation());
	    } else if ("imei".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getImei());
	    } else if ("mResult".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getmResult());
	    } else if ("userName".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUserName());
	    } else if ("userId".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUserId());
	    }else if ("latitude".equals(_FieldName)) {
			_ContentValues.put(_FieldColumnMap.get(_FieldName),
					pModelObject.getLatitude());
		} else if ("longitude".equals(_FieldName)) {
			_ContentValues.put(_FieldColumnMap.get(_FieldName),
					pModelObject.getLongitude());
		}else if ("isupload".equals(_FieldName)) {
			_ContentValues.put(_FieldColumnMap.get(_FieldName),
					pModelObject.getIsupload());
		}
	}

	return _ContentValues;
    }

    @Override
    protected ContentValues updataToContentValues(ModelNoiseHistory pModelObject) {
	Map<String, String> _FieldColumnMap = getFieldColumnMap();
	ContentValues _ContentValues = new ContentValues();

	for (String _FieldName : _FieldColumnMap.keySet()) {
	    if ("time".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getTime());
	    } else if ("phoneNumber".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getPhoneNumber());
	    } else if ("location".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getLocation());
	    } else if ("imei".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getImei());
	    } else if ("mResult".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getmResult());
	    } else if ("userName".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUserName());
	    } else if ("userId".equals(_FieldName)) {
		_ContentValues.put(_FieldColumnMap.get(_FieldName),
			pModelObject.getUserId());
	    } else if ("latitude".equals(_FieldName)) {
			_ContentValues.put(_FieldColumnMap.get(_FieldName),
					pModelObject.getLatitude());
		} else if ("longitude".equals(_FieldName)) {
			_ContentValues.put(_FieldColumnMap.get(_FieldName),
					pModelObject.getLongitude());
		} else if ("isupload".equals(_FieldName)) {
			_ContentValues.put(_FieldColumnMap.get(_FieldName),
					pModelObject.getIsupload());
		}
	}

	return _ContentValues;
    }

    @Override
    protected ModelNoiseHistory toModel(Cursor pCursor) {
	ModelNoiseHistory _ModelObject = new ModelNoiseHistory();

	_ModelObject.setmID(pCursor.getLong(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("mID"))));
	_ModelObject.setTime(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("time"))));
	_ModelObject
		.setPhoneNumber(pCursor.getInt(pCursor
			.getColumnIndexOrThrow(getFieldColumnMap().get(
				"phoneNumber"))));
	_ModelObject.setLocation(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("location"))));
	_ModelObject.setImei(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("imei"))));
	_ModelObject.setmResult(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("mResult"))));
	_ModelObject.setUserName(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("userName"))));
	_ModelObject.setUserId(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("userId"))));
	_ModelObject.setLatitude(pCursor.getString(pCursor
		.getColumnIndexOrThrow(getFieldColumnMap().get("latitude"))));
	_ModelObject.setLongitude(pCursor.getString(pCursor
			.getColumnIndexOrThrow(getFieldColumnMap().get("longitude"))));
	_ModelObject.setIsupload(pCursor.getString(pCursor
			.getColumnIndexOrThrow(getFieldColumnMap().get("isupload"))));
	

	return _ModelObject;
    }

}
