
package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class MultiSelectListBox extends ListBox implements MultiSelectControl, Serializable {
	private static final long serialVersionUID = 2117234602017344729L;

	private String tableName;
	
	private String parentKeyColumn  = "IDENTIFIER";
	
	private String foreignKeyColumn = "RECORD_ID";
	
	public MultiSelectListBox() {
		setDbColumnName("VALUE");
	}
	
	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();
		columnDefs.add(ColumnDef.get(getDbColumnName(), getDbType()));
		columnDefs.add(ColumnDef.get(foreignKeyColumn, ColumnTypeHelper.getIntegerColType()));

		return columnDefs;
	}
	
	@Override
	public String getParentKey() {
		return parentKeyColumn;
	}

	@Override
	public void setParentKey(String parentKeyColumn) {
		if (parentKeyColumn == null) {
			parentKeyColumn = "IDENTIFIER";
		}
		
		this.parentKeyColumn = parentKeyColumn;		
	}
	
	@Override
	public String getForeignKey() {
		return foreignKeyColumn;
	}

	@Override
	public void setForeignKey(String foreignKeyColumn) {
		if (foreignKeyColumn == null) {
			foreignKeyColumn = "RECORD_ID";
		}
		
		this.foreignKeyColumn = foreignKeyColumn;		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result	+ ((parentKeyColumn == null) ? 0 : parentKeyColumn.hashCode());
		result = prime * result	+ ((foreignKeyColumn == null) ? 0 : foreignKeyColumn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		MultiSelectListBox other = (MultiSelectListBox) obj;
		if (!StringUtils.equals(tableName, other.tableName) ||
			!StringUtils.equals(parentKeyColumn, other.parentKeyColumn) ||
			!StringUtils.equals(foreignKeyColumn, other.foreignKeyColumn)) {
			return false;
		}

		return true;
	}

	@Override
	public String getCtrlType() {
		return "multiSelectListbox";
	}
	
	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
	}
	
	@Override
	public ValidationStatus validate(Object value) {
		return validateMultiple(value);
	}

	@Override
	public String toDisplayValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value.getClass().isArray()) {
			return String.join(", ", (String []) value);
		}

		//
		// this should not happen
		//
		return value.toString();
	}
}
