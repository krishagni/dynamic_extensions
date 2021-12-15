package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getIntValue;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.RadioButton;

public class RadioButtonFactory extends AbstractControlFactory {

	public static RadioButtonFactory getInstance() {
		return new RadioButtonFactory();
	}
	
	@Override
	public String getType() {
		return "radioButton";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		RadioButton radioButton = new RadioButton();
		setSelectProps(radioButton, ele, row, xPos, props.getProperty("pvDir"));
		
		Integer optionsPerRow = getIntValue(ele, "optionsPerRow", null);
		if (optionsPerRow != null) {
			radioButton.setOptionsPerRow(optionsPerRow);
		}
		return radioButton;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		RadioButton radioButton = new RadioButton();
		setSelectProps(radioButton, props, row, xPos);

		Integer optionsPerRow = getInt(props, "optionsPerRow", null);
		if (optionsPerRow != null) {
			radioButton.setOptionsPerRow(optionsPerRow);
		}
		return radioButton;
	}
}
