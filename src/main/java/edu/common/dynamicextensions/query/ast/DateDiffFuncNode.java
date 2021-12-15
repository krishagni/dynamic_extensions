package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateDiffFuncNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = 4943379159084480011L;

	public static enum DiffType {
		MINUTES, DAY, MONTH, YEAR
	}
	
	private DiffType diffType;
	
	private ExpressionNode leftOperand;
	
	private ExpressionNode rightOperand;

	@Override
	public DataType getType() {
		return DataType.FLOAT;
	}
	
	@Override
	public DateDiffFuncNode copy() {
		DateDiffFuncNode copy = new DateDiffFuncNode();
		super.copy(this, copy);
		copy.setDiffType(diffType);
		copy.setLeftOperand(leftOperand.copy());
		copy.setRightOperand(rightOperand.copy());
		return copy;
	}
	
	@Override
	public String[] getFormNames() {
		Set<String> formNames = new HashSet<String>();
		formNames.addAll(Arrays.asList(leftOperand.getFormNames()));
		formNames.addAll(Arrays.asList(rightOperand.getFormNames()));
		return formNames.toArray(new String[0]);
	}	
	
	
	public DiffType getDiffType() {
		return diffType;
	}

	public void setDiffType(DiffType diffType) {
		this.diffType = diffType;
	}

	public ExpressionNode getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(ExpressionNode leftOperand) {
		this.leftOperand = leftOperand;
	}

	public ExpressionNode getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(ExpressionNode rightOperand) {
		this.rightOperand = rightOperand;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((diffType == null) ? 0 : diffType.hashCode());
		result = prime * result	+ ((leftOperand == null) ? 0 : leftOperand.hashCode());
		result = prime * result	+ ((rightOperand == null) ? 0 : rightOperand.hashCode());
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
		
		DateDiffFuncNode other = (DateDiffFuncNode) obj;
		if (diffType != other.diffType) {
			return false;
		} 
		
		if (leftOperand == null && other.leftOperand != null) {
			return false;
		} else if (leftOperand != null && !leftOperand.equals(other.leftOperand)) {
			return false;
		} 
		
		if (rightOperand == null && other.rightOperand != null) {
			return false;
		} else if (rightOperand != null && !rightOperand.equals(other.rightOperand)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isPhi() {
		return (leftOperand != null && leftOperand.isPhi()) || 
				(rightOperand != null && rightOperand.isPhi());
	}

	@Override
	public Set<FieldNode> getFields() {
		Set<FieldNode> fields = new HashSet<FieldNode>();
		if (leftOperand != null) {
			fields.addAll(leftOperand.getFields());
		}
		
		if (rightOperand != null) {
			fields.addAll(rightOperand.getFields());
		}
		
		return fields;
	}
}