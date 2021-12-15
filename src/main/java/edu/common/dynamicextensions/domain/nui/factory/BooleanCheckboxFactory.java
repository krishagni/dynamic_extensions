package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getBooleanValue;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.Control;

public class BooleanCheckboxFactory extends AbstractControlFactory {
	public static BooleanCheckboxFactory getInstance() {
		return new BooleanCheckboxFactory();
	}

	@Override
	public String getType() {
		return "booleanCheckBox";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		CheckBox checkBox = new CheckBox();
		setControlProps(checkBox, ele, row, xPos);		
		checkBox.setDefaultValueChecked(getBooleanValue(ele, "checked"));
		return checkBox;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		CheckBox checkBox = new CheckBox();
		setControlProps(checkBox, props, row, xPos);
		checkBox.setDefaultValueChecked(getBool(props, "checked"));
		return checkBox;
	}
}
