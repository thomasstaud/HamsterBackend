package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.util.HashMap;

/**
 * Diese Klasse implementiert einen Lexer fuer Hamster-Scheme-Programme. 
 */
public class HamsterSchemeLexer extends HamsterLexer {
		
	public HamsterSchemeLexer() 
	{
		this.KEYWORDS = new String[]{"define", 
			"cdr", "car", "if", "cond", "cons"};
		
		// Rufe wiederholt die Initialisierungsmethode auf. 
		keywords = new HashMap();
		for (int i = 0; i < KEYWORDS.length; i++)
			keywords.put(KEYWORDS[i], KEYWORDS[i]);
	}
}
