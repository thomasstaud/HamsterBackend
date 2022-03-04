package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class BooleanFunction extends Function implements BooleanExpression {

	protected Block block;

	public BooleanFunction() {
		super();
	}

	public BooleanFunction(Block block) {
		super(block);
	}

	public BooleanFunction(Statement... statements) {
		super(statements);
	}

}
