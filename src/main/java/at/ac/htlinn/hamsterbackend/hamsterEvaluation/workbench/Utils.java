package at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

import java.awt.Color;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Diese Klasse enthaelt Hilfsmethoden und Konstanten, die im Hamster-Simulator
 * benoetigt werden.
 * 
 * @author Daniel Jasper
 */
public class Utils {

	public static final int TYPE_HAM = 1;

	public static final int TYPE_TER = 2;

	public static final String PSEP = System.getProperty("path.separator");

	public static final String FSEP = System.getProperty("file.separator");

	public static final String LSEP = System.getProperty("line.separator");

	// TODO: Umlegen von HOME ins aktuelle Verzeichnis
	public static String HOME = System.getProperty("user.dir") + FSEP + "src/main/resources/hamster"; 
	
	public static String LOGFOLDER = "";

	public static final Color[] COLORS = { Color.BLUE, Color.RED, Color.GREEN,
			Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK,
			Color.GRAY, Color.WHITE };

	/**
	 * Der Rand von Buttons in der Toolbar. Noetig zur Einhaltung der Java Look
	 * and Feel Design Guidelines.
	 */
	public static final Insets TOOLBAR_MARGIN = new Insets(0, 0, 0, 0);

	/**
	 * Das geladene ResourceBundle, passend zur aktuellen Locale
	 */

	/**
	 * Mit dieser Methode kann das ResourceBundle erfragt werden. Ist es noch
	 * nicht geladen, wird es aus der entsprechenden Datei gelesen.
	 * 
	 * @return Das ResourceBundle
	 */




	// --- added by C. Noeske: very simple replacement of environment variables used in hamster.properties
	public static String replaceEnvVariables(String s) {
	    int i=0;
	    while ((i=s.indexOf("$(",i)) >= 0) {
		  int ie = s.indexOf(')',i);
		  if (ie > 2) {
		    String envVarName = s.substring(i+2, ie);
		    String envVar = System.getenv(envVarName);
		    if (envVar != null) {
		      s = s.replace("$("+envVarName+")", envVar);
		    }
		  }
	    }
	    return s;
	}
	// end of addition
	  
	// --- added by C. Noeske: check, if a directory exists, otherwise create it
	public static String checkAndCreateDir(String dirName) {
	    dirName = replaceEnvVariables(dirName);
	    if ((dirName != null) && (dirName.length() != 0)) {
	      File dir = new File(dirName);
	      if ((dir.exists()) || (dir.mkdirs())) {
	        // success
	      } 
	    }
	    return dirName;
	}
	// end of addition
	
	
	public static boolean isValidName(String name) {
		if (!Character.isJavaIdentifierStart(name.charAt(0)))
			return false;
		for (int i = 1; i < name.length(); i++)
			if (!Character.isJavaIdentifierPart(name.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Diese Methode initialisiert eine Action mit den Daten aus dem
	 * ResourceBundle.
	 * 
	 * @param action
	 *            Die zu initialisierende Action
	 * @param key
	 *            Der ue aus dem ResourceBundle
	 */


	/**
	 * Diese Methode erzeugt einen Button zu einer Action. Dieser hat dann das
	 * Design, das fuer den Einsatz in einer Toolbar ausgerichtet ist.
	 * 
	 * @param action
	 *            Die Action, zu der der Knopf erzeugt wird
	 * @return Der erzeugte Knopf
	 */

	/**
	 * Diese Methode erzeugt einen ToggleButton zu einer Action. Dieser hat dann
	 * das Design, das fuer den Einsatz in einer Toolbar ausgerichtet ist.
	 * 
	 * @param action
	 *            Die Action, zu der der Knopf erzeugt wird
	 * @return Der erzeugte Knopf
	 */

	/**
	 * Diese Methode erzeugt einen Menueeintrag zu einer Action.
	 * 
	 * @param action
	 *            Die Action, zu der der Menueeintrag erzeugt wird
	 * @return Der erzeugte Menueeintrag
	 */

	/**
	 * Diese Methode liefert das Image zu dem uebergebenen Namen. Alle Bilder
	 * liegen in dem Verzeichnis resources.
	 * 
	 * @param name
	 *            Der Name der Datei, aus der das Bild gelesen werden soll.
	 * @return Das Image
	 */

	/**
	 * Diese Methode erzeugt ein Icon aus dem Aufruf der Methode getImage.
	 * 
	 * @param name
	 *            Der Name der Datei, aus der das Bild gelesen werden soll.
	 * @return Das ImageIcon.
	 */

	/**
	 * Diese Methode stellt sicher, dass die Standard-Verzeichnisstruktur
	 * vorhanden ist, in der die Hamsterprorgramme und -territorien gespeichert
	 * werden.
	 */
	public static void ensureHome() {
		File file = new File(HOME);
		if (!file.exists())
			file.mkdirs();
	}


	// dibo

	public static boolean security = true;

	// Scheme Martin
	public static boolean SCHEME = false;

	// Prolog
	public static boolean PROLOG = false;

	// Python
	public static boolean PYTHON = false;
	
	// JavaScript
	public static boolean JAVASCRIPT = false;

	// Ruby
	public static boolean RUBY = false;

	// Scratch
	public static boolean SCRATCH = false;

	// Endliche Automaten
	public static boolean FSM = false;
	
	// Flowcharts
	public static boolean FLOWCHART = false;

	public static String PLCON = "swipl.exe";

	public static String PROLOG_MSG = "";

	public static boolean INDENT = true;

	public static int FONTSIZE = 12; // dibo 230309

	public static boolean runlocally = false;

	public static String language = "de";

	public static String LAF = null;

	public static boolean DREI_D = true;

	public static int COLOR = Hamster.BLUE;

	/* lego */
	public static boolean LEGO = false;

	static Properties p;

	public static void loadProperties() {
		// not needed in our version
	}

	public static void doLoading(Properties p, File file) {
		try {
			if (file != null && file.exists() && file.isFile()
					&& file.canRead()) {
				p.load(new FileInputStream(file));

				String str = p.getProperty("security");
				if (str != null)
					if (str.equals("false")) {
						Utils.security = false;
					} else {
						Utils.security = true;
					}
				str = p.getProperty("workspace");
				if (str != null) {
					Utils.HOME = checkAndCreateDir(str);
				} else {
					str = p.getProperty("home");
					if (str != null) {
						Utils.HOME = checkAndCreateDir(str);
					}
				}
				str = p.getProperty("logfolder");
				if (str != null) {
					Utils.LOGFOLDER = checkAndCreateDir(str) + FSEP;
				}

				// Scheme Martin
				str = p.getProperty("scheme");
				if (str != null)
					if (str.equals("true")) {
						Utils.SCHEME = true;
					} else {
						Utils.SCHEME = false;
					}

				// Python
				str = p.getProperty("python");
				if (str != null)
					if (str.equals("true")) {
						Utils.PYTHON = true;
					} else {
						Utils.PYTHON = false;
					}
				
				// JavaScript
				str = p.getProperty("javascript");
				if (str != null)
					if (str.equals("true")) {
						Utils.JAVASCRIPT = true;
					} else {
						Utils.JAVASCRIPT = false;
					}

				// Ruby
				str = p.getProperty("ruby");
				if (str != null)
					if (str.equals("true")) {
						Utils.RUBY = true;
					} else {
						Utils.RUBY = false;
					}

				// Scratch
				str = p.getProperty("scratch");
				if (str != null)
					if (str.equals("true")) {
						Utils.SCRATCH = true;
					} else {
						Utils.SCRATCH = false;
					}
				
				// FSM
				str = p.getProperty("fsm");
				if (str != null)
					if (str.equals("true")) {
						Utils.FSM = true;
					} else {
						Utils.FSM = false;
					}
				
				// Flowchart
				str = p.getProperty("flowchart");
				if (str != null)
					if (str.equals("true")) {
						Utils.FLOWCHART = true;
					} else {
						Utils.FLOWCHART = false;
					}

				// Prolog
				str = p.getProperty("prolog");
				if (str != null)
					if (str.equals("true")) {
						Utils.PROLOG = true;
					} else {
						Utils.PROLOG = false;
					}

				str = p.getProperty("plcon");
				if (str != null)
					Utils.PLCON = new String(str);

				PROLOG_MSG = "Die Prolog-Interpreter-Anwendungsdatei konnte nicht lokalisiert werden.\n\n"
						+ "Um Prolog nutzen zu k�nnen, installieren Sie bitte SWIProlog\n"
						+ "und/oder erweitern Sie die PATH-Variable des\n"
						+ "Systems um das Verzeichnis, in dem sich die Datei 'swipl.exe' befindet.\n"
						+ "Geben Sie alternativ in der Datei 'hamster.properties' in der Property 'plcon'\n "
						+ "den vollst�ndigen Pfad des SWIProlog-Interpreters an (-> LINUX-Nutzer!).\n\n"
						+ "Wenn Sie Prolog nicht nutzen wollen, setzen Sie in der Datei 'hamster.properties'\n"
						+ "das Property 'prolog' auf 'false'.";

				/* lego */
				str = p.getProperty("lego");
				if (str != null)
					if (str.equals("true")) {
						Utils.LEGO = true;
					} else {
						Utils.LEGO = false;
					}

				str = p.getProperty("indent");
				if (str != null)
					if (str.equals("false")) {
						Utils.INDENT = false;
					} else {
						Utils.INDENT = true;
					}

				str = p.getProperty("runlocally");
				if (str != null) {
					Utils.runlocally = str.equals("true");
				}

				str = p.getProperty("language");
				if (str != null)
					if (str.equals("en")) {
						Utils.language = "en";
					} else {
						Utils.language = "de";
					}

				str = p.getProperty("laf");
				if (str != null && !str.equals("")
						&& !str.toLowerCase().equals("default"))
					Utils.LAF = str;

				str = p.getProperty("3D");
				if (str != null)
					Utils.DREI_D = str.equals("true");

				str = p.getProperty("color");
				if (str != null)
					if (str.toUpperCase().equals("BLAU")
							|| str.toUpperCase().equals("BLUE")) {
						Utils.COLOR = Hamster.BLUE;
					} else if (str.toUpperCase().equals("ROT")
							|| str.toUpperCase().equals("RED")) {
						Utils.COLOR = Hamster.RED;
					} else if (str.toUpperCase().equals("GRUEN")
							|| str.toUpperCase().equals("GREEN")) {
						Utils.COLOR = Hamster.GREEN;
					} else if (str.toUpperCase().equals("GELB")
							|| str.toUpperCase().equals("YELLOW")) {
						Utils.COLOR = Hamster.YELLOW;
					} else if (str.toUpperCase().equals("CYAN")) {
						Utils.COLOR = Hamster.CYAN;
					} else if (str.toUpperCase().equals("MAGENTA")) {
						Utils.COLOR = Hamster.MAGENTA;
					} else if (str.toUpperCase().equals("ORANGE")) {
						Utils.COLOR = Hamster.ORANGE;
					} else if (str.toUpperCase().equals("PINK")) {
						Utils.COLOR = Hamster.PINK;
					} else if (str.toUpperCase().equals("GRAU")
							|| str.toUpperCase().equals("GRAY")) {
						Utils.COLOR = Hamster.GRAY;
					} else if (str.toUpperCase().equals("WEISS")
							|| str.toUpperCase().equals("WHITE")) {
						Utils.COLOR = Hamster.WHITE;
					}
				Hamster.$i$defColor = Utils.COLOR;
			}
		} catch (Exception exc) {
		}

	}

	public static URL getFileURL(String name) {
		return Utils.class.getClassLoader().getResource(name);
	}

	// Scratch Props
	private static String PROP_NEW_PROCEDURE = "scratch_new_procedure";
	private static String PROP_NEW_FUNCTION = "scratch_new_function";
	private static String PROP_VOID_RETURN = "scratch_void_return";
	private static String PROP_BOOL_RETURN = "scratch_bool_return";
	private static String PROP_TRUE = "scratch_true";
	private static String PROP_FALSE = "scratch_false";
	private static String PROP_AND = "scratch_and";
	private static String PROP_OR = "scratch_or";
	private static String PROP_NOT = "scratch_not";
	private static String PROP_IF = "scratch_if";
	private static String PROP_ELSE_IF = "scratch_else_if";
	private static String PROP_ELSE = "scratch_else";
	private static String PROP_WHILE = "scratch_while";
	private static String PROP_DO = "scratch_do";
	private static String PROP_DO_WHILE = "scratch_do_while";

	public static String DEF_NEW_PROCEDURE = "Neue Prozedur";
	public static String DEF_NEW_FUNCTION = "Neue Funktion";
	public static String DEF_VOID_RETURN = "verlasse";
	public static String DEF_BOOL_RETURN = "liefere";
	public static String DEF_TRUE = "wahr";
	public static String DEF_FALSE = "falsch";
	public static String DEF_AND = "und";
	public static String DEF_OR = "oder";
	public static String DEF_NOT = "nicht";
	public static String DEF_IF = "falls";
	public static String DEF_ELSE_IF = "falls";
	public static String DEF_ELSE = "sonst";
	public static String DEF_WHILE = "solange";
	public static String DEF_DO = "wiederhole";
	public static String DEF_DO_WHILE = "solange";

	public static String DEF_E_NEW_PROCEDURE = "new procedure";
	public static String DEF_E_NEW_FUNCTION = "new function";
	public static String DEF_E_VOID_RETURN = "return";
	public static String DEF_E_BOOL_RETURN = "return";
	public static String DEF_E_TRUE = "true";
	public static String DEF_E_FALSE = "false";
	public static String DEF_E_AND = "and";
	public static String DEF_E_OR = "or";
	public static String DEF_E_NOT = "not";
	public static String DEF_E_IF = "if";
	public static String DEF_E_ELSE_IF = "if";
	public static String DEF_E_ELSE = "else";
	public static String DEF_E_WHILE = "while";
	public static String DEF_E_DO = "do";
	public static String DEF_E_DO_WHILE = "while";

	public static String getNewProcedure() {
		String prop = p.getProperty(PROP_NEW_PROCEDURE);
		if (prop == null || prop.trim().equals("")) {
			return DEF_NEW_PROCEDURE;
		} else {
			return prop.trim();
		}
	}

	public static String getNewFunction() {
		String prop = p.getProperty(PROP_NEW_FUNCTION);
		if (prop == null || prop.trim().equals("")) {
			return DEF_NEW_FUNCTION;
		} else {
			return prop.trim();
		}
	}

	public static String getVoidReturn() {
		String prop = p.getProperty(PROP_VOID_RETURN);
		if (prop == null || prop.trim().equals("")) {
			return DEF_VOID_RETURN;
		} else {
			return prop.trim();
		}
	}

	public static String getBoolReturn() {
		String prop = p.getProperty(PROP_BOOL_RETURN);
		if (prop == null || prop.trim().equals("")) {
			return DEF_BOOL_RETURN;
		} else {
			return prop.trim();
		}
	}

	public static String getAnd() {
		String prop = p.getProperty(PROP_AND);
		if (prop == null || prop.trim().equals("")) {
			return DEF_AND;
		} else {
			return prop.trim();
		}
	}

	public static String getOr() {
		String prop = p.getProperty(PROP_OR);
		if (prop == null || prop.trim().equals("")) {
			return DEF_OR;
		} else {
			return prop.trim();
		}
	}

	public static String getNot() {
		String prop = p.getProperty(PROP_NOT);
		if (prop == null || prop.trim().equals("")) {
			return DEF_NOT;
		} else {
			return prop.trim();
		}
	}

	public static String getTrue() {
		String prop = p.getProperty(PROP_TRUE);
		if (prop == null || prop.trim().equals("")) {
			return DEF_TRUE;
		} else {
			return prop.trim();
		}
	}

	public static String getFalse() {
		String prop = p.getProperty(PROP_FALSE);
		if (prop == null || prop.trim().equals("")) {
			return DEF_FALSE;
		} else {
			return prop.trim();
		}
	}

	public static String getWhile() {
		String prop = p.getProperty(PROP_WHILE);
		if (prop == null || prop.trim().equals("")) {
			return DEF_WHILE;
		} else {
			return prop.trim();
		}
	}

	public static String getDo() {
		String prop = p.getProperty(PROP_DO);
		if (prop == null || prop.trim().equals("")) {
			return DEF_DO;
		} else {
			return prop.trim();
		}
	}

	public static String getDoWhile() {
		String prop = p.getProperty(PROP_DO_WHILE);
		if (prop == null || prop.trim().equals("")) {
			return DEF_DO_WHILE;
		} else {
			return prop.trim();
		}
	}

	public static String getIf() {
		String prop = p.getProperty(PROP_IF);
		if (prop == null || prop.trim().equals("")) {
			return DEF_IF;
		} else {
			return prop.trim();
		}
	}

	public static String getElseIf() {
		String prop = p.getProperty(PROP_ELSE_IF);
		if (prop == null || prop.trim().equals("")) {
			return DEF_ELSE_IF;
		} else {
			return prop.trim();
		}
	}

	public static String getElse() {
		String prop = p.getProperty(PROP_ELSE);
		if (prop == null || prop.trim().equals("")) {
			return DEF_ELSE;
		} else {
			return prop.trim();
		}
	}
}