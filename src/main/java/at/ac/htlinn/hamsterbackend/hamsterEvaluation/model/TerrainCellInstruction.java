package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

/**
 * Diese Klasse umfasst Instruktionen, die sich auf eine einzelne Kachel des
 * Territoriums beziehen. Dazu muss die Reihe und die Spalte der entsprechende
 * Kachel mit uebertragen werden.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class TerrainCellInstruction extends TerrainInstruction implements
		Serializable {
	public static final int IS_WALL = 1;

	public static final int COUNT_CORN = 2;

	public static final int COUNT_HAMSTER = 3;

	public static final int GET_HAMSTER = 4;

	/**
	 * Die Reihe der Kachel
	 */
	protected int row;

	/**
	 * Die Spalte der Kachel
	 */
	protected int col;

	public TerrainCellInstruction(int type, int row, int col) {
		super(type);
		this.row = row;
		this.col = col;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public String toString() {
		switch (getType()) {
		case IS_WALL:
			return "hamster.mauerDa" + "(" + row + ", "
					+ col + ")";
		case COUNT_CORN:
			return "hamster.getAnzahlKoerner" + "(" + row
					+ ", " + col + ")";
		case COUNT_HAMSTER:
			return "hamster.getAnzahlHamster" + "(" + row
					+ ", " + col + ")";
		case GET_HAMSTER:
			return "hamster.getHamster" + "(" + row + ", "
					+ col + ")";
		}
		return super.toString();
	}
}