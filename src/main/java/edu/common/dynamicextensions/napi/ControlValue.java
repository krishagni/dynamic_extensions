
package edu.common.dynamicextensions.napi;

import java.util.List;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;

public class ControlValue {

	private Control control;

	/**
	 *   String
	 *   String[]
	 *   FileControlValue
	 *   List<FormData>
	 */
	private Object value;

	/*
	 * Used by lookup control
	 */
	private Object uiValue;

	private String codedValue;

	/**
	 * Updated by EnableAction and DisbleAction
	 */
	private boolean readOnly;

	/**
	 * Updated by ShowAction and HideAction 
	 */
	private boolean hidden;

	/**
	 * Used to decide whether subform HTML should be complete form or "Details" hyperlink
	 */
	private boolean showAssociationControlsAsLink;

	private String errorMessage;

	/**
	 * it is updated by PermissibleValueAction skip logic, to subset PVs
	 */
	private List<PermissibleValue> permissibleValues;

	public List<PermissibleValue> getPermissibleValues() {
		return permissibleValues;
	}

	public void setPermissibleValues(List<PermissibleValue> permissibleValues) {
		this.permissibleValues = permissibleValues;
	}

	public boolean isShowAssociationControlsAsLink() {
		return showAssociationControlsAsLink;
	}

	public void setShowAssociationControlsAsLink(boolean showAssociationControlsAsLink) {
		this.showAssociationControlsAsLink = showAssociationControlsAsLink;
	}

	public ControlValue(Control control, Object object) {
		this.control = control;
		this.value = object;
	}

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getUiValue() {
		return uiValue;
	}

	public void setUiValue(Object uiValue) {
		this.uiValue = uiValue;
	}

	public String getCodedValue() {
		return codedValue;
	}

	public void setCodedValue(String codedValue) {
		this.codedValue = codedValue;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return control.toString(value);
	}

	public boolean isEmpty() {
		boolean isEmpty = false;

		if (value == null || value.toString().isEmpty()) {
			isEmpty = true;
		} else if (value instanceof String[] && ((String[]) value).length == 0) {
			isEmpty = true;
		} else if (value instanceof List && ((List)value).isEmpty()) {
			isEmpty = true;
		}
		
		return isEmpty;
	}
}
