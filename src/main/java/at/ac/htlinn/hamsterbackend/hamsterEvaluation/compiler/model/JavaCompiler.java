package at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sun.tools.javac.Main;


/**
 * Diese Klasse kapselt den im Paket tools.jar enthaltenen Java-Compiler, der
 * dem JDK beiliegt. Zu der Kapselung gehoert der Aufruf und ein Einlesen der
 * entstandenen Fehlermeldungen.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.3 $
 */
public class JavaCompiler {
	/**
	 * In diese Temporaere Datei werden die javac-Fehlermeldungen geschrieben,
	 * bevor sie dann wieder eingelesen werden.
	 */
	public static final String TEMP_FILE = "compiler.tmp"; // TODO: very ugly programming by myself -> make better someday

	/**
	 * Der Classpath waehrend der Compilierung
	 */
	protected String classpath;

	protected BufferedReader input;

	public JavaCompiler() {
		classpath = System.getProperty("java.class.path");
	}

	/**
	 * Diese Methode liest die Fehler aus dem String errors aus und erzeugt eine
	 * Liste von JavaErrors.
	 * 
	 * @param errors
	 *            Die Fehler als String in der Form, wie sie javac liefert
	 * @param file
	 *            Die Hamster-Datei, bei der die Fehler aufgetreten sind
	 * @return Eine Liste mit JavaErrors
	 */
	public List parseErrors(String errors, HamsterFile file) {
		Pattern p = Pattern.compile("(.*):(\\d*):(.*)");
		String[] strings = errors.split("\\r\\n|\\r|\\n");
		LinkedList javaErrors = new LinkedList();
		for (int i = strings.length - 2; i >= 0; i--) {
			int column = strings[i].indexOf("^");
			i--;
			if (i < 0)
				break;
			Matcher m = p.matcher(strings[i]);
			LinkedList extra = new LinkedList();
			while (!m.find()) {
				extra.addFirst(strings[i]);
				i--;
				if (i < 0)
					break;
				m = p.matcher(strings[i]);
			}
			try {
				String fileString = m.group(1);
				int line = Integer.parseInt(m.group(2));
				String error = m.group(3);

				javaErrors.addFirst(new JavaError(line, column, error, file,
						extra));
			} catch (Exception exc) {

			}
		}
		return javaErrors;
	}

	/**
	 * Diese Methode liest die temporaere Datei.
	 * 
	 * @return Der Inhalt der Datei als String
	 */
	public static String readTempFile() throws IOException {
		byte[] buffer = null;

		FileInputStream input = null;
		try {
			input = new FileInputStream(Utils.HOME + Utils.FSEP + SecurityContextHolder.getContext().getAuthentication().getName() + Utils.FSEP + TEMP_FILE);
			buffer = new byte[input.available()];
			input.read(buffer);
		} finally {
			if (input != null) {
				input.close();
			}
		}

		return new String(buffer);
	}

	/**
	 * Mit dieser Methode wird das Compilieren angestossen.
	 * 
	 * @param file
	 *            Die zu Compilierende Datei
	 * @return Eine (moeglicherweise leere) Liste von JavaErrors.
	 */
	public List compile(HamsterFile file) throws IOException {
		String javaFile = file.getAbsoluteJava();
		String newClasspath = file.getAbsolute().substring(0,
				file.getAbsolute().lastIndexOf(Utils.FSEP))
				+ Utils.PSEP
				+ classpath
				+ Utils.PSEP
				+ Workbench.getWorkbench().getProperty("classpath", "");
		String[] args = { "-g", "-classpath", newClasspath, javaFile };
		List javaErrors = null;

		PrintWriter writer = null;
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(Utils.HOME +  Utils.FSEP + SecurityContextHolder.getContext().getAuthentication().getName() + Utils.FSEP + TEMP_FILE); 

			writer = new PrintWriter(fwriter);
			Main.compile(args, writer);
		} finally {
			if (fwriter != null) {
				fwriter.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		String errors = readTempFile();
		javaErrors = parseErrors(errors, file);
		new File(javaFile); // delete();

		// new File(javaFile).delete();
		return javaErrors;
	}
}