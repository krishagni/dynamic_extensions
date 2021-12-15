package edu.common.dynamicextensions.napi;

import edu.common.dynamicextensions.domain.nui.Container;

public interface FormEventsListener {
	public void onCreate(Container form);
	
	public void preUpdate(Container form);

	public void onUpdate(Container form);

	public void onDelete(Container form);
}
