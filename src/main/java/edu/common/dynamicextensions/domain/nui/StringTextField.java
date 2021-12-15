package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class StringTextField extends TextField implements Serializable {
	private static final long serialVersionUID = -7400829341249457422L;

	private boolean url;

	private boolean password;

	public boolean isUrl() {
		return url;
	}

	public void setUrl(boolean url)	{
		this.url = url;
	}

	public boolean isPassword()	{
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
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
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getStringColType()));
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String fromString(String value) {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (url ? 1231 : 1237);
		result = prime * result + (password ? 1231 : 1237);		
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
		
		StringTextField other = (StringTextField) obj;
		if (url != other.url || password != other.password) {
			return false;
		}
		
		return true;
	}

	@Override
	public String getCtrlType() {
		return "stringTextField";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
		props.put("url", isUrl());
		props.put("password", isPassword());		
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {		
		writeElementStart(writer, "textField");	
		super.serializeToXml(writer, props);
		
		writeElement(writer, "url",      isUrl());
		writeElement(writer, "password", isPassword());
					
		for (ValidationRule rule : getValidationRules()) {
			if (!rule.getName().equals("textLength")) {
				continue;
			}
			
			for (Entry<String, String> ruleParam : rule.getParams().entrySet()) {
				String prop = "";
				if (ruleParam.getKey().equals("min")) {
					prop = "minLength";
				} else if (ruleParam.getKey().equals("max")) {
					prop = "maxLength";
				} 
				
				if (!prop.isEmpty()) {
					writeElement(writer, prop, ruleParam.getValue());
				}
			}
		}
		
		writeElementEnd(writer, "textField");		
	}
}
