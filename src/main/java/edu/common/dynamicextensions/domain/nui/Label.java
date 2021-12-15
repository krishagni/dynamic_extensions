
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeCDataElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Label extends Control implements Serializable {
	private static final long serialVersionUID = 5717603046743274148L;

	private boolean note;

	private boolean heading;

	public boolean isNote() {
		return note;
	}

	public void setNote(boolean note) {
		this.note = note;
	}

	public boolean isHeading() {
		return heading;
	}

	public void setHeading(boolean heading) {
		this.heading = heading;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.emptyList();
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (heading ? 1231 : 1237);
		result = prime * result + (note ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		Label other = (Label) obj;
		if (heading != other.heading || note != other.note) {
			return false;
		}
		
		return true;
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}

	@Override
	public String getCtrlType() {
		return "label";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("heading", isHeading());
		props.put("note", isNote());		
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "label");
		writeElement(writer, "name", getName());
		writeElement(writer, "udn", getUserDefinedName());

		if (isHeading()) {
			writeCDataElement(writer, "heading", getCaption());
		} else if (isNote()){
			writeCDataElement(writer, "note", getCaption());
		} else {
			writeCDataElement(writer, "caption", getCaption());
		}
		
		writeElement(writer, "customLabel", getCustomLabel());
		writeElement(writer, "phi",         isPhi());
		writeElement(writer, "mandatory",   isMandatory());
		writeCDataElement(writer, "toolTip",     getToolTip());
		writeElementEnd(writer, "label");		
	}	
}
