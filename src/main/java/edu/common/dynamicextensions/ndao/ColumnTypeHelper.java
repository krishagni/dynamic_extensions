package edu.common.dynamicextensions.ndao;

public class ColumnTypeHelper {

	public static String getStringColType() {
		return DbSettingsFactory.getDbSettings().getStringColType();
	}
	
	public static String getTextColType() {
		return DbSettingsFactory.getDbSettings().getTextColType();
	}
		
	public static String getFloatColType() {
		return DbSettingsFactory.getDbSettings().getFloatColType();
	}
	
	public static String getIntegerColType() {
		return DbSettingsFactory.getDbSettings().getIntegerColType();
	}
	
	public static String getDateColType() {
		return DbSettingsFactory.getDbSettings().getDateColType();
	}

	public static String getBlob() {
		return DbSettingsFactory.getDbSettings().getBlob();
	}
}
