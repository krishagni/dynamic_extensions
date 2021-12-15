package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class ShowAction extends SkipAction {
	@Override
	public void perform(ControlValue fieldValue) {
		fieldValue.setHidden(false);

	}

	@Override
	public void reset(ControlValue fieldValue) {
		fieldValue.setHidden(true);
	}
}
