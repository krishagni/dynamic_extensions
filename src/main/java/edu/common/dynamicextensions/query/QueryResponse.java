package edu.common.dynamicextensions.query;

import java.util.Date;

import edu.common.dynamicextensions.query.QueryResultData;

public class QueryResponse {
	private String sql;
	
	private Date timeOfExecution;
	
	private Long executionTime;
	
	private Long postExecutionTime;
	
	private QueryResultData resultData;

	public QueryResponse(){
		
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Date getTimeOfExecution() {
		return timeOfExecution;
	}

	public void setTimeOfExecution(Date timeOfExecution) {
		this.timeOfExecution = timeOfExecution;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public Long getPostExecutionTime() {
		return postExecutionTime;
	}

	public void setPostExecutionTime(Long postExecutionTime) {
		this.postExecutionTime = postExecutionTime;
	}

	public QueryResultData getResultData() {
		return resultData;
	}

	public void setResultData(QueryResultData resultData) {
		this.resultData = resultData;
	}
}
