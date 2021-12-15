package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getIntValue;
import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.NumberField;

public class NumberFieldFactory extends AbstractControlFactory {
	public static NumberFieldFactory getInstance() {
		return new NumberFieldFactory();
	}

	@Override
	public String getType() {
		return "numberField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		NumberField numberField = new NumberField();
		setControlProps(numberField, ele, row, xPos);

		Map<String, Object> numFieldProps = new HashMap<>();
		numFieldProps.put("width", getIntValue(ele, "width", null));
		numFieldProps.put("defaultValue", getTextValue(ele, "defaultValue", ""));
		numFieldProps.put("noOfDigitsAfterDecimal", getIntValue(ele, "noOfDigitsAfterDecimal", null));
		numFieldProps.put("noOfDigits", getIntValue(ele, "noOfDigits", 19));
		numFieldProps.put("measurementUnits", getTextValue(ele, "measurementUnits"));
		numFieldProps.put("formula", getTextValue(ele, "formula"));
		numFieldProps.put("minValue", getTextValue(ele, "minValue"));
		numFieldProps.put("maxValue", getTextValue(ele, "maxValue"));
		setNumberFieldProps(numberField, numFieldProps);
		return numberField;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		NumberField numberField = new NumberField();
		setControlProps(numberField, props, row, xPos);
		setNumberFieldProps(numberField, props);
		return numberField;
	}

	private void setNumberFieldProps(NumberField numberField, Map<String, Object> props) {
		Integer width = getInt(props, "width", null);
		if (width != null) {
			numberField.setNoOfColumns(width);
		}

		numberField.setDefaultValue((String) props.getOrDefault("defaultValue", ""));

		Integer digitsAfterDecimal = getInt(props, "noOfDigitsAfterDecimal", null);
		if (digitsAfterDecimal != null) {
			numberField.setNoOfDigitsAfterDecimal(digitsAfterDecimal);
		}

		numberField.setNoOfDigits(getInt(props, "noOfDigits", 19));
		numberField.setMeasurementUnits((String) props.get("measurementUnits"));

		numberField.setFormula((String) props.get("formula"));
		if (numberField.getFormula() != null && !numberField.getFormula().trim().isEmpty()) {
			numberField.setCalculated(true);
		}

		Object min = props.get("minValue");
		numberField.setMinValue(min != null ? min.toString() : null);

		Object max = props.get("maxValue");
		numberField.setMaxValue(max != null ? max.toString() : null);
	}
}

