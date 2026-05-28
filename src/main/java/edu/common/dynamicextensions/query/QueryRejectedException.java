package edu.common.dynamicextensions.query;

public class QueryRejectedException extends RuntimeException {
	private String aql;

	private String sql;

	private QueryExplainPlan explainPlan;

	private String reason;

	public QueryRejectedException(String aql, String sql, QueryExplainPlan explainPlan, String reason) {
		super("Query rejected by optimiser: " + reason);

		this.aql = aql;
		this.sql = sql;
		this.explainPlan = explainPlan;
		this.reason = reason;
	}

	public String aql() {
		return aql;
	}

	public String sql() {
		return sql;
	}

	public QueryExplainPlan explainPlan() {
		return explainPlan;
	}

	public String explainPlanText() {
		return explainPlan != null ? explainPlan.rawPlanText() : null;
	}

	public String reason() {
		return reason;
	}
}
