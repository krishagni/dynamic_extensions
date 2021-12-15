package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getIntValue;
import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;

public class MultiSelectCheckboxFactory extends AbstractControlFactory {

	public static MultiSelectCheckboxFactory getInstance() {
		return new MultiSelectCheckboxFactory();
	}
	
	@Override
	public String getType() {
		return "checkBox";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		MultiSelectCheckBox msCheckBox = new MultiSelectCheckBox();
		setSelectProps(msCheckBox, ele, row, xPos, props.getProperty("pvDir"));
		
		Integer optionsPerRow = getIntValue(ele, "optionsPerRow", null);
		if (optionsPerRow != null) {
			msCheckBox.setOptionsPerRow(optionsPerRow);
		}
		
		msCheckBox.setParentKey(getTextValue(ele, "parentKey"));
		msCheckBox.setForeignKey(getTextValue(ele, "foreignKey"));
		msCheckBox.setTableName(getTextValue(ele, "table"));
		return msCheckBox;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		MultiSelectCheckBox msCheckBox = new MultiSelectCheckBox();
		setSelectProps(msCheckBox, props, row, xPos);

		Integer optionsPerRow = getInt(props, "optionsPerRow", null);
		if (optionsPerRow != null) {
			msCheckBox.setOptionsPerRow(optionsPerRow);
		}

		msCheckBox.setParentKey((String) props.get("parentKey"));
		msCheckBox.setForeignKey((String) props.get("foreignKey"));
		msCheckBox.setTableName((String) props.get("table"));
		return msCheckBox;
	}
}
