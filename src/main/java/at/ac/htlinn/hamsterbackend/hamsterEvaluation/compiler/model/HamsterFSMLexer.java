package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.util.HashMap;

/**
 * Diese Klasse implementiert einen Lexer fuer Hamster-FSM-Programme.
 */
// FSM
public class HamsterFSMLexer extends HamsterLexer {

	public HamsterFSMLexer() {
		this.KEYWORDS = new String[] { "and" };

		// Rufe wiederholt die Initialisierungsmethode auf.
		keywords = new HashMap();
		for (int i = 0; i < KEYWORDS.length; i++)
			keywords.put(KEYWORDS[i], KEYWORDS[i]);
	}
}
