package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class AggregateNode extends ExpressionNode implements Serializable {	
	private static final long serialVersionUID = 2686666253832434643L;

	//
	// CCOUNT and CSUM is supported in plain mode that does not have
	// any post processing of results
	//
	public enum AGG_FN {
		COUNT, CCOUNT, SUM, CSUM, MIN, MAX, AVG
	};
	
	private AGG_FN aggFn;

	private boolean distinct;
	
	private FieldNode field;

	@Override
	public boolean isAggregateExpression() {
		return true;
	}	
	
	public AGG_FN getAggFn() {
		return aggFn;
	}

	public void setAggFn(AGG_FN aggFn) {
		this.aggFn = aggFn;
	}
	
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public FieldNode getField() {
		return field;
	}

	public void setField(FieldNode field) {
		this.field = field;
	}

	@Override
	public DataType getType() {		
		return DataType.INTEGER;
	}

	@Override
	public ExpressionNode copy() {
		AggregateNode copy = new AggregateNode();
		super.copy(this, copy);
		copy.setAggFn(getAggFn());
		copy.setDistinct(distinct);
		copy.setField(field.copy());
		return copy;
	}

	@Override
	public String[] getFormNames() {
		return field.getFormNames();
	}

	public boolean isCumulative() {
		return aggFn == AGG_FN.CCOUNT || aggFn == AGG_FN.CSUM;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggFn == null) ? 0 : (aggFn.ordinal() + 1));
		result = prime * result + (distinct ? 1231 : 1237);
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
			
		AggregateNode other = (AggregateNode) obj;
		if (aggFn == null) {
			if (other.aggFn != null) {
				return false;
			}			
		} else if (!aggFn.equals(other.aggFn)) {
			return false;
		}
		
		if (distinct != other.distinct) {
			return false;
		} 
		
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isPhi() {
		return field != null && field.isPhi();
	}

	@Override
	public Set<FieldNode> getFields() {
		return Collections.singleton(field);
	}	
}
