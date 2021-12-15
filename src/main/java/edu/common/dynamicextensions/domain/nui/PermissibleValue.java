package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class PermissibleValue  implements Comparable<PermissibleValue>, Serializable{
	private static final long serialVersionUID = -2798378925431518758L;

	private String optionName;
	
	private Long numericCode;
	
	private String definitionSource;
	
	private String conceptCode;
	
	private String value;

	private String showWhen;
	
	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public Long getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(Long numericCode) {
		this.numericCode = numericCode;
	}

	public String getDefinitionSource() {
		return definitionSource;
	}

	public void setDefinitionSource(String definitionSource) {
		this.definitionSource = definitionSource;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getShowWhen() {
		return showWhen;
	}

	public void setShowWhen(String showWhen) {
		this.showWhen = showWhen;
	}

	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((optionName == null) ? 0 : optionName.hashCode());
		result = prime * result	+ ((numericCode == null) ? 0 : numericCode.hashCode());
		result = prime * result	+ ((definitionSource == null) ? 0 : definitionSource.hashCode());
		result = prime * result	+ ((conceptCode == null) ? 0 : conceptCode.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((showWhen == null) ? 0 : showWhen.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		PermissibleValue other = (PermissibleValue) obj;
		if (!StringUtils.equals(optionName, other.optionName) ||
			(numericCode == null && other.numericCode != null) ||
			(numericCode != null && !numericCode.equals(other.numericCode)) ||
			!StringUtils.equals(definitionSource, other.definitionSource) ||
			!StringUtils.equals(conceptCode, other.conceptCode) ||
			!StringUtils.equals(value, other.value) ||
		    !StringUtils.equals(showWhen, other.showWhen)) {
			return false;
		}

		return true;
	}

	@Override
	public int compareTo(PermissibleValue obj) {
		if (this.value == obj.value) {
			return 0;
		} else if (this.value == null) {
			return -1;
		} else if (obj.value == null) {
			return 1;
		} else {
			return this.value.compareToIgnoreCase(obj.value);
		}
	}
}
