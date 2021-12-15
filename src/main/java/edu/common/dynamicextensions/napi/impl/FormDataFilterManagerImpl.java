package edu.common.dynamicextensions.napi.impl;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataFilter;
import edu.common.dynamicextensions.napi.FormDataFilterManager;

public class FormDataFilterManagerImpl implements FormDataFilterManager {
	
	private static FormDataFilterManager instance = new FormDataFilterManagerImpl();
	
	private Map<String, FormDataFilterChain> chains = new HashMap<String, FormDataFilterChain>();
	
	private FormDataFilterChain all = new FormDataFilterChain("ALL FORMS");
	
	private FormDataFilterManagerImpl() {		
	}
	
	public static FormDataFilterManager getInstance() {
		return instance;
	}

	@Override
	public FormDataFilterManager addPreFilter(FormDataFilter filter) {
		all.addPreFilter(filter);
		return this;
	}
	
	@Override
	public FormDataFilterManager addPreFilter(String formName, FormDataFilter filter) {
		FormDataFilterChain chain = getFilterChain(formName, true);
		chain.addPreFilter(filter);
		return this;
	}

	@Override
	public FormDataFilterManager removePreFilter(FormDataFilter filter) {
		all.removePreFilter(filter);
		return this;
	}
	
	@Override
	public FormDataFilterManager removePreFilter(String formName, FormDataFilter filter) {
		FormDataFilterChain chain = getFilterChain(formName, false);
		if (chain != null) {
			chain.removePreFilter(filter);
		}
		
		return this;
	}

	@Override
	public FormDataFilterManager addPostFilter(FormDataFilter filter) {
		all.addPostFilter(filter);
		return this;
	}
	
	@Override
	public FormDataFilterManager addPostFilter(String formName, FormDataFilter filter) {
		FormDataFilterChain chain = getFilterChain(formName, true);
		chain.addPostFilter(filter);
		return this;
	}

	@Override
	public FormDataFilterManager removePostFilter(FormDataFilter filter) {
		all.removePostFilter(filter);
		return this;
	}
	
	@Override
	public FormDataFilterManager removePostFilter(String formName, FormDataFilter filter) {
		FormDataFilterChain chain = getFilterChain(formName, false);
		if (chain != null) {
			chain.removePostFilter(filter);
		}
		
		return this;
	}
		
	@Override
	public FormData executePreFilters(UserContext userCtx, FormData input) {
		input = all.executePreFilters(userCtx, input);
		
		Container form = input.getContainer();		
		FormDataFilterChain chain = getFilterChain(form.getName(), false);
		if (chain != null) {
			input = chain.executePreFilters(userCtx, input);
		}
		
		return input;
	}

	@Override
	public FormData executePostFilters(UserContext userCtx, FormData input) {				
		Container form = input.getContainer();
		FormDataFilterChain chain = getFilterChain(form.getName(), false);
		if (chain != null) {
			input = chain.executePostFilters(userCtx, input);
		}
		
		input = all.executePostFilters(userCtx, input);
		return input;
	}
	
	private FormDataFilterChain getFilterChain(String formName, boolean createIfAbsent) {
		FormDataFilterChain chain = chains.get(formName);
		if (chain == null && createIfAbsent) {
			chain = new FormDataFilterChain(formName);
			chains.put(formName, chain);
		}		
		
		return chain;
	}
}
