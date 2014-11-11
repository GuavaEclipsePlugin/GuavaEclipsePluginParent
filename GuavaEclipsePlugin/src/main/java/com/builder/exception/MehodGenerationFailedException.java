package com.builder.exception;

public class MehodGenerationFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String cause;

	public MehodGenerationFailedException(String cause) {
		this.cause = null;
		this.cause = cause;
	}

	public String getReason() {
		return cause;
	}

}
