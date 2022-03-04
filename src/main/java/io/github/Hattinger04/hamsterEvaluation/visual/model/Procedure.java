package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class Procedure extends Function {

	protected Block block;

	public Procedure() {
		super();
	}

	public Procedure(Block block) {
		super(block);
	}

	public Procedure(Statement... statements) {
		super(statements);
	}

}
