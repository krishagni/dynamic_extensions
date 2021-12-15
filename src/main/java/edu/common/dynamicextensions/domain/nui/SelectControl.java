/**
 * 
 */
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeCDataElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;
import edu.common.dynamicextensions.nutility.ContainerXmlSerializer;

public abstract class SelectControl extends Control implements Serializable {
	private static final long serialVersionUID = 7354155108839121342L;
	
	private PvDataSource pvDataSource;
	
	public PvDataSource getPvDataSource() {
		return pvDataSource;
	}

	public void setPvDataSource(PvDataSource pvDataSource) {
		this.pvDataSource = pvDataSource;
	}
	
	public List<PermissibleValue> getPvs() {
		return getPvs(Calendar.getInstance().getTime());
	}
	
	public List<PermissibleValue> getPvs(Date activationDate) {
		return pvDataSource != null ? pvDataSource.getPermissibleValues(activationDate) : null;
	}
	
	public PermissibleValue getDefaultValue() {
		return getDefaultValue(Calendar.getInstance().getTime());
	}
	
	public PermissibleValue getDefaultValue(Date activationDate) {
		return pvDataSource != null ? pvDataSource.getDefaultValue(activationDate) : null;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), getDbType()));
	}
	
	@Override
	public DataType getDataType() {
		return pvDataSource.getDataType();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T fromString(String value) {
		if (value == null) {
			return null;
		}
		
		Object result = null;
		DataType pvType = pvDataSource.getDataType();
		if (pvType == DataType.STRING) {
			result = value;
		} else if (pvType == DataType.INTEGER || pvType == DataType.FLOAT) {
			result = new BigDecimal(value);
		} else if (pvType == DataType.BOOLEAN) {
			if (value.equals("true")) {
				result = 1;
			} else {
				result = 0;
			}
		} else if (pvType == DataType.DATE) {
			try {
				result = new SimpleDateFormat(pvDataSource.getDateFormat()).parse(value);
			} catch (Exception e) {
				throw new FormException("Error parsing date string: " + value, e);
			}			
		}
		
		return (T)result;
	}
	
	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		}
		
		String result = value.toString();
		DataType pvType = pvDataSource.getDataType();
		if (pvType == DataType.BOOLEAN && value instanceof Number) {
			Number number = (Number)value;
			if (number.intValue() == 1) {
				result = "true";
			} else {
				result = "false";
			}
		} else if (pvType == DataType.DATE && value instanceof Date) {
			try {
				result = new SimpleDateFormat(pvDataSource.getDateFormat()).format((Date)value);
			} catch (Exception e) {
				throw new FormException("Error converting date to string: " + value, e);
			}
		}
		
		return result;
	}
	
	protected String getDbType() {
		String dbType;
		switch (pvDataSource.getDataType()) {
			case STRING:
				dbType = ColumnTypeHelper.getStringColType();
				break;
				
			case INTEGER:
			case FLOAT:
			case BOOLEAN:
				dbType = ColumnTypeHelper.getFloatColType();
				break;
				
			case DATE:
				dbType = ColumnTypeHelper.getDateColType();
				break;
				
			default:
				dbType = null;
				break;
		}
		
		return dbType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((pvDataSource == null) ? 0 : pvDataSource.hashCode());
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
		
		SelectControl other = (SelectControl) obj;
		if ((pvDataSource == null && other.pvDataSource != null) ||
			!pvDataSource.equals(other.pvDataSource)) {
			return false;
		}
		
		return true;
	}
	
		
	//////////////////////////////////////////////////////////////////////////
	//
	// Used by UI
	//
	//////////////////////////////////////////////////////////////////////////	
	protected String getDisplayValue(ControlValue controlValue, Date activationDate) {
		String value = (String) controlValue.getValue();
		if (value == null) {
			PermissibleValue defaultValue = getDefaultValue(activationDate); 
			value = defaultValue != null ? defaultValue.getValue() : "";
		}		
		return value;
	}

	//
	// TODO: get rid of this method
	//
	public List<PermissibleValue> getPVList(Date encounterDate, ControlValue controlValue) {
		List<PermissibleValue> permissibleValues = controlValue.getPermissibleValues();
		if (permissibleValues == null || permissibleValues.isEmpty()) {
			permissibleValues = getPvDataSource().getPermissibleValues(encounterDate);
		}
		return permissibleValues;
	}

	@Override
	public Set<String> getAllConceptCodes() {
		Set<String> conceptCodeCollection = new HashSet<String>();
		for (PermissibleValue permissibleValue : getPvs()) {
				conceptCodeCollection.add(permissibleValue.getConceptCode());
		}
		return conceptCodeCollection;
	}

	public String getPvAsStringByConceptCode(String conceptCode) {
		return getPvAsStringByConceptCode(conceptCode, Calendar.getInstance().getTime());

	}

	public String getPvAsStringByConceptCode(String conceptCode, Date activationDate) {

		String pvString = null;

		for (PermissibleValue permissibleValue : getPvs(activationDate)) {
			if (conceptCode.equals(permissibleValue.getConceptCode())) {
				pvString = permissibleValue.getValue();
				break;
			}
		}
		return pvString;

	}
	
	@Override
	public void getProps(Map<String, Object> props) {
		PvDataSource pvDataSrc =  getPvDataSource();
		props.put("dataType", pvDataSrc.getDataType());
		props.put("dateFormat", pvDataSrc.getDateFormat());
		props.put("defaultValue", getDefaultValue());

		List<PermissibleValue> pvs = pvDataSrc.getPermissibleValues(Calendar.getInstance().getTime(), getContainer().getMaxPvListSize());
		props.put("pvs", pvs);
		props.put("pvOrdering", pvDataSrc.getOrdering().name());		
	}

	@Override
	public void serializeToXml(Writer writer, Properties props) {
		super.serializeToXml(writer, props);

		writeCDataElement(writer, "defaultValue", getDefaultValue());
		writeElementStart(writer, "options");
		
		PvDataSource pvDataSource = getPvDataSource();
		String sql = pvDataSource.getSql();
		if (sql != null) {
			writeElement(writer, "sql", sql);
		} else {
			Date today = Calendar.getInstance().getTime();
			List<PermissibleValue> pvs = pvDataSource.getPermissibleValues(today);
			ContainerXmlSerializer.writePvValues(writer, pvs, getName(), props);
		}

		writeElementEnd(writer, "options");		
	}	
	
	public boolean isValidPv(String input) {
		if (input == null || input.trim().isEmpty()) {
			return true;
		}

		return pvDataSource.isValidPv(Calendar.getInstance().getTime(), input);
	}
	
	public ValidationStatus validateSingle(Object value) {
		boolean empty = (value == null || value.toString().trim().isEmpty());
		if (isMandatory() && empty) {
			return ValidationStatus.NULL_OR_EMPTY;
		}
		
		if (empty) {
			return ValidationStatus.OK;
		}
		
		return isValidPv(value.toString()) ? ValidationStatus.OK : ValidationStatus.INVALID_VALUE;
	}

	public ValidationStatus validateMultiple(Object value) {
		if (isMandatory() && value == null) {
			return ValidationStatus.NULL_OR_EMPTY;
		}
		
		if (value == null) {
			return ValidationStatus.OK;
		}
		
		String[] values = (String[])value;
		if (isMandatory() && values.length == 0) {
			return ValidationStatus.NULL_OR_EMPTY;
		}
		
		for (String val : values) {
			if (!isValidPv(val)) {
				return ValidationStatus.INVALID_VALUE;
			}
		}
		
		return ValidationStatus.OK;
	}	
}
