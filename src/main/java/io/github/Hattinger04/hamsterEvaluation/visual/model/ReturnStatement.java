package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class ReturnStatement implements Statement {

	public ReturnStatement() {
	}

	@Override
	public Object perform() throws FunctionResultException {
		throw new FunctionResultException();
	}
}
