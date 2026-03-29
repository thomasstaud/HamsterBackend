package at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Diese Klasse filtert die Dateien, die im JFile-Chooser angezeigt werden. Der
 * Filter ist so konfigurierbar, dass er entweder nur Programme oder nur
 * Territorien anzeigt. Alle uebrigen Datei (generierte java- oder
 * class-Dateien) werden ausgeblendet.
 * 
 * @author Daniel Jasper
 */
public class HamsterFileFilter  implements java.io.FileFilter {
	public static final HamsterFileFilter HAM_FILTER = new HamsterFileFilter(
			".ham", "Programm");

	public static final HamsterFileFilter TER_FILTER = new HamsterFileFilter(
			".ter", "Territorium");

	private String extension;

	private String description;

	public HamsterFileFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	public boolean accept(File f) {

		if (f.isDirectory()) {
			return true;
		}

		/*
		 * if (Utils.SCHEME) { return f.getName().endsWith(extension); }
		 */

		// lego
		if (Utils.SCHEME && Utils.LEGO) {
			return f.getName().endsWith(extension);
		}

		// Scheme und/oder deaktiviert
		if (f.getName().endsWith(extension)) {
			byte[] buffer = null;
			try {
				FileInputStream input = new FileInputStream(f);
				buffer = new byte[input.available()];
				input.read(buffer);
				input.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			String s = new String(buffer);
			int pos = s.indexOf("*/");
			if (pos > 0 && pos + 2 < s.length()) {
				String typeString = s.substring(0, pos);
				/* lego */
				if (!Utils.LEGO && typeString.equals("/*lego program")) {
					return false;
				}
				if (!Utils.SCHEME && typeString.equals("/*scheme program")) { // Martin
					return false;
				}
				if (!Utils.PROLOG && typeString.equals("/*prolog program")) { // Prolog
					return false;
				}
				if (!Utils.PYTHON && typeString.equals("/*python program")) { // Python
					return false;
				}
				if (!Utils.JAVASCRIPT && typeString.equals("/*javascript program")) { // JavaScript
					return false;
				}
				if (!Utils.RUBY && typeString.equals("/*ruby program")) { // Ruby
					return false;
				}
			}
			return true;
		} else {
			return false;
		}

	}

	public String getDescription() {
		return description;
	}

	public String getExtension() {
		return extension;
	}
}