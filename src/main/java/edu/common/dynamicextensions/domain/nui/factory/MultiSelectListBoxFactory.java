package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Control;

public class MultiSelectListBoxFactory extends ListBoxFactory {
	public static MultiSelectListBoxFactory getInstance() {
		return new MultiSelectListBoxFactory();
	}

	@Override
	public String getType() {
		return "multiSelectListbox";
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		props.put("multiSelect", true);
		return super.parseControl(props, row, xPos);
	}
}
