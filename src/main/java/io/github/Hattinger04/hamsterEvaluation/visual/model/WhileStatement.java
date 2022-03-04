package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class WhileStatement implements Statement {

	protected BooleanExpression condition;
	protected Statement statement;

	public WhileStatement(BooleanExpression cond, Statement s) {
		this.condition = cond;
		this.statement = s;
	}

	public WhileStatement() {
		this(new FalseExpression(), new NullStatement());
	}

	public void setCondition(BooleanExpression expr) {
		this.condition = expr;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	@Override
	public Object perform() {
		Boolean cond = (Boolean) this.condition.perform();
		while (cond) {
			statement.perform();
		}
		return null;
	}
}
