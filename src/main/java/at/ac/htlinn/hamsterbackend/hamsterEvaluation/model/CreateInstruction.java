package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

/**
 * Diese Klasse stellt den speziellen Befehl des Hamster-Erzeugens dar.
 * Zusaetzlich zu der Hamster-ID der Oberklasse HamsterInstruction werden noch
 * die Initialisierungsparameter mit in der Klasse gespeichert.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class CreateInstruction extends HamsterInstruction
		implements
			Serializable {
	/**
	 * Die Initialisierungsparameter
	 */
	protected int row, col, dir, mouth, color;

	public CreateInstruction(int id, int row, int col, int dir, int mouth, int c) {
		super(0, id);
		this.row = row;
		this.col = col;
		this.dir = dir;
		this.mouth = mouth;
		this.color = c;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getDir() {
		return dir;
	}

	public int getMouth() {
		return mouth;
	}
	public int getColor() {
		return color;
	}

	public String toString() {
		return "init(" + row + ", " + col + ", " + dir + ", " + mouth + ")";
	}
}