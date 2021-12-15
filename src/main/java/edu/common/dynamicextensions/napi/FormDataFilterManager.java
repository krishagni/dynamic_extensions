package edu.common.dynamicextensions.napi;

import edu.common.dynamicextensions.domain.nui.UserContext;

public interface FormDataFilterManager {
	public FormDataFilterManager addPreFilter(FormDataFilter filter);
	
	public FormDataFilterManager addPreFilter(String formName, FormDataFilter filter);
	
	public FormDataFilterManager removePreFilter(FormDataFilter filter);
	
	public FormDataFilterManager removePreFilter(String formName, FormDataFilter filter); 
	
	public FormDataFilterManager addPostFilter(FormDataFilter filter);
	
	public FormDataFilterManager addPostFilter(String formName, FormDataFilter filter);
	
	public FormDataFilterManager removePostFilter(FormDataFilter filter);
	
	public FormDataFilterManager removePostFilter(String formName, FormDataFilter filter);
	
	public FormData executePreFilters(UserContext userCtx, FormData formData);
	
	public FormData executePostFilters(UserContext userCtx, FormData formData);
}
