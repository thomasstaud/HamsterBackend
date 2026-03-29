package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.util.HashMap;


/**
 * Diese Klasse implementiert einen Lexer fuer Hamster-Prolog-Programme.
 * 
 * 	@author Andreas (Prolog)
 */
public class HamsterPrologLexer extends HamsterLexer
{
	public HamsterPrologLexer() 
	{
		// Redefiniere die Schl�sselwort-Liste der Superklasse..
		this.KEYWORDS = new String[]{"arg", "assert", "asserta", "assertz", "atom", "atomic",
				"bagof", "div", "is", "mod", "call", "consult", "findall", "functor", "get", "get0",
				"integer", "name", "nl", "nonvar", "nospy", "not", "notrace", "numbervars", "put",
				"read", "reconsult", "repeat", "retract", "see", "seen", "setof", "spy", "tab", "tell",
				"told", "trace", "ttyflush", "var", "write", "listing", "true", "false", "help",
				"debug", "nodebug", "debugging", "tracing", "break", "abort", "halt", "chdir",
				"make", "include", "porttray_clause", "cyclic_term", "acyclic_term", "number", "string",
				"apropos","retractall"};
		
		// Rufe wiederholt die Initialisierungsmethode auf. 
		keywords = new HashMap();
		for (int i = 0; i < KEYWORDS.length; i++)
			keywords.put(KEYWORDS[i], KEYWORDS[i]);
	}
	
	/*
	@Override
	public JavaToken nextToken() {
		if (!ready())
			return null;
		int oldPos = this.pos;
		int type = PLAIN;
		if (LL(0) == ' ' || LL(0) == '\t' || LL(0) == '\n' || LL(0) == '\r') {
			consumeWhiteSpace();
			type = WHITESPACE;
		}
		else if (LL(0) == '%') {
			consumeSingleLineComment();
			type = COMMENT;
		} else if (LL(0) == '/') {
			if (LL(1) == '*') {
				consumeMultiLineComment();
				type = COMMENT;
			} else
			consumeCharacter();
		}
		/*
		/*else if (isPrologIdentifierStart(LL(0))) {
			consumeIdentifier();
			
		}
		/*else if (LL(0) == '%') {
			consumeSingleLineComment();
			type = COMMENT;
		} else if (LL(0) == '/') {
			if (LL(1) == '*') {
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
		} 
		*//*
		else {
			consumeCharacter();
		}
		
		String t = text.substring(oldPos, pos);
		if (type == PLAIN) {
			if (keywords.get(t) != null)
				type = KEYWORD;
		}
		return new JavaToken(t, off + oldPos, type);
	}
	
	private boolean isPrologIdentifierStart(char ll)
	{	
		System.out.print("ispIStart?: "+ll);
		if(Character.isLetter(ll) && Character.isUpperCase(ll) || ll == '_' )
		{
			System.out.println("yes");
			return true;
		}		
		System.out.println("no");
		return false;
	}
	
	private boolean isPrologIdentifierPart(char ll)
	{
		System.out.print("ispIPart?: "+ll);
		if( Character.isLetterOrDigit(ll) || ll == '_' )
		{
			System.out.println("yes");
			return true;			
		}
		System.out.println("no");
		return false;
	}

	protected void consumeIdentifier() {
		pos++;
		while (isPrologIdentifierPart(LL(0)))
			pos++;
	}
	*/
}
