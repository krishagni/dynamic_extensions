package edu.common.dynamicextensions.query;

public class QueryOptimisationResult {
	public enum Status {
		OK,
		REJECT
	}

	private Status status;

	private String sql;

	private String reason;

	private QueryExplainPlan explainPlan;

	public static QueryOptimisationResult ok(String sql) {
		return new QueryOptimisationResult().status(Status.OK).sql(sql);
	}

	public static QueryOptimisationResult reject(String sql, QueryExplainPlan explainPlan, String reason) {
		return new QueryOptimisationResult().status(Status.REJECT).sql(sql).explainPlan(explainPlan).reason(reason);
	}

	public Status status() {
		return status;
	}

	public QueryOptimisationResult status(Status status) {
		this.status = status;
		return this;
	}

	public String sql() {
		return sql;
	}

	public QueryOptimisationResult sql(String sql) {
		this.sql = sql;
		return this;
	}

	public String reason() {
		return reason;
	}

	public QueryOptimisationResult reason(String reason) {
		this.reason = reason;
		return this;
	}

	public QueryExplainPlan explainPlan() {
		return explainPlan;
	}

	public QueryOptimisationResult explainPlan(QueryExplainPlan explainPlan) {
		this.explainPlan = explainPlan;
		return this;
	}
}
