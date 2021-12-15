package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class HideAction extends SkipAction {
	@Override
	public void perform(ControlValue targetControl) {
		targetControl.setHidden(true);
	}

	@Override
	public void reset(ControlValue targetControl) {
		targetControl.setHidden(false);

	}
}
