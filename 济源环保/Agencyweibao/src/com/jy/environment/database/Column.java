package com.jy.environment.database;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Column")
class Column {
	
	@XStreamAsAttribute
	@XStreamAlias("ColumnName")
	private String mColumnName;
	
	@XStreamAsAttribute
	@XStreamAlias("Extra")
	private String mExtra;
	
	@XStreamAsAttribute
	@XStreamAlias("FieldName")
	private String mFieldName;
	
	private Column() {
		
	}
	
	String getColumnName() {
		return mColumnName;
	}
	
	String getExtra() {
		return mExtra;
	}
	
	String getFieldName() {
		return mFieldName;
	}
	
	String toSql() {
		return "\""+mColumnName+"\""+" "+mExtra+","+"\n";
	}

	
	
}
