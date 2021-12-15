package edu.common.dynamicextensions.domain.nui.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.napi.FormException;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class ListBoxFactory extends AbstractControlFactory {

	public static ListBoxFactory getInstance() {
		return new ListBoxFactory();
	}
		
	@Override
	public String getType() {
		return "listBox";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		Map<String, Object> lbProps = new HashMap<>();
		lbProps.put("multiSelect", getBooleanValue(ele, "multiSelect", false));
		lbProps.put("autoCompleteDropdown", getBooleanValue(ele, "autoCompleteDropdown", false));
		lbProps.put("table", getTextValue(ele, "table"));
		lbProps.put("parentKey", getTextValue(ele, "parentKey"));
		lbProps.put("foreignKey", getTextValue(ele, "foreignKey"));
		lbProps.put("minQueryChars", getIntValue(ele, "minQueryChars", null));
		lbProps.put("noOfRows", getIntValue(ele, "noOfRows", null));

		ListBox listBox = getListBox(lbProps);
		setSelectProps(listBox, ele, row, xPos, props.getProperty("pvDir"));
		return listBox;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		ListBox listBox = getListBox(props);
		setSelectProps(listBox, props, row, xPos);
		return listBox;
	}

	private ListBox getListBox(Map<String, Object> props) {
		boolean isMultiSelect = getBool(props, "multiSelect", false);
		boolean autoComplete  = getBool(props, "autoCompleteDropdown", false);

		if (!isMultiSelect && autoComplete) {
			throw new FormException("Autocomplete dropdown cannot be used for non-multiselect listBox");
		}

		ListBox listBox;
		if (isMultiSelect) {
			MultiSelectListBox msLb = new MultiSelectListBox();
			msLb.setTableName((String) props.get("table"));
			msLb.setParentKey((String) props.get("parentKey"));
			msLb.setForeignKey((String) props.get("foreignKey"));
			listBox = msLb;
		} else {
			listBox = new ListBox();
		}


		Integer minQueryChars = getInt(props, "minQueryChars", null);
		if (minQueryChars != null) {
			listBox.setMinQueryChars(minQueryChars);
		}

		Integer noOfRows = getInt(props, "noOfRows", null);
		if (noOfRows != null) {
			listBox.setNoOfRows(noOfRows);
		}

		listBox.setAutoCompleteDropdownEnabled(autoComplete);
		return listBox;
	}
}
