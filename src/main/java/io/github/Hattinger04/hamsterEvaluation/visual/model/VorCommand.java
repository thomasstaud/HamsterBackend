package io.github.Hattinger04.hamsterEvaluation.visual.model;

import io.github.Hattinger04.hamsterEvaluation.interpreter.Hamster;

public class VorCommand extends HamsterCommand {

	@Override
	public Object perform() {
		Hamster p = new Hamster(hamster);
		p.linksUm();
		hamster.vor();
		System.out.println("vor();");
		return null;
	}
}
