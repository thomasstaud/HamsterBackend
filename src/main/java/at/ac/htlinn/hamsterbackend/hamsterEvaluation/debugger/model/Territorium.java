package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.InstructionProcessor;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.TerrainCellInstruction;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.TerrainInstruction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class Territorium {
	/**
	 * Damit keine Instanz der Klasse Territorium erzeugt wird.
	 */
	private Territorium() {
	}

	/**
	 * Der InstructionProcessor.
	 */
	static InstructionProcessor processor;

	/**
	 * Alle im Territorium erzeugten Hamster.
	 */
	static HashMap hamster = new HashMap();

	/**
	 * Fuegt einen Hamster zu dem Territorium hinzu.
	 * 
	 * @param h
	 */
	synchronized static void addHamster(Hamster h) {
		hamster.put(new Integer(h.id), h);
	}

	/**
	 * liefert die Anzahl an Reihen im Territorium
	 * 
	 * @return
	 */
	public synchronized static int getAnzahlReihen() {
		return ((Integer) processor.process(new TerrainInstruction(
				TerrainInstruction.COUNT_ROWS))).intValue();
	}

	/**
	 * liefert die Anzahl an Spalten im Territorium
	 * 
	 * @return
	 */
	public synchronized static int getAnzahlSpalten() {
		return ((Integer) processor.process(new TerrainInstruction(
				TerrainInstruction.COUNT_COLS))).intValue();
	}

	/**
	 * ueberprueft, ob sich auf der Kachel (reihe/spalte) eine Mauer befindet;
	 * es wird genau dann true geliefert, wenn sich auf der angegebenen Kachel
	 * eine Mauer befindet oder wenn sich die angegebenen Werte ausserhalb des
	 * Territoriums befinden
	 * 
	 * @param reihe
	 * @param spalte
	 * @return
	 */
	public synchronized static boolean mauerDa(int reihe, int spalte) {
		return ((Boolean) processor.process(new TerrainCellInstruction(
				TerrainCellInstruction.IS_WALL, reihe, spalte))).booleanValue();
	}
	/**
	 * liefert die Gesamtzahl an Koernern, die im Territorium auf Kacheln
	 * herumliegen
	 * 
	 * @return
	 */
	public synchronized static int getAnzahlKoerner() {
		return ((Integer) processor.process(new TerrainInstruction(
				TerrainInstruction.COUNT_CORN))).intValue();
	}

	/**
	 * liefert die Anzahl an Koernern auf der angegebenen Kachel oder 0, falls
	 * die Kachel nicht existiert oder durch eine Mauer blockiert ist
	 * 
	 * @param reihe
	 * @param spalte
	 * @return
	 */
	public synchronized static int getAnzahlKoerner(int reihe, int spalte) {
		return ((Integer) processor.process(new TerrainCellInstruction(
				TerrainCellInstruction.COUNT_CORN, reihe, spalte))).intValue();
	}

	/**
	 * liefert die Gesamtzahl an erzeugten und initialisierten Hamstern im
	 * Territorium (inkl. dem Standard-Hamster)
	 * 
	 * @return
	 */
	public synchronized static int getAnzahlHamster() {
		return ((Integer) processor.process(new TerrainInstruction(
				TerrainInstruction.COUNT_HAMSTER))).intValue();
	}

	/**
	 * liefert alle erzeugten und initialisierten Hamster im Territorium (inkl.
	 * dem Standard-Hamster)
	 * 
	 * @return
	 */
	public synchronized static Hamster[] getHamster() {
		ArrayList ids = (ArrayList) processor.process(new TerrainInstruction(
				TerrainInstruction.GET_HAMSTER));
		Hamster[] result = new Hamster[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			result[i] = (Hamster) hamster.get(ids.get(i));
		}
		return result;
	}

	/**
	 * Liefert die Anzahl an erzeugten und initialisierten Hamstern (inkl. dem
	 * Standard-Hamster) auf der angegebenen Kachel oder 0, falls die Kachel
	 * nicht existiert oder durch eine Mauer blockiert ist
	 * 
	 * @param reihe
	 * @param spalte
	 * @return
	 */
	public synchronized static int getAnzahlHamster(int reihe, int spalte) {
		return ((Integer) processor.process(new TerrainCellInstruction(
				TerrainCellInstruction.COUNT_HAMSTER, reihe, spalte)))
				.intValue();
	}

	/**
	 * Liefert alle erzeugten und initialisierten Hamster, die aktuell auf der
	 * angegebenen Kachel stehen (inkl. dem Standard-Hamster), oder eine leeres
	 * Array, falls sich kein Hamster auf der Kachel befindet, die Kachel nicht
	 * existiert oder durch eine Mauer blockiert ist
	 * 
	 * @param reihe
	 * @param spalte
	 * @return
	 */
	public synchronized static Hamster[] getHamster(int reihe, int spalte) {
		ArrayList ids = (ArrayList) processor
				.process(new TerrainCellInstruction(
						TerrainCellInstruction.GET_HAMSTER, reihe, spalte));
		Hamster[] result = new Hamster[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			result[i] = (Hamster) hamster.get(ids.get(i));
		}
		return result;
	}
	
	// dibo
	static Object[][] kacheln = null;
	public synchronized static Object getKachel(int reihe, int spalte) {
		if (kacheln == null) {
			kacheln = new Object[getAnzahlReihen()][getAnzahlSpalten()];
			for (int r = 0; r < kacheln.length; r++) {
				for (int s = 0; s < kacheln[r].length; s++) {
					kacheln[r][s] = new Object();
				}
			}
		}
		return kacheln[reihe][spalte];
	}

}