package io.github.Hattinger04.hamster.visual.model;

import io.github.Hattinger04.hamster.interpreter.Hamster;

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
