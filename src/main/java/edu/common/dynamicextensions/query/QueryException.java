package edu.common.dynamicextensions.query;

public class QueryException extends RuntimeException {
	public enum Code {
		CYCLES_IN_QUERY("Cyclic relations in query");

		private String message;

		private Code(String message) {
			this.message = message;
		}

		public String toString() {
			return name() + ": " + message;
		}
	}

	private Code error;

	public QueryException(Code error) {
		this.error = error;
	}

	public Code getErrorCode() {
		return error;
	}

	@Override
	public String getMessage() {
		return error.toString();
	}

	public static void throwCycleException() {
		throw new QueryException(Code.CYCLES_IN_QUERY);
	}
}