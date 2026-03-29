package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;


import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;

import java.util.LinkedList;


/**
 * Diese Klasse repraesentiert einen Fehler, der bei der Compilierung mit dem
 * JavaCompiler auftreten. Sie speichert die Position und genauere Information
 * ueber den Fehler, so dass dies leicht in der Benutzerschnittstelle angezeigt
 * werden kann.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class JavaError {
	/**
	 * Die Zeile in der der Fehler aufgetreten ist.
	 */
	protected int line;

	/**
	 * Die Spalte, in der der Fehler aufgetreten ist.
	 */
	protected int column;

	/**
	 * Die Meldung des javac-Compilers
	 */
	protected String message;

	/**
	 * Die Datei, die den Fehler enthaelt.
	 */
	protected HamsterFile file;

	/**
	 * Bei manchen Fehlermeldungen liefert der javac-Compiler noch weitere
	 * Zeilen mit zusaetzlicher Information. Diese Information wird hier
	 * abgelegt.
	 */
	LinkedList extra;

	/**
	 * Der Konstruktor. Benoetigt alle Attribute
	 * 
	 * @param line
	 *            Die Zeile
	 * @param column
	 *            Die Spalte
	 * @param message
	 *            Die Meldung
	 * @param file
	 *            Die Datei
	 * @param extra
	 *            Zusaetzliche Information
	 */
	public JavaError(int line, int column, String message, HamsterFile file,
			LinkedList extra) {
		this.line = line;
		this.column = column;
		this.message = message;
		this.file = file;
		this.extra = extra;
	}

	/**
	 * Liefert die Zeile der Fehlerposition
	 * 
	 * @return Die Zeile
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Liefert die Spalte der Fehlerposition.
	 * 
	 * @return Die Spalte
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 * @return Die Meldung als String
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Liefert die zusaetzliche Information als verkettete Liste.
	 * @return Die Liste.
	 */
	public LinkedList getExtra() {
		return extra;
	}

	/**
	 * Die fehlerhafte Datei
	 * @return Die Datei.
	 */
	public HamsterFile getFile() {
		return file;
	}
}