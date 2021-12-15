
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class TextArea extends TextField implements Serializable {
	private static final long serialVersionUID = 1215698978305864499L;
	
	private int noOfRows;

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public int getMinLength() {
		int minLength = 0;		
		String min = getValidationRuleParam("textLength", "min");
		if (min != null && !min.trim().isEmpty()) {
			minLength = Integer.parseInt(min);
		}
		
		return minLength;
	}
		
	public void setMinLength(int minChars) {
		addValidationRule("textLength", Collections.singletonMap("min", String.valueOf(minChars)));
	}

	public int getMaxLength() {
		int maxLength = 0;		
		String max = getValidationRuleParam("textLength", "max");
		if (max != null && !max.trim().isEmpty()) {
			maxLength = Integer.parseInt(max);
		}
		
		return maxLength;		
	}
	
	public void setMaxLength(int maxChars) {
		addValidationRule("textLength", Collections.singletonMap("max", String.valueOf(maxChars)));
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getTextColType()));
	}

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	
	@Override
	public String fromString(String value) {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfRows;
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
		
		TextArea other = (TextArea) obj;
		if (noOfRows != other.noOfRows) {
			return false;
		}
		
		return true;
	}

	@Override
	public String getCtrlType() {
		return "textArea";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
		props.put("noOfRows", getNoOfRows());
		props.put("minLength", getMinLength());
		props.put("maxLength", getMaxLength());		
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "textArea");
		super.serializeToXml(writer, props);
		
		writeElement(writer, "height", getNoOfRows());			
		writeElementEnd(writer, "textArea");		
	}
}
