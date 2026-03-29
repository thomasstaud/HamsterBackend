package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.util.HashMap;

/**
 * Diese Klasse implementiert einen Lexer fuer Hamsterprogramme. Dieser
 * unterscheidet zwischen normalem Text, Kommentaren, Literalen,
 * Schluesselwoertern und Leerzeichen.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterLexer {
	/**
	 * Diese Schluesselwoerter kann der Lexer erkennen.
	 */
	// Prolog
	// Andreas: musste das 'final' wegnehmen, da die Schl�sselw�rtersich  bei der 
	// Vererbung (beispielsweise beim HamsterPrologLexer) nicht umdefinieren lassen. 
	public String[] KEYWORDS = {"abstract", "double", "int",
			"strictfp", "boolean", "else", "interface", "super", "break",
			"extends", "long", "switch", "byte", "final", "native",
			"synchronized", "case", "finally", "new", "this", "catch", "float",
			"package", "throw", "char", "for", "private", "throws", "class",
			"goto", "protected", "transient", "const", "if", "public", "try",
			"continue", "implements", "return", "void", "default", "import",
			"short", "volatile", "do", "instanceof", "static", "while"};

	/**
	 * Die Schluesselwoerter werden aus Performanzgruenden in einen HashMap
	 * gepackt.
	 */
	protected HashMap keywords;

	public static final int PLAIN = 0;
	public static final int COMMENT = 1;
	public static final int LITERAL = 2;
	public static final int KEYWORD = 3;
	public static final int WHITESPACE = 4;

	/**
	 * Der zu erkennende Text.
	 */
	protected String text;

	/**
	 * Die aktuelle Position im Text.
	 */
	protected int pos, off;

	/**
	 * Diese Methode packt die Schluesselwoerter in die HashMap.
	 */
	public void init() {
		if (keywords != null)
			return;
		keywords = new HashMap();
		for (int i = 0; i < KEYWORDS.length; i++)
			keywords.put(KEYWORDS[i], KEYWORDS[i]);
	}

	/**
	 * Der Konstruktor des HamsterLexers. Sorgt dafuer, dass init() aufgerufen
	 * wird.
	 */
	public HamsterLexer() {
		init();
	}

	/**
	 * Initialisiert den Lexer mit einem neuen Text und setzt gleichzeitig
	 * Position und Offset.
	 * 
	 * @param pos
	 *            Die neue Position
	 * @param offset
	 *            Der neue Offset
	 * @param text
	 *            Der neue Text
	 */
	public void init(int pos, int offset, String text) {
		this.pos = pos;
		this.off = offset;
		this.text = text;
	}

	/**
	 * Die LL(k) Methode eines Lexers.
	 * 
	 * @param k
	 *            lookahead
	 * @return Das Symbol im Abstand k
	 */
	protected char LL(int k) {
		if (pos + k < text.length()) {
			return text.charAt(pos + k);
		} else {
			return (char) -1;
		}
	}

	/**
	 * Erkennt ein Leerzeichen
	 */
	protected void consumeWhiteSpace() {
		pos++;
	}

	/**
	 * Erkennt einen Bezeichner
	 */
	protected void consumeIdentifier() {
		pos++;
		while (Character.isJavaIdentifierPart(LL(0)))
			pos++;
	}

	/**
	 * Erkennt einen einzeiligen Kommentar
	 */
	protected void consumeSingleLineComment() {
		pos += 2;
		while (LL(0) != '\n' && LL(0) != '\r' && LL(0) != (char) -1)
			pos++;
		if (LL(0) == '\r' && LL(1) == '\n')
			pos += 2;
		else if (LL(0) == '\n')
			pos++;
		else if (LL(0) == '\r')
			pos++;
	}

	protected void consumeMultiLineComment() {
		pos += 2;
		while ((LL(0) != '*' || LL(1) != '/') && LL(0) != (char) -1)
			pos++;
		if (LL(0) == '*' && LL(1) == '/')
			pos += 2;
	}

	/**
	 * Erkennt ein einzelnes Zeichen
	 */
	protected void consumeCharacter() {
		pos++;
	}

	/**
	 * Erkennt einen String-Literal
	 */
	protected void consumeStringLiteral() {
		pos++;
		while (LL(0) != (char) -1 && LL(0) != '\"' && LL(0) != '\r'
				&& LL(0) != '\n') {
			if (LL(0) == '\\' && LL(1) == '\"')
				pos++;
			pos++;
		}
		if (LL(0) == '\r' && LL(1) == '\n')
			pos += 2;
		else if (LL(0) == '\n' || LL(0) == '\r')
			pos++;
		else if (LL(0) == '\"')
			pos++;
	}

	/**
	 * Erkennt einen Character-Literal
	 */
	protected void consumeCharacterLiteral() {
		pos++;
		while (LL(0) != (char) -1 && LL(0) != '\'' && LL(0) != '\r'
				&& LL(0) != '\n') {
			if (LL(0) == '\\' && LL(1) == '\'')
				pos++;
			pos++;
		}
		if (LL(0) == '\r' && LL(1) == '\n')
			pos += 2;
		else if (LL(0) == '\n' || LL(0) == '\r')
			pos++;
		else if (LL(0) == '\'')
			pos++;
	}

	/**
	 * Erkennt das naechste Token im Text
	 * 
	 * @return Das naechste Token
	 */
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
		} else if (LL(0) == '/') {
			if (LL(1) == '/') {
				consumeSingleLineComment();
				type = COMMENT;
			} else if (LL(1) == '*') {
				consumeMultiLineComment();
				type = COMMENT;
			} else
				consumeCharacter();
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

	/**
	 * Gibt an, ob noch weitere Token existieren.
	 * 
	 * @return true, wenn der Lexer noch nicht am Ende des Textes ist.
	 */
	public boolean ready() {
		return pos < text.length();
	}
}