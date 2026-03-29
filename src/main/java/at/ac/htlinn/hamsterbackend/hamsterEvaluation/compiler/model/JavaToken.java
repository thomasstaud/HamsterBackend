package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

/**
 * @author Daniel
 */
public class JavaToken {
	String text;
	int start;
	int type;

	public JavaToken(String text, int start, int type) {
		this.text = text;
		this.start = start;
		this.type = type;
	}

	public String toString() {
		return start + ":" + type + ":" + text;
	}

	/**
	 * @return Returns the start.
	 */
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	public boolean isComment() {
		return type == HamsterLexer.COMMENT;
	}
	public boolean isWhiteSpace() {
		return type == HamsterLexer.WHITESPACE;
	}
}
