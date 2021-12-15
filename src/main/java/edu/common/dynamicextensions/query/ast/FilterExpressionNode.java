package edu.common.dynamicextensions.query.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterExpressionNode implements FilterNodeMarker {
	public enum Op {
		AND, OR, NOT, PAND, PARENTHESIS, IDENTITY, NTHCHILD;
	}

	private String aql;
	
	private List<FilterNodeMarker> operands = new ArrayList<FilterNodeMarker>();
	
	private Op operator;

    @Override
    public String getAql() {
        return aql;
    }

    public void setAql(String aql) {
        this.aql = aql;
    }

    public Op getOperator() {
        return operator;
    }

    public void setOperator(Op operator) {
        this.operator = operator;
    }

    public List<FilterNodeMarker> getOperands() {
        return operands;
    }

    public void setOperands(List<FilterNodeMarker> operands) {
        this.operands = operands;
    }

    public static FilterExpressionNode notExpr(FilterNodeMarker operand) {
        return makeExpression(Op.NOT, new FilterNodeMarker[] { operand });
    }

    public static FilterExpressionNode andExpr(FilterNodeMarker ... operands)  {
        return makeExpression(Op.AND, operands);
    }

    public static FilterExpressionNode orExpr(FilterNodeMarker ... operands)  {
        return makeExpression(Op.OR, operands);
    }

    public static FilterExpressionNode pAndExpr(FilterNodeMarker ... operands)  {
        return makeExpression(Op.PAND, operands);
    }

    public static FilterExpressionNode parenExpr(FilterNodeMarker operand)  {
        return makeExpression(Op.PARENTHESIS, new FilterNodeMarker[] {operand});
    }
    
    public static FilterExpressionNode nthChildExpr(FilterNodeMarker operand)  {
        return makeExpression(Op.NTHCHILD, new FilterNodeMarker[] {operand});
    }

    public static FilterExpressionNode identity(FilterNodeMarker operand) {
    	return makeExpression(Op.IDENTITY, new FilterNodeMarker[] {operand});
    }

    private static FilterExpressionNode makeExpression(Op op, FilterNodeMarker operands[])  {
        FilterExpressionNode expr = new FilterExpressionNode();
        expr.setOperator(op);
        expr.getOperands().addAll(Arrays.asList(operands));
        return expr;
    }    
}
