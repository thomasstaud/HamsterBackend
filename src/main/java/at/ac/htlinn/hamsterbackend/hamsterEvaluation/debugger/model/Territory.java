package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;


/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class Territory {
	/**
	 * Damit keine Instanz der Klasse Territorium erzeugt wird.
	 */
	private Territory() {
	}

	/**
	 * liefert die Anzahl an Reihen im Territorium
	 * 
	 * @return
	 */
	public synchronized static int getNumberOfRows() {
		return Territorium.getAnzahlReihen();
	}

	/**
	 * liefert die Anzahl an Spalten im Territorium
	 * 
	 * @return
	 */
	public synchronized static int getNumberOfColumns() {
		return Territorium.getAnzahlSpalten();
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
	public synchronized static boolean wall(int reihe, int spalte) {
		return Territorium.mauerDa(reihe, spalte);
	}
	/**
	 * liefert die Gesamtzahl an Koernern, die im Territorium auf Kacheln
	 * herumliegen
	 * 
	 * @return
	 */
	public synchronized static int getNumberOfGrains() {
		return Territorium.getAnzahlKoerner();
	}

	/**
	 * liefert die Anzahl an Koernern auf der angegebenen Kachel oder 0, falls
	 * die Kachel nicht existiert oder durch eine Mauer blockiert ist
	 * 
	 * @param reihe
	 * @param spalte
	 * @return
	 */
	public synchronized static int getNumberOfGrains(int reihe, int spalte) {
		return Territorium.getAnzahlKoerner(reihe, spalte);
	}

	/**
	 * liefert die Gesamtzahl an erzeugten und initialisierten Hamstern im
	 * Territorium (inkl. dem Standard-Hamster)
	 * 
	 * @return
	 */
	public synchronized static int getNumberOfHamsters() {
		return Territorium.getAnzahlHamster();
	}

	/**
	 * liefert alle erzeugten und initialisierten Hamster im Territorium (inkl.
	 * dem Standard-Hamster)
	 * 
	 * @return
	 */
	public synchronized static Hamster[] getHamster() {
		return Territorium.getHamster();
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
	public synchronized static int getNumberOfHamsters(int reihe, int spalte) {
		return  Territorium.getAnzahlHamster(reihe, spalte);
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
		return Territorium.getHamster(reihe, spalte);
	}
	
	public synchronized static Object getTile(int reihe, int spalte) {
		return Territorium.getKachel(reihe, spalte);
	}

}