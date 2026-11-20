package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class ShallowWideRowGeneratorFactory implements WideRowGeneratorFactory {
	@Override
	public ShallowWideRowGenerator create(JoinTree joinTree, QueryExpressionNode queryExpr, WideRowMode mode) {
		return new ShallowWideRowGenerator(joinTree, queryExpr, mode);
	}
}
