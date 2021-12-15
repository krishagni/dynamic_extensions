package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class CrosstabFactory implements ResultPostProcFactory {
	
	public static CrosstabFactory getInstance() {
		return new CrosstabFactory();
	}
	
	@Override
	public ResultPostProc create(QueryExpressionNode queryExpr, String timeZone) {
		return new Crosstab(queryExpr, timeZone);
	}
}
