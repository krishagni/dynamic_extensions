package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class SignatureControl extends Control implements Serializable {

	private static final long serialVersionUID = 6775007298962330538L;

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getStringColType()));
	}

	@Override
	public String fromString(String value) {
		return value;
	}

	@Override
	public String getCtrlType() {
		return "signature";
	}

	@Override
	public void getProps(Map<String, Object> props) {

	}

	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "signature");
		super.serializeToXml(writer, props);
		writeElementEnd(writer, "signature");
	}

	@Override
	public ValidationStatus validate(Object value) {
		if (isMandatory() && value == null) {
			return ValidationStatus.NULL_OR_EMPTY;
		}

		if (value == null) {
			return ValidationStatus.OK;
		}

		String fileId = value.toString();
		int lastDotIdx = fileId.lastIndexOf(".");
		if (lastDotIdx == -1) {
			return ValidationStatus.INVALID_VALUE;
		}

		String type = fileId.substring(lastDotIdx + 1);
		if (!type.equalsIgnoreCase("png") &&
			!type.equalsIgnoreCase("jpeg") &&
			!type.equalsIgnoreCase("jpg")) {
			return ValidationStatus.INVALID_VALUE;
		}

		return ValidationStatus.OK;
	}
}
