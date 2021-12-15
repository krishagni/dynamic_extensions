package edu.common.dynamicextensions.domain.nui;

import java.util.Properties;

public interface LookupControl {	
	String getTableName();
	
	String getParentKey();
	
	String getLookupKey();

	String getValueColumn();
	
	DataType getValueType();

	Properties getPvSourceProps();

	boolean isMultiValued();

	String getCollectionTable();

	String getCollectionKey();

	String getCodeColumn();
}
