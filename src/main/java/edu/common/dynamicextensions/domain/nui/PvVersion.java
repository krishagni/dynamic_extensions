package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PvVersion implements Serializable {
	private static final long serialVersionUID = -6241553745219532731L;

	private Date activationDate;
	
	private List<PermissibleValue> permissibleValues;
	
	private PermissibleValue defaultValue;

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public List<PermissibleValue> getPermissibleValues() {
		return permissibleValues;
	}

	public void setPermissibleValues(List<PermissibleValue> permissibleValues) {
		this.permissibleValues = permissibleValues;
	}

	public PermissibleValue getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(PermissibleValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activationDate == null) ? 0 : activationDate.hashCode());		
		result = prime * result	+ ((permissibleValues == null) ? 0 : permissibleValues.hashCode());
		result = prime * result	+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
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
		
		PvVersion other = (PvVersion) obj;
				
		if ((activationDate == null && other.activationDate != null) ||
		    (activationDate != null &&  !activationDate.equals(other.activationDate)) ||
		    (defaultValue == null && other.defaultValue != null) ||
		    (defaultValue != null && !defaultValue.equals(other.defaultValue)) ||
		    (permissibleValues == null && other.permissibleValues != null) ||
		    (permissibleValues != null && !permissibleValues.equals(other.permissibleValues))) {
			return false;
		}
		
		return true;
	}
}