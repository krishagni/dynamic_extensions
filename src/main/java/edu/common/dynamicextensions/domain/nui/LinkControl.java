package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;

import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class LinkControl extends Control {

	private String formName;

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	@Override
	public DataType getDataType() {
		return DataType.INTEGER;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getIntegerColType()));
	}

	@Override
	public BigDecimal fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		try {
			return new BigDecimal(value);
		} catch (NumberFormatException nfe) {
			throw new FormException("Invalid number: " + value);
		}
	}

	@Override
	public String getCtrlType() {
		return "linkField";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("formName", getFormName());
	}

	@Override
	public ValidationStatus validate(Object value) {
		if (isMandatory() && (value == null || value.toString().trim().isEmpty())) {
			return ValidationStatus.NULL_OR_EMPTY;
		}

		return ValidationStatus.OK;
	}

	@Override
	public void serializeToXml(Writer writer, Properties props) {
		super.serializeToXml(writer, props);
		writeElement(writer, "formName", getFormName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (formName == null ? 0 : formName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LinkControl)) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!super.equals(obj)) {
			return false;
		}

		LinkControl other = (LinkControl) obj;
		return StringUtils.equals(formName, other.formName);
	}

	@Override
	public String toString(Object value) {
		if (value instanceof Number) {
			value = new BigDecimal(((Number)value).toString());
		}

		if (!(value instanceof BigDecimal)) {
			return null;
		}

		BigDecimal numberValue = (BigDecimal) value;
		if (BigDecimal.ZERO.compareTo(numberValue) == 0) {
			numberValue = BigDecimal.ZERO;
		}

		return numberValue.toPlainString();
	}
}
