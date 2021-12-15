package edu.common.dynamicextensions.napi.impl;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataFilter;

public class FormDataFilterChain {
	private String formName;
	
	private List<FormDataFilter> preFilters = new ArrayList<FormDataFilter>();
	
	private List<FormDataFilter> postFilters = new ArrayList<FormDataFilter>();
	
	public FormDataFilterChain(String formName) {
		this.formName = formName;
	}
	
	public void addPreFilter(FormDataFilter filter) {
		addFilter(filter, preFilters);
	}
	
	public void addPostFilter(FormDataFilter filter) {
		addFilter(filter, postFilters);
	}
	
	public void removePreFilter(FormDataFilter filter) {
		removeFilter(filter, preFilters);
	}
	
	public void removePostFilter(FormDataFilter filter) {
		removeFilter(filter, postFilters);
	}
	
	public FormData executePreFilters(UserContext userCtx, FormData input) {
		return executeFilters(userCtx, input, preFilters);
	}
	
	public FormData executePostFilters(UserContext userCtx, FormData input) {
		return executeFilters(userCtx, input, postFilters);
	}
	
	private void addFilter(FormDataFilter filter, List<FormDataFilter> filters) {
		int idx = getFilterIndex(filter, filters);
		if (idx == -1) {
			filters.add(filter);
		}
	}
	
	private void removeFilter(FormDataFilter filter, List<FormDataFilter> filters) {
		int idx = getFilterIndex(filter, filters);
		if (idx != -1) {
			filters.remove(idx);
		}		
	}
	
	private int getFilterIndex(FormDataFilter filter, List<FormDataFilter> filters) {
		int idx = 0;
		for (FormDataFilter existing : filters) {
			if (existing == filter) {
				return idx;
			}
		}
		
		return -1;		
	}
	
	private FormData executeFilters(UserContext userCtx, FormData input, List<FormDataFilter> filters) {
		for (FormDataFilter filter : filters) {
			input = filter.execute(userCtx, input);
		}
		
		return input;
	}
}
