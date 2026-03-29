package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

/**
 * Diese Instruktions-Klasse umfasst alle Befehle, die von einem Hamster
 * ausgefuehrt werden koennen, ohne dass dazu weitere Parameter notwenig sind.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterInstruction extends Instruction implements Serializable {
	public static final int FORWARD = 1;

	public static final int TURN_LEFT = 2;

	public static final int LAY_DOWN = 3;

	public static final int PICK_UP = 4;

	public static final int FREE = 5;

	public static final int CORN_AVAILABLE = 6;

	public static final int MOUTH_EMPTY = 7;

	public static final int GET_ROW = 8;

	public static final int GET_COL = 9;

	public static final int GET_DIR = 10;

	public static final int GET_MOUTH = 11;

	public static final int GET_DATA = 12;

	/**
	 * Dieses Attribut gibt an, welcher Hamster den Befehl ausgefuehrt hat.
	 */
	protected int hamster;

	public HamsterInstruction(int type, int hamster) {
		super(type);
		this.hamster = hamster;
	}

	public int getHamster() {
		return hamster;
	}

	public String toString() {
		switch (getType()) {
		case FORWARD:
			return "hamster.vor" + "()";
		case TURN_LEFT:
			return "hamster.linksUm" + "()";
		case LAY_DOWN:
			return "hamster.gib" + "()";
		case PICK_UP:
			return "hamster.nimm" + "()";
		case FREE:
			return "hamster.vornFrei" + "()";
		case CORN_AVAILABLE:
			return "hamster.kornDa" + "()";
		case MOUTH_EMPTY:
			return "hamster.maulLeer" + "()";
		case GET_ROW:
			return "hamster.getReihe" + "()";
		case GET_COL:
			return "hamster.getSpalte" + "()";
		case GET_DIR:
			return "hamster.getBlickrichtung" + "()";
		case GET_MOUTH:
			return "hamster.getAnzahlKoerner" + "()";

		case GET_DATA:
			return "";
		}
		return super.toString();
	}
}