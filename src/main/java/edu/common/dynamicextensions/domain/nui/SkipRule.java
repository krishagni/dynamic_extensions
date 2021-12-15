package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkipRule implements Serializable {
	private static final long serialVersionUID = -267620889178734818L;

	public static enum LogicalOp {
		AND, OR
	}
	
	private List<SkipCondition> conditions = new ArrayList<SkipCondition>();
	
	private LogicalOp logicalOp = LogicalOp.AND;
	
	private List<SkipAction> actions = new ArrayList<SkipAction>();
	
	public List<SkipCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<SkipCondition> conditions) {
		this.conditions = conditions;
	}

	public LogicalOp getLogicalOp() {
		return logicalOp;
	}

	public void setLogicalOp(LogicalOp logicalOp) {
		this.logicalOp = logicalOp;
	}

	public List<SkipAction> getActions() {
		return actions;
	}

	public void setActions(List<SkipAction> actions) {
		this.actions = actions;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result	+ ((conditions == null) ? 0 : conditions.hashCode());
		result = prime * result	+ ((logicalOp == null) ? 0 : logicalOp.hashCode());
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
		
		SkipRule other = (SkipRule) obj;
		if ((actions == null && other.actions != null) ||
			!actions.equals(other.actions) ||
			(conditions == null && other.conditions != null) ||
			!conditions.equals(other.conditions) ||
			logicalOp != other.logicalOp) {			
			return false;
		}
		
		return true;
	}	
}
