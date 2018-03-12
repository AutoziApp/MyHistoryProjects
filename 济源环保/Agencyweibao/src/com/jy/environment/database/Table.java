package com.jy.environment.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Table")
class Table {
	
	@XStreamAsAttribute
	@XStreamAlias("TableName")
	private String mTableName;
	
	@XStreamAsAttribute
	@XStreamAlias("ModelClassName")
	private String mModelClassName;
	
	@XStreamImplicit
	private List<Column> mColumnList;
	
	@XStreamImplicit
	private List<ForeignKey> mForeignKeyList;
	
	private Table() {
		
	}
	
	String getTableName() {
		return mTableName;
	}
	
	String getModelClassName() {
		return mModelClassName;
	}
	
	List<String> getAllColumnName() {
		List<String> _AllColumnName = new ArrayList<String>();
		
		for (Column _ModelColumn : mColumnList) {
			_AllColumnName.add(_ModelColumn.getColumnName());
		}
		
		return _AllColumnName;
		
	}
	
	Map<String, String> getAllColumnExtra() {
		Map<String, String> _AllColumnExtra = new HashMap<String, String>();
		
		for (Column _ModelColumn : mColumnList) {
			_AllColumnExtra.put(_ModelColumn.getColumnName(), _ModelColumn.getExtra());
		}
		
		return _AllColumnExtra;
	}
	
	Map<String, String> getFieldColumnMap() {
		Map<String, String> _FieldColumnMap = new HashMap<String, String>();
		
		for (Column _ModelColumn : mColumnList) {
			_FieldColumnMap.put(_ModelColumn.getFieldName(), _ModelColumn.getColumnName());
		}
		
		return _FieldColumnMap;

	}
	
	Map<String, String> getColumnFieldMap() {
		Map<String, String> _ColumnFieldMap = new HashMap<String, String>();
		
		for (Column _ModelColumn : mColumnList) {
			_ColumnFieldMap.put(_ModelColumn.getColumnName(), _ModelColumn.getFieldName());
		}
		
		return _ColumnFieldMap;
		
	}
	
	String getFieldNameByColumnName(String pColumnName) {
		String _FieldName = null;
		
		for (Column _ModelColumn : mColumnList) {
			if (pColumnName.equals(_ModelColumn.getColumnName())) {
				_FieldName = _ModelColumn.getFieldName();
				break;
			}
		}
		
		return _FieldName;
	}

	String getExtraByColumnName(String pColumnName) {
		String _Extra = null;
		
		for (Column _ModelColumn : mColumnList) {
			if (pColumnName.equals(_ModelColumn.getColumnName())) {
				_Extra = _ModelColumn.getExtra();
				break;
			}
		}
		
		return _Extra;
	}
	
	String getColumnNameByFieldName(String pFieldName) {
		String _ColumnName = null;
		
		for (Column _ModelColumn : mColumnList) {
			if (pFieldName.equals(_ModelColumn.getFieldName())) {
				_ColumnName = _ModelColumn.getColumnName();
				break;
			}
		}
		
		return _ColumnName;
	}
	
	String getCreatTableSql() {
		StringBuilder _Sql = new StringBuilder();
		
		_Sql.append("CREATE TABLE ");
		_Sql.append("\""+mTableName+"\""+" "+"("+"\n");
		
		for (Column _Column : mColumnList)
		{
			_Sql.append(_Column.toSql());
		}
		
		if (mForeignKeyList!=null && mForeignKeyList.size()>0)
		{
			for (ForeignKey _ForeignKey : mForeignKeyList)
			{
				_Sql.append(_ForeignKey.toSql());
			}
		}
		
		_Sql.append(")"+";");
		
//		删掉最后一个逗号
		_Sql.deleteCharAt(_Sql.lastIndexOf(","));
		
		return _Sql.toString();
	}
	
}
