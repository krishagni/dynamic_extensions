package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class BetweenNode extends ExpressionNode implements Serializable {
	private ExpressionNode lhs;

	private ExpressionNode minNode;

	private ExpressionNode maxNode;

	@Override
	public DataType getType() {
		return lhs.getType();
	}

	@Override
	public ExpressionNode copy() {
		BetweenNode copy = new BetweenNode();
		super.copy(this, copy);
		copy.setLhs(lhs.copy());
		copy.setMinNode(minNode.copy());
		copy.setMaxNode(maxNode.copy());
		return copy;
	}

	@Override
	public String[] getFormNames() {
		Set<String> formNames = new HashSet<String>();
		formNames.addAll(Arrays.asList(lhs.getFormNames()));
		formNames.addAll(Arrays.asList(minNode.getFormNames()));
		formNames.addAll(Arrays.asList(maxNode.getFormNames()));
		return formNames.toArray(new String[0]);
	}

	public ExpressionNode getLhs() {
		return lhs;
	}

	public void setLhs(ExpressionNode lhs) {
		this.lhs = lhs;
	}

	public ExpressionNode getMinNode() {
		return minNode;
	}

	public void setMinNode(ExpressionNode minNode) {
		this.minNode = minNode;
	}

	public ExpressionNode getMaxNode() {
		return maxNode;
	}

	public void setMaxNode(ExpressionNode maxNode) {
		this.maxNode = maxNode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BetweenNode that = (BetweenNode) o;

		if (!lhs.equals(that.lhs)) return false;
		if (!maxNode.equals(that.maxNode)) return false;
		if (!minNode.equals(that.minNode)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = lhs.hashCode();
		result = 31 * result + minNode.hashCode();
		result = 31 * result + maxNode.hashCode();
		return result;
	}

	@Override
	public boolean isPhi() {
		return lhs.isPhi() || minNode.isPhi() || maxNode.isPhi();
	}

	@Override
	public Set<FieldNode> getFields() {
		Set<FieldNode> fields = new HashSet<FieldNode>();
		fields.addAll(lhs.getFields());
		fields.addAll(minNode.getFields());
		fields.addAll(maxNode.getFields());
		return fields;
	}
}
