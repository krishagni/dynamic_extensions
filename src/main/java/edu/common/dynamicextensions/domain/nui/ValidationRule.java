package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ValidationRule implements Serializable {	
	private static final long serialVersionUID = -5874184770973835862L;

	private String name;
	
	private Map<String, String> params = new HashMap<String, String>();  // TODO: Make value from String -> Object
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}
				
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		ValidationRule other = (ValidationRule) obj;
		if (!StringUtils.equals(name, other.name)) {
			return false;
		}
		
		if (params == null && other.params != null) {
			return false;
		}
		
		return params.equals(other.params);
	}	
}
