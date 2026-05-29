package edu.common.dynamicextensions.query;

public class QueryRiskAssessmentResult {
	public enum Status {
		OK,
		REJECT
	}

	private Status status;

	private String sql;

	private String reason;

	private QueryExplainPlan explainPlan;

	public static QueryRiskAssessmentResult ok(String sql) {
		return new QueryRiskAssessmentResult().status(Status.OK).sql(sql);
	}

	public static QueryRiskAssessmentResult reject(String sql, QueryExplainPlan explainPlan, String reason) {
		return new QueryRiskAssessmentResult().status(Status.REJECT).sql(sql).explainPlan(explainPlan).reason(reason);
	}

	public Status status() {
		return status;
	}

	public QueryRiskAssessmentResult status(Status status) {
		this.status = status;
		return this;
	}

	public String sql() {
		return sql;
	}

	public QueryRiskAssessmentResult sql(String sql) {
		this.sql = sql;
		return this;
	}

	public String reason() {
		return reason;
	}

	public QueryRiskAssessmentResult reason(String reason) {
		this.reason = reason;
		return this;
	}

	public QueryExplainPlan explainPlan() {
		return explainPlan;
	}

	public QueryRiskAssessmentResult explainPlan(QueryExplainPlan explainPlan) {
		this.explainPlan = explainPlan;
		return this;
	}
}
