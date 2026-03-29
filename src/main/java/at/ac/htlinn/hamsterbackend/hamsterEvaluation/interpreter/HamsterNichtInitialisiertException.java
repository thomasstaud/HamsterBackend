package at.ac.htlinn.hamsterbackend.hamsterEvaluation.interpreter;
/**
 * Hamster-Exception die den Fehler repraesentiert, dass Befehle fuer einen zwar
 * erzeugten aber nicht initialisierten Hamster aufgerufen werden.
 * 
 * @author Dietrich Boles (Universitaet Oldenburg)
 * @version 1.0 (25.01.2006)
 * 
 */
public class HamsterNichtInitialisiertException extends HamsterException {

	/**
	 * Konstruktor, der die Exception mit dem Hamster initialisiert, der die
	 * Exception verschuldet hat.
	 * 
	 * @param hamster
	 *            der Hamster, der die Exception verschuldet hat
	 */
	public HamsterNichtInitialisiertException(Hamster hamster) {
		super(hamster);
	}

	/**
	 * liefert eine der Exception entsprechende Fehlermeldung
	 * 
	 * @return Fehlermeldung
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Der Hamster ist nicht initialisiert!";
	}
}
