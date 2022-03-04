package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class IfStatement implements Statement {

	protected BooleanExpression condition;
	protected Statement trueStatement;
	protected Statement falseStatement;

	public IfStatement(BooleanExpression cond, Statement trueS, Statement falseS) {
		this.condition = cond;
		this.trueStatement = trueS;
		this.falseStatement = falseS;
	}

	public IfStatement() {
		this(new TrueExpression(), new NullStatement(), new NullStatement());
	}

	public IfStatement(BooleanExpression cond, Statement trueS) {
		this(cond, trueS, new NullStatement());
	}

	public void setCondition(BooleanExpression expr) {
		this.condition = expr;
	}

	public void setTrueStatement(Statement statement) {
		this.trueStatement = statement;
	}

	public void setFalseStatement(Statement statement) {
		this.falseStatement = statement;
	}

	@Override
	public Object perform() {
		Boolean cond = (Boolean) this.condition.perform();
		if (cond) {
			return trueStatement.perform();
		} else {
			return falseStatement.perform();
		}
	}
}
