package edu.common.dynamicextensions.domain.nui.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Control;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class DropdownFactory extends AbstractControlFactory {

	public static DropdownFactory getInstance() {
		return new DropdownFactory();
	}
	
	@Override
	public String getType() {
		return "dropDown";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		ComboBox comboBox = new ComboBox();
		setSelectProps(comboBox, ele, row, xPos, props.getProperty("pvDir"));

		Map<String, Object> comboProps = new HashMap<>();
		comboProps.put("lazyLoad", getBooleanValue(ele, "lazyLoad"));
		comboProps.put("minQueryChars", getIntValue(ele, "minQueryChars", null));
		comboProps.put("width", getIntValue(ele, "width", null));
		setComboxBoxProps(comboBox, comboProps);
		return comboBox;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		ComboBox comboBox = new ComboBox();
		setSelectProps(comboBox, props, row, xPos);
		setComboxBoxProps(comboBox, props);
		return comboBox;
	}

	private void setComboxBoxProps(ComboBox comboBox, Map<String, Object> props) {
		comboBox.setLazyPvFetchingEnabled(getBool(props, "lazyLoad"));

		Integer minQueryChars = getInt(props, "minQueryChars", null);
		if (minQueryChars != null) {
			comboBox.setMinQueryChars(minQueryChars);
		}

		Integer width = getInt(props, "width", null);
		if (width != null) {
			comboBox.setNoOfColumns(width);
		}
	}
}
