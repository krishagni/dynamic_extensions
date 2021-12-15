
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class NumberField extends TextField implements Serializable {
	private static final long serialVersionUID = 1205899623349158320L;

	//
	// TODO: noOfDigits and noOfDigitsAfterDecimal needs to be pushed
	// to rule
	//
	private int noOfDigits;

	private int noOfDigitsAfterDecimal;

	private boolean calculated;

	private String formula;

	private String measurementUnits;

	public int getNoOfDigits() {
		return noOfDigits;
	}

	public void setNoOfDigits(int noOfDigits) {
		this.noOfDigits = noOfDigits;
	}

	public int getNoOfDigitsAfterDecimal() {
		return noOfDigitsAfterDecimal;
	}

	public void setNoOfDigitsAfterDecimal(int noOfDigitsAfterDecimal) {
		this.noOfDigitsAfterDecimal = noOfDigitsAfterDecimal;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	

	@Override
	public boolean isDynamic() {
		return super.isDynamic() || isCalculated();
	}

	public void setMinValue(String minValue) {
		if (StringUtils.isBlank(minValue)) {
			return;
		}

		addValidationRule("range", Collections.singletonMap("min", minValue));
	}
	
	public String getMinValue() {
		return getValidationRuleParam("range", "min");
	}

	public void setMaxValue(String maxValue) {
		if (StringUtils.isBlank(maxValue)) {
			return;
		}

		addValidationRule("range", Collections.singletonMap("max", maxValue));
	}
	
	public String getMaxValue() {
		return getValidationRuleParam("range", "max");
	}

	public String getMeasurementUnits() {
		return measurementUnits;
	}

	public void setMeasurementUnits(String measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getFloatColType()));
	}

	@Override
	public DataType getDataType() {
		return noOfDigitsAfterDecimal == 0 ? DataType.INTEGER : DataType.FLOAT;
	}
	
	@Override
	public ValidationStatus validate(Object value) {
		ValidationStatus status = super.validate(value);
		if (status != ValidationStatus.OK) {
			return status;
		}
		
		if (value == null || value.toString().trim().isEmpty()) {
			return status;
		}
		
		BigDecimal number = null;
		try {
			number = new BigDecimal(value.toString().trim());
		} catch (Exception e) {
			return ValidationStatus.NOT_NUMBER;
		}
		
		String minValStr = getMinValue();
		if (minValStr != null && !minValStr.trim().isEmpty()) {
			BigDecimal minVal = new BigDecimal(minValStr);
			if (number.compareTo(minVal) < 0) {
				return ValidationStatus.NUMBER_OUT_OF_RANGE;
			}
		}
		
		String maxValStr = getMaxValue();
		if (maxValStr != null && !maxValStr.trim().isEmpty()) {
			BigDecimal maxVal = new BigDecimal(maxValStr);
			if (number.compareTo(maxVal) > 0) {
				return ValidationStatus.NUMBER_OUT_OF_RANGE;
			}
		}
		
		int actualDigitsAfterDecimal = getNumberOfDigitsAfterDecimal(value.toString().trim());
		if (actualDigitsAfterDecimal > noOfDigitsAfterDecimal) {
			return ValidationStatus.DIGITS_AFTER_DECIMAL_EXCEEDS;
		}
		
		return ValidationStatus.OK;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		try {
			return new BigDecimal(value.trim());
		} catch (NumberFormatException nfe) {
			throw new FormException("Invalid number: " + value);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfDigits;
		result = prime * result + noOfDigitsAfterDecimal;		
		result = prime * result + (calculated ? 1231 : 1237);
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result	+ ((measurementUnits == null) ? 0 : measurementUnits.hashCode());
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
		
		NumberField other = (NumberField) obj;
		if (noOfDigits != other.noOfDigits ||
			noOfDigitsAfterDecimal != other.noOfDigitsAfterDecimal ||
			calculated != other.calculated ||
			!StringUtils.equals(formula, other.formula) ||
			!StringUtils.equals(measurementUnits, other.measurementUnits)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString(Object value) {
		if (value instanceof Number) {
			value = new BigDecimal(((Number)value).toString());
		}
		
		if (!(value instanceof BigDecimal)) {
			return value == null ? null : value.toString();
		}
		
		BigDecimal numberValue = (BigDecimal) value;
		if (BigDecimal.ZERO.compareTo(numberValue) == 0) {
			numberValue = BigDecimal.ZERO;
		} else {
			numberValue = numberValue.setScale(noOfDigitsAfterDecimal, RoundingMode.HALF_UP).stripTrailingZeros();
		}
		return numberValue.toPlainString();
	}

	@Override
	public String getCtrlType() {
		return "numberField";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
		props.put("noOfDigits", getNoOfDigits());
		props.put("noOfDigitsAfterDecimal", getNoOfDigitsAfterDecimal());
		props.put("minValue", getMinValue());
		props.put("maxValue", getMaxValue());
		props.put("calculated", isCalculated());
		props.put("formula", getFormula());		
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "numberField");
		super.serializeToXml(writer, props);

		writeElement(writer, "noOfDigits", getNoOfDigits());			
		if (getNoOfDigitsAfterDecimal() != 0) {
			writeElement(writer, "noOfDigitsAfterDecimal", getNoOfDigitsAfterDecimal());
		}
		
		writeElement(writer, "formula",          getFormula());
		writeElement(writer, "measurementUnits", getMeasurementUnits());
	
		for (ValidationRule valRule : getValidationRules()) {				
			if (!valRule.getName().equals("range")) {
				continue;
			}
			
			for (Entry<String, String> ruleParam : valRule.getParams().entrySet()) {
				String prop = "";
				if (ruleParam.equals("min")) {
					prop = "minValue";
				} else if (ruleParam.equals("max")) {
					prop = "maxValue";
				} 
				
				if (!prop.isEmpty()) {
					writeElement(writer, prop, ruleParam.getValue());
				}
			}
		}
		
		writeElementEnd(writer, "numberField");		
	}
	
	private int getNumberOfDigitsAfterDecimal(String value) {
	    int index = value.indexOf('.');
	    if (index < 0) {
	       return 0;
	    }
	    
	    return value.length() - 1 - index;
	}	
}
