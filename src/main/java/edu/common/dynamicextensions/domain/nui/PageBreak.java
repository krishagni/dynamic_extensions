package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PageBreak extends Control {
	private static final long serialVersionUID = 8682630795270255415L;

	@Override
	public DataType getDataType() {
		return null;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return null;
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}

	@Override
	public String getCtrlType() {
		return "pageBreak";
	}

	@Override
	public void getProps(Map<String, Object> props) {

	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "pageBreak");
		writeElementEnd(writer, "pageBreak");		
	}
}
