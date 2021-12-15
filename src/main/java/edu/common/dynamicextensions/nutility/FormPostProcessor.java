package edu.common.dynamicextensions.nutility;

import java.util.List;

public interface FormPostProcessor {
	public void process(Long containerId , Integer sortOrder);

	public List<Long> getQueryForms();
	
	public void deleteQueryForm(Long formId);
}
