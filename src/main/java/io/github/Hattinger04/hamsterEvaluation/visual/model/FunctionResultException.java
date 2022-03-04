package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class FunctionResultException extends RuntimeException {

	protected Object result;

	public FunctionResultException() {
		this(null);
	}

	public FunctionResultException(Object result) {
		this.result = result;
	}

	public Object getReturnValue() {
		return result;
	}

	public void setReturnValue(Object result) {
		this.result = result;
	}
}
