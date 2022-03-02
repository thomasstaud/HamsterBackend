package io.github.Hattinger04.hamster.visual.model;

public class VornFreiFunction extends HamsterFunction {

	@Override
	public Object perform() {
		System.out.println("vornFrei()");
		return hamster.vornFrei();
	}
}
