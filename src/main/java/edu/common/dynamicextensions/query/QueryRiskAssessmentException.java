package edu.common.dynamicextensions.query;

public class QueryRiskAssessmentException extends RuntimeException {
	public QueryRiskAssessmentException(String message) {
		super(message);
	}

	public QueryRiskAssessmentException(String message, Throwable t) {
		super(message, t);
	}
}
