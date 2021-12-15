
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

public class ComboBox extends SelectControl implements Serializable {
	private static final long serialVersionUID = 6015502487841170385L;

	private boolean lazyPvFetchingEnabled;

	private int noOfColumns;

	private int minQueryChar;

	public boolean isLazyPvFetchingEnabled() {
		return lazyPvFetchingEnabled;
	}

	public void setLazyPvFetchingEnabled(boolean lazyPvFetchingEnabled) {
		this.lazyPvFetchingEnabled = lazyPvFetchingEnabled;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}
	
	public int getMinQueryChars() {
		return minQueryChar;
	}
	
	public void setMinQueryChars(int minQueryChar) {
		this.minQueryChar = minQueryChar;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (lazyPvFetchingEnabled ? 1231 : 1237);
		result = prime * result + noOfColumns;
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

		ComboBox other = (ComboBox) obj;
		if (lazyPvFetchingEnabled != other.lazyPvFetchingEnabled ||
			noOfColumns != other.noOfColumns ||
			minQueryChar != other.minQueryChar) {
			return false;
		}
		
		return true;
	}

	@Override
	public String getCtrlType() {
		return "combobox";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "dropDown");
		super.serializeToXml(writer, props);
		
		writeElement(writer, "lazyLoad",      isLazyPvFetchingEnabled());
		writeElement(writer, "width",         getNoOfColumns());			
		writeElement(writer, "minQueryChars", getMinQueryChars());				
		writeElementEnd(writer, "dropDown");		
	}

	@Override
	public ValidationStatus validate(Object value) {
		return validateSingle(value);
	}
}
