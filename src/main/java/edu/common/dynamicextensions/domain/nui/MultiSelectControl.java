package edu.common.dynamicextensions.domain.nui;

import java.util.List;

public interface MultiSelectControl {
	public String getTableName();
	
	public void setTableName(String tableName);
	
	public abstract List<ColumnDef> getColumnDefs();
	
	public abstract <T> T fromString(String value);	
	
	public String getParentKey();
	
	public void setParentKey(String parentKey);
	
	public String getForeignKey();
	
	public void setForeignKey(String foreignKey);	
}
