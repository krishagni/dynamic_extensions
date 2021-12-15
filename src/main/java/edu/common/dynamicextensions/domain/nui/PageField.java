package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class PageField implements Serializable {

	private static final long serialVersionUID = 8862292032633631268L;

	private Control ctrl;

	public Control getCtrl() {
		return ctrl;
	}

	public void setCtrl(Control ctrl) {
		this.ctrl = ctrl;
	}

	public Map<String, Object> getProps() {
		return Collections.singletonMap("field", ctrl.getName());
	}
}
