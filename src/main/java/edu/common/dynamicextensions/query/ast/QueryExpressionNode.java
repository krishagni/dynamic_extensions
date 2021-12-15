package edu.common.dynamicextensions.query.ast;


public class QueryExpressionNode implements Node {
	private String aql;

	private SelectListNode selectList;
	
	private FilterExpressionNode filterExpr;

	private FilterExpressionNode havingExpr;

	private OrderExprListNode orderExpr;
	
	private LimitExprNode limitExpr;
	
	private CrosstabNode crosstabSpec;
	
	private ResultPostProcNode resultPostProc;

	@Override
	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public SelectListNode getSelectList() {
		return selectList;
	}

	public void setSelectList(SelectListNode selectList) {
		this.selectList = selectList;
	}

	public FilterExpressionNode getFilterExpr() {
		return filterExpr;
	}

	public void setFilterExpr(FilterExpressionNode filterExpr) {
		this.filterExpr = filterExpr;
	}

	public FilterExpressionNode getHavingExpr() {
		return havingExpr;
	}

	public void setHavingExpr(FilterExpressionNode havingExpr) {
		this.havingExpr = havingExpr;
	}

	public OrderExprListNode getOrderExpr() {
		return orderExpr;
	}

	public void setOrderExpr(OrderExprListNode orderExpr) {
		this.orderExpr = orderExpr;
	}

	public LimitExprNode getLimitExpr() {
		return limitExpr;
	}

	public void setLimitExpr(LimitExprNode limitExpr) {
		this.limitExpr = limitExpr;
	}
	
	public CrosstabNode getCrosstabSpec() {
		return crosstabSpec;
	}

	public void setCrosstabSpec(CrosstabNode crosstabSpec) {
		this.crosstabSpec = crosstabSpec;
	}
	
	public ResultPostProcNode getResultPostProc() {
		return resultPostProc;
	}
	
	public void setResultPostProc(ResultPostProcNode resultPostProc) {
		this.resultPostProc = resultPostProc;
	}

	public String getResultPostProcName() {
		if (crosstabSpec != null) {
			return crosstabSpec.getName();
		} else if (resultPostProc != null) {
			return resultPostProc.getName();
		} else {
			return null;
		}
	}

	public boolean isAggregateQuery() {
		return selectList.hasAggregateExpr() || havingExpr != null;
	}
	
	public boolean hasResultPostProc() {
		return crosstabSpec != null || resultPostProc != null;
	}
}
