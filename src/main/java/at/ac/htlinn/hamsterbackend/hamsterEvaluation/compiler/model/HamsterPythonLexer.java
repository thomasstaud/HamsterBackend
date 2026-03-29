package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.util.HashMap;

/**
 * Diese Klasse implementiert einen Lexer fuer Hamster-Python-Programme.
 */
// Python
public class HamsterPythonLexer extends HamsterLexer {

	public HamsterPythonLexer() {
		this.KEYWORDS = new String[] { "and", "del", "from", "not", "while",
				"as", "elif", "global", "or", "with", "assert", "else", "if",
				"pass", "yield", "break", "except", "import", "print", "class",
				"exec", "in", "raise", "continue", "finally", "is", "return",
				"def", "for", "lambda", "try" };

		// Rufe wiederholt die Initialisierungsmethode auf.
		keywords = new HashMap();
		for (int i = 0; i < KEYWORDS.length; i++)
			keywords.put(KEYWORDS[i], KEYWORDS[i]);
	}

	/**
	 * Erkennt das naechste Token im Text
	 * 
	 * @return Das naechste Token
	 */
	@Override
	public JavaToken nextToken() {
		if (!ready())
			return null;
		int oldPos = this.pos;
		int type = PLAIN;
		if (LL(0) == ' ' || LL(0) == '\t' || LL(0) == '\n' || LL(0) == '\r') {
			consumeWhiteSpace();
			type = WHITESPACE;
		} else if (Character.isJavaIdentifierStart(LL(0))) {
			consumeIdentifier();
		} else if (LL(0) == '#') {
			consumeSingleLineComment();
			type = COMMENT;
		} else if (LL(0) == '\"') {
			if (LL(1) == '\"' && LL(2) == '\"') {
				consumeMultiLineComment();
				type = COMMENT;
			} else {
				consumeStringLiteral();
				type = LITERAL;
			}
		} else if (LL(0) == '\'') {
			consumeCharacterLiteral();
			type = LITERAL;
		} else {
			consumeCharacter();
		}
		String t = text.substring(oldPos, pos);
		if (type == PLAIN) {
			if (keywords.get(t) != null)
				type = KEYWORD;
		}
		return new JavaToken(t, off + oldPos, type);
	}

	@Override
	protected void consumeSingleLineComment() {
		pos += 1;
		while (LL(0) != '\n' && LL(0) != '\r' && LL(0) != (char) -1)
			pos++;
		if (LL(0) == '\r' && LL(1) == '\n')
			pos += 2;
		else if (LL(0) == '\n')
			pos++;
		else if (LL(0) == '\r')
			pos++;
	}

	@Override
	protected void consumeMultiLineComment() {
		pos += 3;
		while ((LL(0) != '\"' || LL(1) != '\"' || LL(2) != '\"')
				&& LL(0) != (char) -1)
			pos++;
		if (LL(0) == '\"' && LL(1) == '\"' && LL(2) == '\"')
			pos += 3;
	}
}
