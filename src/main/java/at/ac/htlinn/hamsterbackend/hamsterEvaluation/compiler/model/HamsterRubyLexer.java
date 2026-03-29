package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.util.HashMap;

/**
 * Diese Klasse implementiert einen Lexer fuer Hamster-Ruby-Programme.
 */
// Ruby
public class HamsterRubyLexer extends HamsterLexer {

	public HamsterRubyLexer() {
		this.KEYWORDS = new String[] { "alias", "and", "BEGIN", "begin",
				"break", "case", "class", "def", "defined?", "do", "else",
				"elsif", "END", "end", "ensure", "false", "for", "if", "in",
				"module", "next", "nil", "not", "or", "redo", "rescue",
				"retry", "return", "self", "super", "then", "true", "undef",
				"unless", "until", "when", "while", "yield" };

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
		} else if (LL(0) == '=') {
			if (LL(1) == 'b' && LL(2) == 'e' && LL(3) == 'g' && LL(4) == 'i'
					&& LL(5) == 'n') {
				consumeMultiLineComment();
				type = COMMENT;
			} else {
				consumeCharacter();
			}
		} else if (LL(0) == '\"') {
			consumeStringLiteral();
			type = LITERAL;
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
		pos += 6;
		while ((LL(0) != '=' || LL(1) != 'e' || LL(2) != 'n' || LL(3) != 'd')
				&& LL(0) != (char) -1)
			pos++;
		if (LL(0) == '=' && LL(1) == 'e' && LL(2) == 'n' && LL(3) == 'd')
			pos += 3;
	}

}
