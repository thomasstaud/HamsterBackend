package at.ac.htlinn.hamsterbackend.hamsterEvaluation;

import java.util.Locale;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.SimulationModel;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import java.io.*;

public class run {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out
					.println("Aufruf: java -classpath hamstersimulator.jar;tools.jar "
							+ ".hamsterEvaluation.run <ham-Programm> [<territorium-Datei>]");
			return;
		}

		String hamProgramm = args[0];
		if (args[0].endsWith(".class")) {
			hamProgramm = args[0].substring(0, args[0].length() - 6);
		}
		File hamFile = new File(hamProgramm + ".class");
		if (!hamFile.exists()) {
			System.out
					.println("Aufruf: java -classpath hamstersimulator.jar;tools.jar "
							+ ".hamsterEvaluation.run <ham-Programm> [<territorium-Datei>]");
			System.out.println("Fehler: Sie haben kein gueltiges"
					+ " compiliertes Hamster-Programm" + " angegeben! ");
			return;
		}

		String terr = null;
		if (args.length >= 2) {
			terr = args[1];
			if (!args[1].endsWith(".ter")) {
				terr = terr + ".ter";
			}
			File terrFile = new File(terr);
			if (!terrFile.exists()) {
				System.out
						.println("Aufruf: java -classpath hamstersimulator.jar;tools.jar "
								+ ".hamsterEvaluation.run <ham-Programm> [<territorium-Datei>]");
				System.out.println("Fehler: Sie haben keine gueltige"
						+ " Territorium-Datei" + " angegeben! ");
				return;
			}
		}

		Locale.setDefault(Locale.GERMANY);
		// Sicherstellen, dass die noetigen Verzeichnisse existieren, in denen
		// die Hamsterprogramme abgelegt werden.

		Utils.loadProperties(); // dibo
		Utils.ensureHome();

		// Erzeugen der Werkbank.
		Workbench workbench = Workbench.getSimWorkbench(new SimulationModel());
		workbench.setActiveFile(HamsterFile.getHamsterFile(hamProgramm));

		SimulationModel simulationModel = workbench.getSimulationController()
				.getSimulationModel();

		if (terr != null) {
			HamsterFile ter = HamsterFile.getHamsterFile(terr);
			simulationModel.setTerrain(new Terrain(ter.load()));
		}
	}

}
