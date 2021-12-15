package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderExprListNode implements Node, Serializable {
	private static final long serialVersionUID = -6216319287644413822L;

	private String aql;

	private List<OrderExprNode> exprs = new ArrayList<>();

	@Override
	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public List<OrderExprNode> getExprs() {
		return exprs;
	}

	public void setExprs(List<OrderExprNode> exprs) {
		this.exprs = exprs;
	}
}
