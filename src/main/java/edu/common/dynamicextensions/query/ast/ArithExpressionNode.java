package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class ArithExpressionNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = -274162789478308346L;

	public static enum ArithOp {
		PLUS("+"), MINUS("-"), MUL("*"), DIV("/");
		
        private String symbol;
        
        private ArithOp(String symbol) {
            this.symbol = symbol;  
        }
        
        public String symbol() {
            return symbol;
        }
        
        public static ArithOp getBySymbol(String symbol) {
            ArithOp result = null;
            for (ArithOp op : ArithOp.values()) {
                if (op.symbol.equals(symbol)) {
                    result = op;
                    break;
                }
            }
            
            return result;
        }                
	};
	
	private ExpressionNode leftOperand;
	
	private ExpressionNode rightOperand;
	
	private ArithOp op;

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

	public ArithOp getOp() {
		return op;
	}

	public void setOp(ArithOp op) {
		this.op = op;
	}

	@Override
	public DataType getType() {
		DataType result = null;
		if (leftOperand.isNumber() && rightOperand.isNumber()) {
			result = (leftOperand.isFloat() || rightOperand.isFloat()) ? DataType.FLOAT : DataType.INTEGER;
		} else if (op == ArithOp.PLUS && leftOperand.isDateInterval() && rightOperand.isDate()) {
			result = DataType.DATE;
		} else if (op == ArithOp.PLUS && leftOperand.isDate() && rightOperand.isDateInterval()) {
			result = DataType.DATE;
		} else if (op == ArithOp.MINUS && leftOperand.isDate() && rightOperand.isDateInterval()) {
			result = DataType.DATE;
		} else if (op == ArithOp.MINUS && leftOperand.isDate() && rightOperand.isDate()) {
			result = DataType.FLOAT;
		} else if (op == ArithOp.MINUS && leftOperand.isDate() && rightOperand.isString()) {
			result = DataType.FLOAT;
		} else if (op == ArithOp.MINUS && leftOperand.isString() && rightOperand.isDate()) {
			result = DataType.FLOAT;
		}

		return result;
	}		
	
	public DataType getLeftOperandCoercion() {
		return leftOperand.isString() && rightOperand.isDate() ? DataType.DATE : null;
	}
	
	public DataType getRightOperandCoercion() {
		return leftOperand.isDate() && rightOperand.isString() ? DataType.DATE : null; 
	}
	
	@Override
	public ArithExpressionNode copy() {
		ArithExpressionNode copy = new ArithExpressionNode();
		super.copy(this, copy);
		copy.setOp(this.op);
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
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((leftOperand == null) ? 0 : leftOperand.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
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
		
		ArithExpressionNode other = (ArithExpressionNode) obj;
		if (leftOperand == null && other.leftOperand != null) {
			return false;
		} else if (!leftOperand.equals(other.leftOperand)) {
			return false;
	    }
		
		if (op != other.op) {
			return false;
		}
		
		if (rightOperand == null && other.rightOperand != null) {
			return false;
		} else if (!rightOperand.equals(other.rightOperand)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isPhi() {
		return (leftOperand != null && leftOperand.isPhi()) || (rightOperand != null && rightOperand.isPhi());
	}
	
	@Override
	public Set<FieldNode> getFields() {
		Set<FieldNode> fields = new HashSet<FieldNode>();
		fields.addAll(leftOperand.getFields());
		if (rightOperand != null) {
			fields.addAll(rightOperand.getFields());
		}
		
		return fields;
	}	
}
