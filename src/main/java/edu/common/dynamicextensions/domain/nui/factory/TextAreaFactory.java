package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getIntValue;
import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.TextArea;

public class TextAreaFactory extends AbstractControlFactory {
	public static TextAreaFactory getInstance() {
		return new TextAreaFactory();
	}

	@Override
	public String getType() {
		return "textArea";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		TextArea textArea = new TextArea();
		setControlProps(textArea, ele, row, xPos);

		Map<String, Object> textProps = new HashMap<>();
		textProps.put("width", getIntValue(ele, "width", null));
		textProps.put("defaultValue", getTextValue(ele, "defaultValue", ""));
		textProps.put("height", getIntValue(ele, "height", null));
		textProps.put("minLength", getIntValue(ele, "minLength", null));
		textProps.put("maxLength", getIntValue(ele, "maxLength", null));
		setTextAreaProps(textArea, textProps);
		return textArea;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		TextArea textArea = new TextArea();
		setControlProps(textArea, props, row, xPos);
		setTextAreaProps(textArea, props);
		return textArea;
	}

	private void setTextAreaProps(TextArea textArea, Map<String, Object> props) {
		Integer width = getInt(props, "width", null);
		if (width != null) {
			textArea.setNoOfColumns(width);
		}

		textArea.setDefaultValue((String) props.getOrDefault("defaultValue", ""));

		Integer height = getInt(props, "height", getInt(props, "noOfRows", null));
		if (height != null) {
			textArea.setNoOfRows(height);
		}

		Integer minLen = getInt(props, "minLength", null);
		if (minLen != null) {
			textArea.setMinLength(minLen);
		}

		Integer maxLen = getInt(props, "maxLength", null);
		if (maxLen != null) {
			textArea.setMaxLength(maxLen);
		}
	}
}
