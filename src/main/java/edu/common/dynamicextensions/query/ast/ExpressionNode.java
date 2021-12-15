package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public abstract class ExpressionNode implements Node, Serializable {
	private static final long serialVersionUID = -4913352175312731795L;

	private int pos;
	
	private String label;
	
	private String columnAlias;

	private String aql;
	
	public abstract DataType getType();
	
	public abstract ExpressionNode copy();
	
	public abstract String[] getFormNames();
	
	public abstract boolean isPhi();
	
	public abstract Set<FieldNode> getFields();
	
	public void copy(ExpressionNode from, ExpressionNode to) {
		to.setPos(from.getPos());
		to.setLabel(from.getLabel());
		to.setColumnAlias(from.getColumnAlias());
		to.setAql(from.getAql());
	}
	
	public boolean isAggregateExpression() {
		return false;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}

	@Override
	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public boolean isDate() {
		return getType() == DataType.DATE;
	}
	
	public boolean isString() {
		return getType() == DataType.STRING;
	}
	
	public boolean isNumber() {
		return isFloat() || isInteger() || isBoolean();
	}
	
	public boolean isFloat() {
		return getType() == DataType.FLOAT;
	}
	
	public boolean isInteger() {
		return getType() == DataType.INTEGER;
	}
	
	public boolean isBoolean() {
		return getType() == DataType.BOOLEAN;
	}
	
	public boolean isDateInterval() {
		return getType() == DataType.DATE_INTERVAL;
	}
}
