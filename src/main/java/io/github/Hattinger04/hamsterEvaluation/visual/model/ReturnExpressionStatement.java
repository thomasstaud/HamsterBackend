package io.github.Hattinger04.hamsterEvaluation.visual.model;

public class ReturnExpressionStatement extends ReturnStatement {

	protected Expression expression;

	public ReturnExpressionStatement() {
		this(new FalseExpression());
	}

	public ReturnExpressionStatement(Expression expr) {
		this.expression = expr;
	}

	public void setExpression(Expression expr) {
		this.expression = expr;
	}

	public Expression getExpression() {
		return this.expression;
	}

	@Override
	public Object perform() throws FunctionResultException {
		throw new FunctionResultException(this.expression.perform());
	}
}
