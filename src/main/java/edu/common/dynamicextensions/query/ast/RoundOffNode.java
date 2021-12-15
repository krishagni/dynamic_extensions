package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class RoundOffNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = 1L;

	private ExpressionNode exprNode;
	
	private int noOfDigitsAfterDecimal;

	public ExpressionNode getExprNode() {
		return exprNode;
	}

	public void setExprNode(ExpressionNode exprNode) {
		this.exprNode = exprNode;
	}

	public int getNoOfDigitsAfterDecimal() {
		return noOfDigitsAfterDecimal;
	}

	public void setNoOfDigitsAfterDecimal(int noOfDigitsAfterDecimal) {
		this.noOfDigitsAfterDecimal = noOfDigitsAfterDecimal;
	}

	@Override
	public DataType getType() {
		return exprNode.getType();
	}

	@Override
	public ExpressionNode copy() {
		RoundOffNode node = new RoundOffNode();
		super.copy(this, node);
		node.setExprNode(exprNode.copy());
		node.setNoOfDigitsAfterDecimal(noOfDigitsAfterDecimal);
		return node;
	}

	@Override
	public String[] getFormNames() {
		return exprNode.getFormNames();
	}

	@Override
	public boolean isPhi() {
		return exprNode.isPhi();
	}

	@Override
	public Set<FieldNode> getFields() {
		return exprNode.getFields();
	}
}
