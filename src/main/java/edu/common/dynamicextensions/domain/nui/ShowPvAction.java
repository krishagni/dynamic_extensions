package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.napi.ControlValue;

public class ShowPvAction extends SkipAction implements Serializable {
	private static final long serialVersionUID = 1683054969541760054L;

	private List<PermissibleValue> listOfPvs = new ArrayList<PermissibleValue>();
	
	private PermissibleValue defaultPv;

	public List<PermissibleValue> getListOfPvs() {
		return listOfPvs;
	}

	public void setListOfPvs(List<PermissibleValue> listOfPvs) {
		this.listOfPvs = listOfPvs;
	}

	public PermissibleValue getDefaultPv() {
		return defaultPv;
	}

	public void setDefaultPv(PermissibleValue defaultPv) {
		this.defaultPv = defaultPv;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((defaultPv == null) ? 0 : defaultPv.hashCode());
		result = prime * result + ((listOfPvs == null) ? 0 : listOfPvs.hashCode());
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
		
		ShowPvAction other = (ShowPvAction) obj;
		if ((defaultPv == null && other.defaultPv != null) ||
			!defaultPv.equals(other.defaultPv) ||
			(listOfPvs == null && other.listOfPvs != null) ||
			!listOfPvs.equals(other.listOfPvs)) {
			return false;
		}
		
		return true;
	}

	@Override
	public void perform(ControlValue fieldValue) {
		fieldValue.setPermissibleValues(listOfPvs);
	}

	@Override
	public void reset(ControlValue fieldValue) {
		if (fieldValue.getPermissibleValues() != null) {
			fieldValue.getPermissibleValues().clear();
		}
	}
}
