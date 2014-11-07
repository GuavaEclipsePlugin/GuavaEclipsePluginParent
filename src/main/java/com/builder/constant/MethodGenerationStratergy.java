package com.builder.constant;

public enum MethodGenerationStratergy {

	USE_SUPER("UseSuper", 0), 
	DONT_USE_SUPER("DontUseSuper", 1), 
	SMART_OPTION("SmartOption", 2);

	private final int pos;
	private final String method;

	private MethodGenerationStratergy(String method, int pos) {
		this.pos = pos;
		this.method = method;
	}

	public int getPos() {
		return pos;
	}

	public String getMethod() {
		return method;
	}

}
