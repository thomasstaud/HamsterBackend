package io.github.Hattinger04.hamster.visual.model;

public class VisualTest {

	public static void main(String[] args) {
		test1();
	}

	public static void test1() {

		// rechtsUm
		Procedure rechtsUm = new Procedure(new LinksUmCommand(),
				new LinksUmCommand(), new LinksUmCommand());

		// linksFrei
		Block lfTrueBlock = new Block(rechtsUm, new ReturnExpressionStatement(
				new TrueExpression()));
		Block lfFalseBlock = new Block(rechtsUm, new ReturnExpressionStatement(
				new FalseExpression()));
		BooleanFunction linksFrei = new BooleanFunction(new LinksUmCommand(),
				new IfStatement(new VornFreiFunction(), lfTrueBlock,
						lfFalseBlock));

		// zurMauer (rekursiv)
		Block zurMauerBlock = new Block();
		Procedure zurMauer = new Procedure(zurMauerBlock);
		Block zmTrueBlock = new Block(new VorCommand(), zurMauer);
		IfStatement ifR = new IfStatement(new VornFreiFunction(), zmTrueBlock);
		zurMauerBlock.add(ifR);

		// main
		Block mainBlock = new Block(linksFrei, zurMauer);
		Function main = new Function(mainBlock);
		main.perform();
	}

}
