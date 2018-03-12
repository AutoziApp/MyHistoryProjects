package com.jy.environment.database;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ForeignKey")
class ForeignKey {
	
	@XStreamAsAttribute
	@XStreamAlias("Column")
	private String mColumn;
	
	@XStreamAsAttribute
	@XStreamAlias("ReferencesTable")
	private String mReferencesTable;
	
	@XStreamAsAttribute
	@XStreamAlias("ReferencesColumn")
	private String mReferencesColumn;
	
	@XStreamAsAttribute
	@XStreamAlias("OnDelete")
	private String mOnDelete;
	
	@XStreamAsAttribute
	@XStreamAlias("OnUpdate")
	private String mOnUpdate;
	
	private ForeignKey() {
		
	}
	
	String getColumn() {
		return mColumn;
	}
	
	String getReferencesTable() {
		return mReferencesTable;
	}
	
	String getReferencesColumn() {
		return mReferencesColumn;
	}
	
	String getOnDelete() {
		return mOnDelete;
	}

	String getOnUpdate() {
		return mOnUpdate;
	}
	
	String toSql() {
		return "FOREIGN KEY (\""+mColumn+"\")"+" REFERENCES \""+mReferencesTable+"\" (\""+mReferencesColumn+"\") ON DELETE "+mOnDelete+" ON UPDATE "+mOnUpdate+","+"\n";
	}

	
	
}
