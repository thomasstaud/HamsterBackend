package at.ac.htlinn.hamsterbackend.hamsterEvaluation.interpreter;


import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;

import java.io.File;


/**
 * Die Klasse stellt eine Repraesentation des Hamster-Territoriums dar. Sie
 * definiert ausschliesslich Klassenmethoden. Diese dienen zum Abfragen
 * bestimmter Zustandswerte des aktuellen Territoriums.
 * 
 * @author Dietrich Boles (Universitaet Oldenburg)
 * @version 1.0 (25.01.2006)
 * 
 */
public class Territorium {

	/**
	 * private-Konstruktor: es koennen keine Instanzen der Klasse erzeugt werden
	 */
	private Territorium() {
	}

	/**
	 * liefert die Anzahl an Reihen im Territorium
	 * 
	 * @return die Anzahl an Reihen im Territorium
	 */
	public static int getAnzahlReihen() {
		Terrain t = Workbench.getWorkbench().getSimulationController()
				.getSimulationModel().getTerrain();
		int res = t.getHeight();
		return res;
	}

	/**
	 * liefert die Anzahl an Spalten im Territorium
	 * 
	 * @return die Anzahl an Spalten im Territorium
	 */
	public static int getAnzahlSpalten() {
		Terrain t = Workbench.getWorkbench().getSimulationController()
				.getSimulationModel().getTerrain();
		int res = t.getWidth();
		return res;
	}

	/**
	 * ueberprueft, ob sich auf der Kachel (reihe/spalte) eine Mauer befindet;
	 * es wird genau dann true geliefert, wenn sich auf der angegebenen Kachel
	 * eine Mauer befindet oder wenn sich die angegebenen Werte ausserhalb des
	 * Territoriums befinden
	 * 
	 * @param reihe
	 *            Reihe der Kachel
	 * @param spalte
	 *            Spalte der Kachel
	 * @return true geliefert, wenn sich auf der angegebenen Kachel eine Mauer
	 *         befindet oder wenn sich die angegebenen Werte ausserhalb des
	 *         Territoriums befinden; sonst false
	 */
	public static boolean mauerDa(int reihe, int spalte) {
		Terrain t = Workbench.getWorkbench().getSimulationController()
				.getSimulationModel().getTerrain();
		boolean res = t.getWall(spalte, reihe);
		return res;
	}

	/**
	 * liefert die Gesamtzahl an Koernern, die im Territorium auf Kacheln
	 * herumliegen
	 * 
	 * @return die Gesamtzahl an Koernern, die im Territorium auf Kacheln
	 *         herumliegen
	 */
	public static int getAnzahlKoerner() {
		int anzahl = 0;
		for (int r = 0; r < getAnzahlReihen(); r++) {
			for (int s = 0; s < getAnzahlSpalten(); s++) {
				anzahl = anzahl + getAnzahlKoerner(r, s);
			}
		}
		return anzahl;
	}

	/**
	 * liefert die Anzahl an Koernern auf der Kachel (reihe/spalte) oder 0,
	 * falls die Kachel nicht existiert oder durch eine Mauer blockiert ist
	 * 
	 * @param reihe
	 *            Reihe der Kachel
	 * @param spalte
	 *            Spalte der Kachel
	 * @return die Anzahl an Koernern auf der Kachel (reihe/spalte) oder 0,
	 *         falls die Kachel nicht existiert oder durch eine Mauer blockiert
	 *         ist
	 */
	public static int getAnzahlKoerner(int reihe, int spalte) {
		Terrain t = Workbench.getWorkbench().getSimulationController()
				.getSimulationModel().getTerrain();
		int res = t.getCornCount(spalte, reihe);
		return res;
	}

	/**
	 * liefert die Gesamtzahl an erzeugten und initialisierten Hamstern im
	 * Territorium (inkl. dem Standard-Hamster)
	 * 
	 * @return die Gesamtzahl an erzeugten und initialisierten Hamstern im
	 *         Territorium
	 */
	public static int getAnzahlHamster() {
		int res = Hamster.getAnzahlHamster();
		return res;
	}

	/**
	 * liefert alle erzeugten und initialisierten Hamster im Territorium (inkl.
	 * dem Standard-Hamster)
	 * 
	 * @return alle erzeugten und initialisierten Hamster im Territorium
	 */
	public static Hamster[] getHamster() {
		int size = Hamster._intern_hamsters.size();
		Hamster[] res = new Hamster[size];
		for (int h = 0; h < size; h++) {
			res[h] = (Hamster) Hamster._intern_hamsters.get(h);
		}
		return res;
	}

	/**
	 * liefert die Anzahl an Hamstern auf der Kachel (reihe/spalte) oder 0,
	 * falls die Kachel nicht existiert oder durch eine Mauer blockiert ist
	 * 
	 * @param reihe
	 *            Reihe der Kachel
	 * @param spalte
	 *            Spalte der Kachel
	 * @return die Anzahl an Hamstern auf der Kachel (reihe/spalte) oder 0,
	 *         falls die Kachel nicht existiert oder durch eine Mauer blockiert
	 *         ist
	 */
	public static int getAnzahlHamster(int reihe, int spalte) {
		int anzahl = 0;
		for (int h = 0; h < Hamster._intern_hamsters.size(); h++) {
			Hamster ham = (Hamster) Hamster._intern_hamsters.get(h);
			if (ham.getReihe() == reihe && ham.getSpalte() == spalte) {
				anzahl++;
			}
		}
		return anzahl;
	}

	/**
	 * liefert alle erzeugten und initialisierten Hamster, die aktuell auf der
	 * Kachel (reihe/spalte) stehen (inkl. dem Standard-Hamster)
	 * 
	 * @param reihe
	 *            Reihe der Kachel
	 * @param spalte
	 *            Spalte der Kachel
	 * @return alle erzeugten und initialisierten Hamster, die aktuell auf der
	 *         Kachel (reihe/spalte) stehen
	 */
	public static Hamster[] getHamster(int reihe, int spalte) {
		Hamster[] res = new Hamster[getAnzahlHamster(reihe, spalte)];
		int index = 0;
		for (int h = 0; h < Hamster._intern_hamsters.size(); h++) {
			Hamster ham = (Hamster) Hamster._intern_hamsters.get(h);
			if (ham.getReihe() == reihe && ham.getSpalte() == spalte) {
				res[index++] = ham;
			}
		}
		return res;
	}

	/**
	 * Laedt ein Territorium aus der angegebenen Datei. Wenn die Datei nicht
	 * existiert oder keine gueltige Territoriumsdatei ist, passiert nichts. Die
	 * Methode darf nur aufgerufen werden, bevor irgendein Hamster erzeugt
	 * worden ist. Ansonsten passiert auch nichts.
	 * 
	 * Die Methode gehoert nicht zum Standard-Java-Hamster-Modell!
	 * 
	 * @param dateiName
	 *            Name der Datei mit dem zu ladenenden Territorium
	 */
	public static void ladeTerritorium(String dateiName) {
		try {
			String terr = dateiName;
			if (!dateiName.endsWith(".ter")) {
				terr = terr + ".ter";
			}
			File terrFile = new File(terr);
			if (!terrFile.exists()) {
				return;
			}
			HamsterFile ter = HamsterFile.getHamsterFile(terr);
			Workbench.getWorkbench().getSimulationController()
					.getSimulationModel().setTerrain(new Terrain(ter.load()));
		} catch (Exception exc) {
		}
	}

}