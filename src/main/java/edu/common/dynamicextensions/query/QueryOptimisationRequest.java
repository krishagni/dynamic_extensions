package edu.common.dynamicextensions.query;

import javax.sql.DataSource;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class QueryOptimisationRequest {
	private String sql;

	private String originalSql;

	private QueryExpressionNode queryExpr;

	private JoinTree joinTree;

	private DataSource dataSource;

	private QueryOptimisationConfig config;

	public String sql() {
		return sql;
	}

	public QueryOptimisationRequest sql(String sql) {
		this.sql = sql;
		return this;
	}

	public String originalSql() {
		return originalSql;
	}

	public QueryOptimisationRequest originalSql(String originalSql) {
		this.originalSql = originalSql;
		return this;
	}

	public QueryExpressionNode queryExpr() {
		return queryExpr;
	}

	public QueryOptimisationRequest queryExpr(QueryExpressionNode queryExpr) {
		this.queryExpr = queryExpr;
		return this;
	}

	public JoinTree joinTree() {
		return joinTree;
	}

	public QueryOptimisationRequest joinTree(JoinTree joinTree) {
		this.joinTree = joinTree;
		return this;
	}

	public DataSource dataSource() {
		return dataSource;
	}

	public QueryOptimisationRequest dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public QueryOptimisationConfig config() {
		return config;
	}

	public QueryOptimisationRequest config(QueryOptimisationConfig config) {
		this.config = config;
		return this;
	}
}
