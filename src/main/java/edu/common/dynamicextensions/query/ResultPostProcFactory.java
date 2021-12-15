package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public interface ResultPostProcFactory {
	ResultPostProc create(QueryExpressionNode queryExpr, String timeZone);
}
