package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.StringTextField;

public class TextFieldFactory extends AbstractControlFactory {

	public static TextFieldFactory getInstance() {
		return new TextFieldFactory();
	}
	
	@Override
	public String getType() {
		return "textField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		StringTextField textField = new StringTextField();
		setControlProps(textField, ele, row, xPos);

		Map<String, Object> textProps = new HashMap<>();
		textProps.put("width", getIntValue(ele, "width", null));
		textProps.put("defaultValue", getTextValue(ele, "defaultValue", ""));
		textProps.put("url", getBooleanValue(ele, "url"));
		textProps.put("password", getBooleanValue(ele, "password"));
		textProps.put("minLength", getIntValue(ele, "minLength", null));
		textProps.put("maxLength", getIntValue(ele, "maxLength", null));
		setTextFieldProps(textField, textProps);
		return textField;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		StringTextField textField = new StringTextField();
		setControlProps(textField, props, row, xPos);
		setTextFieldProps(textField, props);
		return textField;
	}

	private void setTextFieldProps(StringTextField textField, Map<String, Object> props) {
		Integer width = getInt(props,"width", null);
		if (width != null) {
			textField.setNoOfColumns(width);
		}

		textField.setDefaultValue((String) props.getOrDefault("defaultValue", ""));
		textField.setUrl(getBool(props, "url"));
		textField.setPassword(getBool(props, "password"));

		Integer minLen = getInt(props, "minLength", null);
		if (minLen != null) {
			textField.setMinLength(minLen);
		}

		Integer maxLen = getInt(props, "maxLength", null);
		if (maxLen != null) {
			textField.setMaxLength(maxLen);
		}
	}
}