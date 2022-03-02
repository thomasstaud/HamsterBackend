package io.github.Hattinger04.hamster.visual.model;

import io.github.Hattinger04.hamster.interpreter.Hamster;

public class VisualHamster extends Hamster {

	public static VisualHamster hamster = new VisualHamster();

	private VisualHamster() {
		super(true);
	}
	
	public VisualHamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner) {
		super(reihe, spalte, blickrichtung, anzahlKoerner);
	}
	
	public VisualHamster(Hamster hamster) {
		super(hamster);
	}
}
