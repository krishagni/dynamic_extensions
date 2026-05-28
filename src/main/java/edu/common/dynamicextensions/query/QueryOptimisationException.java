package edu.common.dynamicextensions.query;

public class QueryOptimisationException extends RuntimeException {
	public QueryOptimisationException(String message) {
		super(message);
	}

	public QueryOptimisationException(String message, Throwable t) {
		super(message, t);
	}
}
