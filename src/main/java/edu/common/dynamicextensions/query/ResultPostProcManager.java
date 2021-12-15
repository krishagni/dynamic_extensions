package edu.common.dynamicextensions.query;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.napi.FormException;

public class ResultPostProcManager {
	private static ResultPostProcManager instance = new ResultPostProcManager();
			
	private Map<String, ResultPostProcFactory> factoryMap = new HashMap<String, ResultPostProcFactory>();
	
	public static ResultPostProcManager getInstance() {
		return instance;
	}
	
	private ResultPostProcManager() {
		addFactory("crosstab", CrosstabFactory.getInstance());
		addFactory("columnsummary", ColumnSummaryPostProcFactory.getInstance());
	}

	public Map<String, ResultPostProcFactory> getFactoryMap() {
		return factoryMap;
	}

	public void setFactoryMap(Map<String, ResultPostProcFactory> factoryMap) {
		this.factoryMap = factoryMap;
	}
	
	public void addFactory(String name, ResultPostProcFactory factory) {
		if (name == null || name.isEmpty()) {
			throw new FormException("Invalid result post processor name");
		}
		
		if (factory == null) {
			throw new FormException("Result post processor factory can't be null");
		}
		
		factoryMap.put(name, factory);
	}
	
	public ResultPostProcFactory getFactory(String name) {
		return factoryMap.get(name);
	}
}
