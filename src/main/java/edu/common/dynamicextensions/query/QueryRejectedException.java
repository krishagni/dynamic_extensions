package edu.common.dynamicextensions.query;

public class QueryRejectedException extends RuntimeException {
	private String aql;

	private String sql;

	private QueryExplainPlan explainPlan;

	private QueryRiskAssessmentMetrics metrics;

	private String reason;

	public QueryRejectedException(String aql, String sql, QueryExplainPlan explainPlan, String reason) {
		super("Query rejected by risk assessor: " + reason);

		this.aql = aql;
		this.sql = sql;
		this.explainPlan = explainPlan;
		this.metrics = QueryRiskAssessmentMetrics.from(explainPlan);
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

	public QueryRiskAssessmentMetrics metrics() {
		return metrics;
	}

	public String reason() {
		return reason;
	}
}
