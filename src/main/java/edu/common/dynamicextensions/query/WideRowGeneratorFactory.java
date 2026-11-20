package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public interface WideRowGeneratorFactory {
	ShallowWideRowGenerator create(JoinTree joinTree, QueryExpressionNode queryExpr, WideRowMode mode);
}
