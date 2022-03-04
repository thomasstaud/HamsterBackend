package io.github.Hattinger04.hamsterEvaluation.debugger.controller;

import com.sun.jdi.StackFrame;

import io.github.Hattinger04.hamsterEvaluation.debugger.model.DebuggerModel;
import io.github.Hattinger04.hamsterEvaluation.model.HamsterFile;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench;

/**
 * Dies ist der Controller-Teil der Debugger-Komponente. Erzeugt die View und
 * nimmt Events von dieser entgegen.
 * 
 * TODO: Vor dem Ausfuehren auf Compilierung testen
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.2 $
 */
public class DebuggerController   {
	/**
	 * Aktion Debugger aktiveren.
	 */
	public static final String ACTION_ENABLE = "enable";
	/**
	 * Aktion Schritt hinein.
	 */
	public static final String ACTION_STEP = "step";
	/**
	 * Aktion Schritt ueber.
	 */
	public static final String ACTION_STEPOVER = "stepover";
	/**
	 * Aktion Stoppen.
	 */
	public static final String ACTION_STOP = "stop";
	/**
	 * Aktion Pause.
	 */
	public static final String ACTION_PAUSE = "pause";
	/**
	 * Aktion Starten.
	 */
	public static final String ACTION_START = "start";
	/**
	 * Aktion Stackframe-Auswaehlen.
	 */
	public static final String ACTION_FRAME = "frame";

	/**
	 * Der Controller der Werkbank.
	 */
	protected Workbench workbench;

	/**
	 * Das dazugehoerige Model.
	 */
	protected DebuggerModel model;

	/**
	 * View-Komponente zur Anzeige von Stackframes.
	 */

	/**
	 * Die aktuell editierte Datei.
	 */
	protected HamsterFile activeFile;

	public DebuggerController(DebuggerModel model, Workbench workbench) {
		this.workbench = workbench;
		this.model = model;
	}

	/*
	 * Verarbeitet ActionEvents
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	
	/**
	 * Liefert die Werkbank
	 * 
	 * @return Die Werkbank
	 */
	public Workbench getWorkbench() {
		return workbench;
	}

	/**
	 * Liefert die Stackframe-Anzeige
	 * 
	 * @return Die Stackframe-Anzeige
	 */

	/**
	 * Setzt die aktuelle editierte Datei.
	 * 
	 * @param activeFile
	 *            Die neue Datei
	 */
	public void setActiveFile(HamsterFile activeFile) {
		this.activeFile = activeFile;
	}

	public HamsterFile getActiveFile() {
		return this.activeFile;
	}

	
	public DebuggerModel getDebuggerModel() {
		return this.model;
	}
}