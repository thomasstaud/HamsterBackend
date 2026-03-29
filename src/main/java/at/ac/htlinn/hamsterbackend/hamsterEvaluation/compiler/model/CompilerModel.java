package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;


import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

/**
 * Dies ist das Model der Compiler Komponente. Es kombiniert im Wesentlichen Den
 * Precompiler und den JavaCompiler und bietet den Benachrichtungsmechanismus
 * fuer die View-Komponenten.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class CompilerModel extends Observable {
	/**
	 * Dieses Argument benachrichtung ueber eine Aenderung der Compilerfehler.
	 */
	public static final String COMPILER_ERRORS = "compiler-errors";

	/**
	 * Der Precompiler
	 */
	protected Precompiler precompiler;
	/**
	 * Der JavaCompiler
	 */
	protected JavaCompiler javaCompiler;

	/**
	 * Die Liste der Fehler, die bei der letzten Compilierung aufgetreten sind.
	 */
	protected List compilerErrors;

	/**
	 * Der Konstruktor des CompilerModels. Erzeugt lediglich Instanzen von
	 * Precompiler und JavaCompiler
	 */
	public CompilerModel() {
		precompiler = new Precompiler();
		javaCompiler = new JavaCompiler();
	}

	/**
	 * Fuerht eine Kompilierung der uebergebenen Datei durch.
	 * 
	 * @param file
	 *            Die zu kompilierende Datei.
	 * @throws IOException
	 *             Falls ein Fehler auftritt
	 */
	public boolean compile(HamsterFile file) throws IOException {
		if (!precompiler.precompile(file)) {
		    return false;
		}
		compilerErrors = javaCompiler.compile(file);
		setChanged();
		notifyObservers(COMPILER_ERRORS);
		if (compilerErrors == null || compilerErrors.size() == 0) {
			return true;
		}
		for(Object o : compilerErrors) {
//			System.out.println("Compiler Error: " + o.toString());
		}
		return false;
	}

	/**
	 * Liefert die Fehlerliste
	 * 
	 * @return Die Liste der Fehler, die bei der letzten Kompilierung
	 *         aufgetreten sind.
	 */
	public List getCompilerErrors() {
		return compilerErrors;
	}
	
	public Precompiler getPrecompiler() {
		return precompiler;
	}
}