package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public abstract class SkipAction {
	private Long id;
	
	private Control targetCtrl;
	
	public Long getId() {
		return id;
	}
		
	public Control getTargetCtrl() {
		return targetCtrl;
	}

	public void setTargetCtrl(Control targetCtrl) {
		this.targetCtrl = targetCtrl;
	}

	public abstract void perform(ControlValue fieldValue);

	public abstract void reset(ControlValue targetControl);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((targetCtrl == null) ? 0 : targetCtrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj) || getClass() != obj.getClass()) {
			return false;
		}
		
		SkipAction other = (SkipAction) obj;
		if ((targetCtrl == null && other.targetCtrl != null) ||
			!targetCtrl.equals(other.targetCtrl)) {
				return false;
		}
		
		return true;
	}
}