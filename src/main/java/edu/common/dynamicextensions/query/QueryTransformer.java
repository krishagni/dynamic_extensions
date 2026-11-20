package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public interface QueryTransformer {
	QueryExpressionNode transform(QueryExpressionNode query);
}
