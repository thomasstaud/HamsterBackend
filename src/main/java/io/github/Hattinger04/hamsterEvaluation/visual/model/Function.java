package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class Function implements Expression {

	protected Block block;

	public Function() {
		super();
	}

	public Function(Block block) {
		this.block = block;
		if (block == null) {
			this.block = new Block();
		}
	}
	
	public Function(Statement... statements) {
		this.block = new Block(statements);
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	@Override
	public Object perform() {
		try {
			this.block.perform();
		} catch (FunctionResultException result) {
			return result.getReturnValue();
		}
		return null;
	}

}
