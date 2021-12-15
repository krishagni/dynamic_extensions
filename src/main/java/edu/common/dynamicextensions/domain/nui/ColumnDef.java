
package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;

public class ColumnDef implements Serializable {
	private static final long serialVersionUID = -4514209229731927120L;

	private String columnName;
	
	private String dbType;

	private String refTable;

	private String refColumn;
	
	public static ColumnDef get(String columnName, String dbType) {
		ColumnDef columnDef = new ColumnDef();
		columnDef.columnName = columnName;
		columnDef.dbType = dbType;
		return columnDef;		
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getRefTable() {
		return refTable;
	}

	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}

	public String getRefColumn() {
		return refColumn;
	}

	public void setRefColumn(String refColumn) {
		this.refColumn = refColumn;
	}
}
