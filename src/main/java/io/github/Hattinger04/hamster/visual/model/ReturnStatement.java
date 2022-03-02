package io.github.Hattinger04.hamster.visual.model;

public class ReturnStatement implements Statement {

	public ReturnStatement() {
	}

	@Override
	public Object perform() throws FunctionResultException {
		throw new FunctionResultException();
	}
}
