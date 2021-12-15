
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

public class ListBox extends SelectControl implements Serializable {
	private static final long serialVersionUID = -5758511918989339406L;

	private int noOfRows;
	
	private boolean autoCompleteDropdownEnabled;

	private int minQueryChar = 3;

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	
	public boolean isAutoCompleteDropdownEnabled() {
		return autoCompleteDropdownEnabled;
	}
	
	public void setAutoCompleteDropdownEnabled(boolean autoCompleteDropdownEnabled) {
		this.autoCompleteDropdownEnabled = autoCompleteDropdownEnabled;
	}
	
	public int getMinQueryChars() {
		return minQueryChar;
	}
	
	public void setMinQueryChars(int minQueryChars) {
		this.minQueryChar = minQueryChars;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfRows;
		result = prime * result + (autoCompleteDropdownEnabled ? 1231 : 1237);
		result = prime * result + minQueryChar;		
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
		
		ListBox other = (ListBox) obj;
		if (noOfRows != other.noOfRows ||
			autoCompleteDropdownEnabled != other.autoCompleteDropdownEnabled ||
			minQueryChar != other.minQueryChar) {		
			return false;
		}

		return true;
	}

	@Override
	public String getCtrlType() {
		return "listbox";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "listBox");			
		super.serializeToXml(writer, props);
		
		writeElement(writer, "autoCompleteDropdown", isAutoCompleteDropdownEnabled());			
		writeElement(writer, "noOfRows",             getNoOfRows());	
		writeElement(writer, "minQueryChars",        getMinQueryChars());	
		
		if (this instanceof MultiSelectListBox) { // Foul code smell
			writeElement(writer, "multiSelect", true);			
		} else {
			writeElement(writer, "multiSelect", false);
		}
		
		writeElementEnd(writer, "listBox");		
	}
	
	@Override
	public ValidationStatus validate(Object value) {
		return validateSingle(value);
	}	
}
