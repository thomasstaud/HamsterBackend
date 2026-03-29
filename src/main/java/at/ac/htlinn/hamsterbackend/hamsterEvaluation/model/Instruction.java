package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

/**
 * Diese Klasse stellt die Oberklasse aller Instruktionen dar.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class Instruction implements Serializable {
	/**
	 * Eine Typ-ID. Wird von manchen Unterklassen benutzt, die verschiedene
	 * gleichartige Instruktion umfassen.
	 */
	protected int type;

	/**
	 * Der Konstruktor
	 * 
	 * @param type
	 *            Der Typ
	 */
	public Instruction(int type) {
		this.type = type;
	}

	/**
	 * Liefert den Typ
	 * 
	 * @return Der Typ
	 */
	public int getType() {
		return type;
	}
}