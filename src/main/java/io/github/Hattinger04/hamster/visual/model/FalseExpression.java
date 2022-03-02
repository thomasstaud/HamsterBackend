package io.github.Hattinger04.hamster.visual.model;

public class FalseExpression implements BooleanExpression {

	@Override
	public Object perform() {
		return false;
	}
}
