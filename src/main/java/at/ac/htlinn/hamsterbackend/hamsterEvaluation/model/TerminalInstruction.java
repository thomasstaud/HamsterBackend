package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

/**
 * Diese speziellen Hamster-Instruktionen stellen Terminal-Funktionen, also
 * Funktionen zur Ein- und Ausgabe bereit. Dabei wird immer zusaetzlich zu der
 * Hamster-ID aus HamsterInstruction noch einen Nachricht mit uebermittelt.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class TerminalInstruction extends HamsterInstruction implements
		Serializable {
	public static final int WRITE = 11;

	public static final int READ_STRING = 12;

	public static final int READ_INT = 13;

	/**
	 * Die Nachricht, die mit uebermittelt wird.
	 */
	protected String message;

	public TerminalInstruction(int type, int hamster, String message) {
		super(type, hamster);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		if (getType() == WRITE) {
			return "hamster.schreib" + "(\"" + message
					+ "\")";
		} else if (getType() == READ_INT) {
			return "hamster.liesZahl" + "(\"" + message
					+ "\")";
		} else if (getType() == READ_STRING) {
			return "hamster.liesZeichenkette" + "(\""
					+ message + "\")";
		}
		return super.toString();
	}
}