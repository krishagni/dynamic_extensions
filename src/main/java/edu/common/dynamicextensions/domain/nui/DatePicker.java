
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;
import edu.common.dynamicextensions.nutility.DeConfiguration;
import edu.common.dynamicextensions.nutility.Util;

public class DatePicker extends Control implements Serializable {
	private static final long serialVersionUID = 6046956576964435896L;

	private static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	private static final String DEFAULT_DATE_FORMAT = "MM-dd-yyyy";
	
	private static final String DEFAULT_TIME_FORMAT = "HH:mm";
	
	public enum DefaultDateType {
		PREDEFINED,
		CURRENT_DATE,
		NONE
	};

	private String format = DEFAULT_DATE_FORMAT;
	
	private boolean showCalendar = true;
	
	private Date defaultDate;
	
	private DefaultDateType defaultDateType = DefaultDateType.NONE;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean showCalendar() {
		return showCalendar;
	}

	public void setShowCalendar(boolean showCalendar) {
		this.showCalendar = showCalendar;
	}

	public Date getDefaultDate() {
		return defaultDate;
	}

	public void setDefaultDate(Date defaultDate) {
		this.defaultDate = defaultDate;
	}

	public DefaultDateType getDefaultDateType() {
		return defaultDateType;
	}

	public void setDefaultDateType(DefaultDateType defaultDateType) {
		this.defaultDateType = defaultDateType;
	}

	public boolean isDateTimeFmt() {
		return format.contains("HH:mm") || format.contains("hh:mm");
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getDateColType()));
	}
	
	@Override
	public DataType getDataType() {
		return DataType.DATE;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Date fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		String fmt = DeConfiguration.getInstance().dateFormat();
		if (fmt == null) {
			fmt = DEFAULT_DATE_FORMAT;
		}

		if (isDateTimeFmt()) {
			String timeFormat = DeConfiguration.getInstance().timeFormat();
			if (StringUtils.isBlank(timeFormat)) {
				timeFormat = DEFAULT_TIME_FORMAT;
			}
			fmt = fmt + " " + timeFormat;
		}

		value = value.trim();
		if (value.endsWith("Z")) {
			try {
				return Date.from(Instant.parse(value));
			} catch (DateTimeParseException dtpe) {
				throw new FormException("Error creating date object from [" + value + "]. Format: " + fmt, dtpe);
			}
		} else {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
				simpleDateFormat.setLenient(false);
				return simpleDateFormat.parse(value);
			} catch (Exception e) {
				try {
					return new Date(Long.parseLong(value));
				} catch (Exception e1) {
					try {
						return YYYY_MM_DD.parse(value);
					} catch (Exception e2) {
						throw new FormException("Error creating date object from [" + value + "]. Format: " + fmt, e);
					}
				}
			}
		}
	}
	
	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String)value;
		} else if (value instanceof Number) {
			return value.toString();
		}
		
		return "" + getDateObj(value).getTime();		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + (showCalendar ? 1231 : 1237);
		result = prime * result	+ ((defaultDate == null) ? 0 : defaultDate.hashCode());
		result = prime * result	+ ((defaultDateType == null) ? 0 : defaultDateType.hashCode());
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
		
		DatePicker other = (DatePicker) obj;
		if (!StringUtils.equals(format, other.format) ||
			showCalendar != other.showCalendar ||
			(defaultDate == null && other.defaultDate != null) ||
			(defaultDate != null && !defaultDate.equals(other.defaultDate)) ||
			defaultDateType != other.defaultDateType) {
			return false;
		}

		return true;
	}

	@Override
	public String getCtrlType() {
		return "datePicker";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("format", getFormat());
		props.put("showCalendar", showCalendar());
		props.put("defaultType", getDefaultDateType());
		props.put("defaultValue", getDefaultDate());
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "datePicker");
		super.serializeToXml(writer, props);
		
		writeElement(writer, "format", 	getFormat());
		writeElement(writer, "showCal", showCalendar());
		
		String defaultDate = "none";
		switch (getDefaultDateType()) {
		    case NONE:
		    	defaultDate = "none";
		    	break;
		    	
		    case CURRENT_DATE:
		    	defaultDate = "current_date";
		    	break;
		    	
		    case PREDEFINED:
		    	SimpleDateFormat sdf = new SimpleDateFormat(getFormat());
		    	defaultDate = sdf.format(getDefaultDate());
		    	break;
		}
		
		writeElement(writer, "default", defaultDate);
		writeElementEnd(writer, "datePicker");		
	}
	
	private Date getDateObj(Object value) {
		try {
			if (Util.isOraTimestamp(value)) { 
				return Util.getDateFromOraTimestamp(value);
			} else if (value instanceof Date){ 
				return (Date)value;
			} else {
				throw new FormException("Unknown object type");
			}
		} catch (Exception e) {
			throw new FormException("Error converting input object: " + value.getClass().getName() + " to java.util.Date", e);
		}
	}

	@Override
	public ValidationStatus validate(Object value) {
		boolean empty = (value == null || value.toString().isEmpty());
		if (isMandatory() && empty) {
			return ValidationStatus.NULL_OR_EMPTY;
		}
		
		if (empty) {
			return ValidationStatus.OK;
		}
		
		try {
			fromString(value.toString());
			return ValidationStatus.OK;
		} catch (Exception e) {
			return ValidationStatus.INVALID_VALUE;
		}
	}
}
