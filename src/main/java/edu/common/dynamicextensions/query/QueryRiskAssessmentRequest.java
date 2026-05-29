package edu.common.dynamicextensions.query;

import javax.sql.DataSource;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class QueryRiskAssessmentRequest {
	private String sql;

	private QueryExpressionNode queryExpr;

	private JoinTree joinTree;

	private DataSource dataSource;

	private QueryRiskAssessmentConfig config;

	public String sql() {
		return sql;
	}

	public QueryRiskAssessmentRequest sql(String sql) {
		this.sql = sql;
		return this;
	}

	public QueryExpressionNode queryExpr() {
		return queryExpr;
	}

	public QueryRiskAssessmentRequest queryExpr(QueryExpressionNode queryExpr) {
		this.queryExpr = queryExpr;
		return this;
	}

	public JoinTree joinTree() {
		return joinTree;
	}

	public QueryRiskAssessmentRequest joinTree(JoinTree joinTree) {
		this.joinTree = joinTree;
		return this;
	}

	public DataSource dataSource() {
		return dataSource;
	}

	public QueryRiskAssessmentRequest dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public QueryRiskAssessmentConfig config() {
		return config;
	}

	public QueryRiskAssessmentRequest config(QueryRiskAssessmentConfig config) {
		this.config = config;
		return this;
	}
}
