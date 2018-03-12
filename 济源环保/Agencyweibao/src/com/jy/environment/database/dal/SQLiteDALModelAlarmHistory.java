package com.jy.environment.database.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.jy.environment.database.model.ModelAlarmHistory;

public class SQLiteDALModelAlarmHistory extends SQLiteDALBase<ModelAlarmHistory>{

    public SQLiteDALModelAlarmHistory(Class<ModelAlarmHistory> pAlarmClass) {
    	super(pAlarmClass);
    }
    
    /**
     * 统计城市一天内的数据
     * @param city
     * @return
     */
    public int countModel(String city) {
    	List<ModelAlarmHistory> _ModelList = new ArrayList<ModelAlarmHistory>();
    	try {
    		_ModelList = selectByCity(city);
		} catch (Exception e) {
			_ModelList = new ArrayList<ModelAlarmHistory>();
		}
		return _ModelList.size();
    }
    
    /**
     * 查询城市一天内的数据
     * @param city
     * @return
     */
    public List<ModelAlarmHistory> selectByCity(String city) {
		String _timeColumnName = getFieldColumnMap().get("town");
		List<ModelAlarmHistory> _ModelList=select(_timeColumnName+"="
				+"'"+city+"'", null, null, null, null);
		return _ModelList;
    }
    
    public boolean insertNewModel(ModelAlarmHistory pModelObject) {
    	ModelAlarmHistory _ModelWebServiceGet = select(pModelObject.getTime());
		if (_ModelWebServiceGet == null) {
		    return insert(pModelObject);
		}
		return false;
    }
    
    /**
     * 根据时间查询，插入时防止重复
     */
    public ModelAlarmHistory select(String time) {
		String _timeColumnName = getFieldColumnMap().get("time");
		List<ModelAlarmHistory> _ModelList = select(_timeColumnName + "="
			+"'"+ time+"'", null, null, null, null);
		if (_ModelList.size() <= 0) {
		    return null;
		} else {
		    return _ModelList.get(0);
		}
    }
    
    /**
     * 删除城市一天外的数据
     * @param time
     * @return
     */
    public int deleteByTime(String time){
    	String where = "time<?";
    	String[] args = new String[]{time};
    	return delete(where, args);
    }

	@Override
	protected ContentValues toContentValues(ModelAlarmHistory pModelObject) {
		Map<String, String> _FieldColumnMap = getFieldColumnMap();
		ContentValues _ContentValues = new ContentValues();
		for (String _FieldName : _FieldColumnMap.keySet()) {
		    if ("province".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getProvince());
		    } else if ("town".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getTown());
		    } else if ("title".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getTitle());
		    } else if ("message".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getMessage());
		    } else if ("time".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getTime());
		    } else if ("url".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getUrl());
		    } else if ("alarm".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getAlarm());
		    }
		}
		return _ContentValues;
	}

	@Override
	protected ContentValues updataToContentValues(ModelAlarmHistory pModelObject) {
		Map<String, String> _FieldColumnMap = getFieldColumnMap();
		ContentValues _ContentValues = new ContentValues();
		for (String _FieldName : _FieldColumnMap.keySet()) {
		    if ("province".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getProvince());
		    } else if ("town".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getTown());
		    } else if ("title".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getTitle());
		    } else if ("message".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getMessage());
		    } else if ("time".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getTime());
		    } else if ("url".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getUrl());
		    } else if ("alarm".equals(_FieldName)) {
		    	_ContentValues.put(_FieldColumnMap.get(_FieldName),
		    			pModelObject.getAlarm());
		    }
		}
		return _ContentValues;
	}

	@Override
	protected ModelAlarmHistory toModel(Cursor pCursor) {
		ModelAlarmHistory _ModelObject = new ModelAlarmHistory();
		String province = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("province")));
		String town = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("town")));
		String title = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("title")));
		String message = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("message")));
		String time = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("time")));
		String url = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("url")));
		String alarm = pCursor.getString(pCursor.getColumnIndexOrThrow(getFieldColumnMap().get("alarm")));
		_ModelObject.setProvince(province);
		_ModelObject.setTown(town);
		_ModelObject.setTitle(title);
		_ModelObject.setMessage(message);
		_ModelObject.setTime(time);
		_ModelObject.setUrl(url);
		_ModelObject.setAlarm(alarm);
		return _ModelObject;
	}
}
