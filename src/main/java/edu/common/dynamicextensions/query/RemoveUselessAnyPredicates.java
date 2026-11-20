package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.FilterExpressionNode;
import edu.common.dynamicextensions.query.ast.FilterNode;
import edu.common.dynamicextensions.query.ast.FilterNodeMarker;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class RemoveUselessAnyPredicates implements QueryTransformer {
	@Override
	public QueryExpressionNode transform(QueryExpressionNode query) {
		FilterNodeMarker filter = transform(query.getFilterExpr());
		query.setFilterExpr(
			filter == null || filter instanceof FilterExpressionNode
				? (FilterExpressionNode) filter
				: FilterExpressionNode.identity(filter)
		);
		return query;
	}

	private FilterNodeMarker transform(FilterNodeMarker marker) {
		if (marker == null) {
			return null;
		}

		if (marker instanceof FilterNode) {
			FilterNode filter = (FilterNode) marker;
			return filter.getRelOp() == FilterNode.RelationalOp.ANY ? null : filter;
		}

		FilterExpressionNode expression = (FilterExpressionNode) marker;
		if (expression.getOperator() == FilterExpressionNode.Op.AND) {
			FilterNodeMarker lhs = transform(expression.getOperands().get(0));
			FilterNodeMarker rhs = transform(expression.getOperands().get(1));
			return lhs == null ? rhs : rhs == null ? lhs : FilterExpressionNode.andExpr(lhs, rhs);
		} else if (expression.getOperator() == FilterExpressionNode.Op.OR) {
			FilterNodeMarker lhs = transform(expression.getOperands().get(0));
			FilterNodeMarker rhs = transform(expression.getOperands().get(1));
			return lhs == null || rhs == null ? null : FilterExpressionNode.orExpr(lhs, rhs);
		} else if (expression.getOperator() == FilterExpressionNode.Op.IDENTITY) {
			return transform(expression.getOperands().get(0));
		} else if (expression.getOperator() == FilterExpressionNode.Op.PARENTHESIS) {
			FilterNodeMarker operand = transform(expression.getOperands().get(0));
			return operand != null ? FilterExpressionNode.parenExpr(operand) : null;
		}

		return expression;
	}
}
