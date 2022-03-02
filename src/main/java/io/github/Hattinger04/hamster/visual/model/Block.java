package io.github.Hattinger04.hamster.visual.model;

import java.util.ArrayList;

public class Block extends ArrayList<Statement> implements Statement {

	public Block() {
		super();
	}
	
	public Block(Statement... statements) {
		super();
		for (Statement statement : statements)  {
			add(statement);
		}
	}

	@Override
	public Object perform() {
		for (int s = 0; s < this.size(); s++) {
			get(s).perform();
		}
		return null;
	}

}
