package io.github.Hattinger04.hamsterEvaluation.visual.model;

public abstract class HamsterFunction implements BooleanExpression {

	protected VisualHamster hamster = VisualHamster.hamster;

	public abstract Object perform();
}
