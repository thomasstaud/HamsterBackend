package io.github.Hattinger04.hamsterEvaluation.compiler.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import io.github.Hattinger04.hamsterEvaluation.compiler.model.CompilerModel;
import io.github.Hattinger04.hamsterEvaluation.compiler.model.JavaError;
import io.github.Hattinger04.hamsterEvaluation.model.HamsterFile;
import io.github.Hattinger04.hamsterEvaluation.workbench.Utils;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench;

/**
 * Dies ist der Controller-Teil der Compiler-Komponente.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.3 $
 */
public class CompilerController {
	/**
	 * Dieser ActionCommand wird von einer Action benutzt, die das Kompilieren
	 * startet.
	 */
	public static final String ACTION_COMPILE = "compile";

	/**
	 * Dieser ActionCommand wird benutzt, wenn ein Eintrag aus der Fehlertabelle
	 * ausgewaehlt wurde.
	 */
	public static final String ACTION_SELECT = "select";

	/**
	 * Dieses Kommando wird benutzt, wenn der CLASSPATH geaendert werden soll.
	 */
	public static final String ACTION_CLASSPATH = "classpath";

	/**
	 * Dies ist die Werkbank.
	 */
	private Workbench workbench;

	/**
	 * Das Model der Compiler-Componente.
	 */
	private CompilerModel compilerModel;


	/**
	 * Dies ist die aktuelle editierte Datei. Diese wird bei einem Klick auf
	 * "Kompilieren" uebersetzt.
	 */
	private HamsterFile activeFile;

	/**
	 * Der Konstruktor des CompilerControllers. Erzeugt die View-Komponenten.
	 * 
	 * @param model
	 *            Das schon erzeugte Model
	 * @param workbench
	 *            Die Werkbank
	 */
	public CompilerController(CompilerModel model, Workbench workbench) {
		this.workbench = workbench;

		this.compilerModel = model;
	}

	public boolean ensureCompiled(HamsterFile file) {
		File classFile = new File(file.getFile().getParent(), file.getName()
				+ ".class");
		if (!classFile.exists()
				|| file.lastModified() > classFile.lastModified()) {
				try {
					System.out.println("Ensuring Compiled...");
					return this.compilerModel.compile(file); // dibo 15.11.2010
				} catch (IOException e) { // dibo 25.10.2011
					System.out.println("Compiling error! " + e.getMessage());
				}
			} 
		return true;
	}

	/**
	 * Wird von den View-Komponenten ausgeloest und faehrt die entsprechende
	 * Aktion aus.
	 * 
	 * @param e
	 *            Der ActionEvent, der die Aktion beschreibt.
	 */
	



	/**
	 * Liefert die Werkbank
	 * 
	 * @return die Werkbank
	 */
	public Workbench getWorkbench() {
		return this.workbench;
	}

	/**
	 * Setzt activeFile.
	 * 
	 * @param activeFile
	 *            Der neue Wert von activeFile.
	 */
	public void setActiveFile(HamsterFile activeFile) {
		this.activeFile = activeFile;
	}
}