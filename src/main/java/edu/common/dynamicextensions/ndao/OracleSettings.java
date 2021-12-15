package edu.common.dynamicextensions.ndao;


public class OracleSettings implements DbSettings {

	private static OracleSettings instance = null;

	public static DbSettings getInstance() {
		if (instance == null) {
			instance = new OracleSettings();
		}
		
		return instance;
	}
	
	
	@Override
	public String getStringColType() {
		return "VARCHAR2(255)";
	}

	@Override
	public String getTextColType() {
		return "VARCHAR2(4000)";
	}
	@Override
	public String getFloatColType() {
		return "NUMBER(19, 6)";
	}

	@Override
	public String getIntegerColType() {
		return "NUMBER(19, 0)";
	}

	@Override
	public String getDateColType() {
		return "TIMESTAMP";
	}

	@Override
	public String getBlob() {
		return "BLOB";
	}

}
