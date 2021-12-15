
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.*;

import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class CheckBox extends Control implements Serializable {
	private static final long serialVersionUID = -2448001564822140677L;
	
	private boolean defaultValueChecked;

	public boolean isDefaultValueChecked() {
		return defaultValueChecked;
	}

	public void setDefaultValueChecked(boolean defaultValueChecked) {
		this.defaultValueChecked = defaultValueChecked;
	}

	// TODO: Be database agnostic
	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getFloatColType()));
	}

	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean fromString(String value) {
		return (value != null) && (value.equals("1") || value.equalsIgnoreCase("true"));
	}
	
	@Override
	public String toString(Object value) { 
		if (value instanceof BigDecimal) {
			int val = ((BigDecimal) value).intValue();
			return val == 0 ? "0" : "1";
		} else if (value == null) {
			return "0";
		} else {
			String valStr = value.toString();
			return valStr.equals("true") || valStr.equals("1") ? "1" : "0";
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (defaultValueChecked ? 1231 : 1237);
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
		
		CheckBox other = (CheckBox) obj;
		if (defaultValueChecked != other.defaultValueChecked) {
			return false;
		}
		
		return true;
	}

	@Override
	public String getCtrlType() {
		return "booleanCheckbox";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("defaultChecked", isDefaultValueChecked());		
	}	
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "booleanCheckBox");
		super.serializeToXml(writer, props);
		
		writeElement(writer, "checked", isDefaultValueChecked());			
		writeElementEnd(writer, "booleanCheckBox");		
	}

	@Override
	public ValidationStatus validate(Object value) {
		boolean empty = (value == null || value.toString().trim().isEmpty());
		if (isMandatory() && empty) {
			return ValidationStatus.NULL_OR_EMPTY;
		}
		
		if (empty) {
			return ValidationStatus.OK;
		}
		
		String valStr = value.toString().trim();
		if (!valStr.equals("true") && 
			!valStr.equals("false") && 
			!valStr.equals("1") && 
			!valStr.equals("0")) {
			return ValidationStatus.INVALID_VALUE;
		}
		
		return ValidationStatus.OK;		
	}
}
