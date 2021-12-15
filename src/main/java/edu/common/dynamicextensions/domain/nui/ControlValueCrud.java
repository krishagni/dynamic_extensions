package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.ndao.JdbcDao;

public interface ControlValueCrud {
	public ControlValue getValue(JdbcDao jdbcDao, FormData form);
		
	public Object saveValue(JdbcDao jdbcDao, FormData form, ControlValue value);
	
	// delete will happen shortly
}
