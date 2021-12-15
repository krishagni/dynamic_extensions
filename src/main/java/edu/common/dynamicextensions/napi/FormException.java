package edu.common.dynamicextensions.napi;

public class FormException extends RuntimeException {
	private String error;

	public FormException(String error) {
		super(error);
		this.error = error;
	}

	public FormException(String error, Throwable t) {
		super(error, t);
		this.error = error;
	}

	public String getError() {
		return error;
	}
}
