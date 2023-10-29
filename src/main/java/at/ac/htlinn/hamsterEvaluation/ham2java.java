package at.ac.htlinn.hamsterEvaluation;

import java.io.File;

import at.ac.htlinn.hamsterEvaluation.compiler.model.Precompiler;
import at.ac.htlinn.hamsterEvaluation.model.HamsterFile;

public class ham2java {

	public static void main(String[] args) {
		 if (args  .length != 1) {
			System.out.println("Aufruf: java -classpath hamstersimulator.jar;tools.jar "
					+ "at.ac.htlinn.hamsterEvaluation.ham2java <ham-Datei>");
			return;
		}

		String hamProgramm = args[0];
		if (!args[0].endsWith(".ham")) {
			hamProgramm = args[0] + ".ham";
		}
		File hamFile = new File(hamProgramm);
		if (!hamFile.exists()) {
			System.out.println("Aufruf: java -classpath hamstersimulator.jar;tools.jar "
					+ "at.ac.htlinn.hamsterEvaluation.ham2java <ham-Datei>");
			System.out.println("Fehler: Sie haben keine gueltige"
					+ " Hamster-Programm-Datei" + " angegeben! ");
			return;
		}
		try {
			Precompiler precompiler = new Precompiler();
			precompiler.precompile(HamsterFile.getHamsterFile(hamProgramm));
		} catch (Exception exc) {
			System.out.println("Fehler: " + exc.getMessage());
		}
	}

}
