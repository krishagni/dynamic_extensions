
package edu.common.dynamicextensions.domain.nui;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.napi.ControlValue;

public class SkipCondition {

	public static enum RelationalOp {
		LT, LE, EQ, GE, GT
	}

	private Control sourceControl;

	private RelationalOp relationalOp;

	private String value;

	public Control getSourceControl() {
		return sourceControl;
	}

	public void setSourceControl(Control sourceControl) {
		this.sourceControl = sourceControl;
	}

	public RelationalOp getRelationalOp() {
		return relationalOp;
	}

	public void setRelationalOp(RelationalOp relationalOp) {
		this.relationalOp = relationalOp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((relationalOp == null) ? 0 : relationalOp.hashCode());
		result = prime * result	+ ((sourceControl == null) ? 0 : sourceControl.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		
		SkipCondition other = (SkipCondition) obj;
		if (relationalOp != other.relationalOp ||
			(sourceControl == null && other.sourceControl != null) ||
			!sourceControl.equals(other.sourceControl) ||
			!StringUtils.equals(value, other.value)) {
			return false;
		}
		
		return true;
	}

	public boolean evaluate(ControlValue fieldValue) {
		boolean result = false;
		Control control = fieldValue.getControl();
		if (this.value != null && fieldValue.getValue() != null) {
			if (fieldValue.getValue() instanceof String[]) {
				for (String string : (String[]) fieldValue.getValue()) {
					if (value.equals(string)) {
						result = true;
						break;
					}
				}

			} else {
				Comparable conditionValueObject = control.fromString(value);
				Comparable userValueObject = control.fromString((String) fieldValue.getValue());
				switch (relationalOp) {
					case LT :
						result = (userValueObject.compareTo(conditionValueObject) < 0 ? true : false);
						break;
					case GT :
						result = (userValueObject.compareTo(conditionValueObject) > 0 ? true : false);
						break;
					case EQ :
						result = (userValueObject.compareTo(conditionValueObject) == 0 ? true : false);
						break;
					case LE :
						result = (userValueObject.compareTo(conditionValueObject) <= 0 ? true : false);
						break;
					case GE :
						result = (userValueObject.compareTo(conditionValueObject) >= 0 ? true : false);
						break;
				}
			}
		}
		return result;
	}
}
