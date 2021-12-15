package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateFormatFuncNode extends ExpressionNode implements Serializable {
	private ExpressionNode dateExpr;

	private String format;

	@Override
	public DataType getType() {
		return DataType.STRING;
	}

	@Override
	public ExpressionNode copy() {
		DateFormatFuncNode copy = new DateFormatFuncNode();
		super.copy(this, copy);
		copy.setDateExpr(getDateExpr().copy());
		copy.setFormat(getFormat());
		return copy;
	}

	@Override
	public String[] getFormNames() {
		return getDateExpr().getFormNames();
	}

	@Override
	public boolean isPhi() {
		return getDateExpr().isPhi();
	}

	@Override
	public Set<FieldNode> getFields() {
		return getDateExpr().getFields();
	}

	public ExpressionNode getDateExpr() {
		return dateExpr;
	}

	public void setDateExpr(ExpressionNode dateExpr) {
		this.dateExpr = dateExpr;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DateFormatFuncNode that = (DateFormatFuncNode) o;
		if (getDateExpr() == null && that.getDateExpr() != null) {
			return false;
		} else if (getDateExpr() != null && !getDateExpr().equals(that.getDateExpr())) {
			return false;
		}

		return getFormat() != null ? getFormat().equals(that.getFormat()) : that.getFormat() == null;
	}

	@Override
	public int hashCode() {
		int result = getDateExpr() != null ? getDateExpr().hashCode() : 0;
		result = 31 * result + (getFormat() != null ? getFormat().hashCode() : 0);
		return result;
	}
}
