package edu.common.dynamicextensions.domain.nui.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.DatePicker.DefaultDateType;
import edu.common.dynamicextensions.napi.FormException;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class DatePickerFactory extends AbstractControlFactory {

	public static DatePickerFactory getInstance() {
		return new DatePickerFactory();
	}
	
	@Override
	public String getType() {
		return "datePicker";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		DatePicker datePicker = new DatePicker();
		setControlProps(datePicker, ele, row, xPos);

		Map<String, Object> ctrlProps = new HashMap<>();
		ctrlProps.put("format", getTextValue(ele, "format", "MM-dd-yyyy"));
		ctrlProps.put("showCalendar", getBooleanValue(ele, "showCalendar", true));
		ctrlProps.put("default", getTextValue(ele, "default", "none"));
		setDatePickerProps(datePicker, ctrlProps);
		return datePicker;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		DatePicker datePicker = new DatePicker();
		setControlProps(datePicker, props, row, xPos);
		setDatePickerProps(datePicker, props);
		return datePicker;
	}

	private void setDatePickerProps(DatePicker datePicker, Map<String, Object> props) {
		String format = (String) props.getOrDefault("format", "MM-dd-yyyy");
		datePicker.setFormat(format);
		datePicker.setShowCalendar(getBool(props, "showCalendar", true));

		String defaultDate = (String) props.getOrDefault("default", props.getOrDefault("defaultType", "none"));
		if (defaultDate.equalsIgnoreCase("none")) {
			datePicker.setDefaultDateType(DefaultDateType.NONE);
		} else if (defaultDate.equalsIgnoreCase("current_date")) {
			datePicker.setDefaultDateType(DefaultDateType.CURRENT_DATE);
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Date date = sdf.parse(defaultDate);
				datePicker.setDefaultDate(date);
			} catch (Exception e) {
				throw new FormException("Invalid default date: " + defaultDate);
			}
		}
	}
}
