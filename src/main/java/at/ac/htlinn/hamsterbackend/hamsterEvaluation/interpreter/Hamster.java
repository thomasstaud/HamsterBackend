package at.ac.htlinn.hamsterbackend.hamsterEvaluation.interpreter;


import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;

import java.util.ArrayList;


/**
 * 
 * @author Dietrich Boles (Universitaet Oldenburg)
 * @version 1.0 (25.01.2006)
 * 
 */
public class Hamster {

	static ArrayList _intern_hamsters = new ArrayListIntern();

	private static Hamster _intern_standardHamster = new Hamster(false);

	private static int _intern_ID = 0;

	/**
	 * Blickrichtung Nord
	 */
	public final static int NORD = 0;

	/**
	 * Blickrichtung Ost
	 */
	public final static int OST = 1;

	/**
	 * Blickrichtung Sued
	 */
	public final static int SUED = 2;

	/**
	 * Blickrichtung West
	 */
	public final static int WEST = 3;

	public static void _re_init() {
		ArrayList<Hamster> hams = new ArrayList<Hamster>();
		int no = _intern_hamsters.size();
		for (int i = 0; i < no; i++) {
			Hamster h = (Hamster) _intern_hamsters.get(i);
			if (h._intern_id != -1) {
				hams.add(h);
			}
		}
		no = hams.size();
		for (int i = 0; i < no; i++) {
			Hamster ham = hams.get(i);
			_intern_hamsters.remove(ham);
			Workbench.getWorkbench().getSimulationController().getSimulationModel().removeHamster(ham._intern_id);
			ham._intern_init = false;
		}
		_intern_ID = 0;
		Workbench.getWorkbench().getSimulationController().getSimulationModel().clearLogs(); // dibo 290710
	}

	/**
	 * Konstruktor zum Erzeugen eines nicht initialisierten Hamsters
	 */
	public Hamster() {
		this._intern_init = false;
	}

	/**
	 * Konstruktor zum Erzeugen und Initialisieren eines Hamsters mit den
	 * uebergebenen Parametern
	 * 
	 * @param reihe         die Reihe des Territoriums, in der der Hamster erzeugt
	 *                      wird
	 * @param spalte        die Spalte des Territoriums, in der der Hamster erzeugt
	 *                      wird
	 * @param blickrichtung die Richtung, in der der Hamster anfangs schaut (siehe
	 *                      Konstanten)
	 * @param anzahlKoerner die Anzahl an Koernern, die der Hamster anfangs im Maul
	 *                      hat
	 * @throws HamsterInitialisierungsException wird geworfen, wenn: (a) eine Kachel
	 *                                          (reihe/spalte) nicht existiert (b)
	 *                                          die Kachel (reihe/spalte) durch eine
	 *                                          Mauer blockiert ist (c) der Wert von
	 *                                          blickrichtung nicht zwischen 0 und 3
	 *                                          liegt (d) der Wert von anzahlKoerner
	 *                                          < 0 ist
	 */
	public Hamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner)
			throws HamsterInitialisierungsException {
		this.init(reihe, spalte, blickrichtung, anzahlKoerner);
	}

	/**
	 * Konstruktor zum Erzeugen und Initialisieren eines Hamsters mit den Werten
	 * eines bereis existierenden Hamsters
	 * 
	 * @param hamster ein bereits existierender Hamster
	 */
	public Hamster(Hamster hamster) {
		if (hamster == null || !hamster._intern_init) {
			this._intern_init = false;
		} else {
			this.init(hamster.getReihe(), hamster.getSpalte(), hamster.getBlickrichtung(), hamster.getAnzahlKoerner());
		}
	}

	/**
	 * Methode zum Initialisieren eines noch nicht initialsierten Hamsters. Der
	 * Aufruf der Methode fuer einen bereits initialisierten Hamster bewirkt nichts.
	 * 
	 * @param reihe         die Reihe des Territoriums, in der der Hamster erzeugt
	 *                      wird
	 * @param spalte        die Spalte des Territoriums, in der der Hamster erzeugt
	 *                      wird
	 * @param blickrichtung die Richtung, in der der Hamster anfangs schaut (siehe
	 *                      Konstanten)
	 * @param anzahlKoerner die Anzahl an Koernern, die der Hamster anfangs im Maul
	 *                      hat
	 * @throws HamsterInitialisierungsException wird geworfen, wenn: (a) eine Kachel
	 *                                          (reihe/spalte) nicht existiert (b)
	 *                                          die Kachel (reihe/spalte) durch eine
	 *                                          Mauer blockiert ist (c) der Wert von
	 *                                          blickrichtung nicht zwischen 0 und 3
	 *                                          liegt (d) der Wert von anzahlKoerner
	 *                                          < 0 ist
	 */
	public void init(int reihe, int spalte, int blickrichtung, int anzahlKoerner)
			throws HamsterInitialisierungsException {
		try {
			if (_intern_init)
				return;
			this._intern_init = false;
			if (this._intern_id == -2) {
				this._intern_id = _intern_ID++;
			}
			Workbench.getWorkbench().getSimulationController().getSimulationModel().newHamster(this._intern_id, reihe,
					spalte, blickrichtung, anzahlKoerner, -1);
			this._intern_init = true;
			_intern_hamsters.add(this);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterInitialisierungsException(this);
		} finally {
			sleep();
		}
	}

	/**
	 * Der aufgerufene Hamster springt auf die in Blickrichtung vor ihm liegende
	 * Kachel.
	 * 
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 * @throws MauerDaException                   wird geworfen, wenn die Kachel in
	 *                                            Blickrichtung vor dem Hamster
	 *                                            durch eine Mauer blockiert ist
	 *                                            oder der Hamster in Blickrichtung
	 *                                            am Rand des Territoriums steht
	 */
	public void vor() throws HamsterNichtInitialisiertException, MauerDaException {
		try {
			checkInit("vor()");
			Workbench.getWorkbench().getSimulationController().getSimulationModel().forward(this._intern_id);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.MauerDaException exc) {
			throw new MauerDaException(this, exc.getReihe(), exc.getSpalte());
		} finally {
			sleep();
		}
	}

	/**
	 * Der aufgerufene Hamster dreht sich linksum.
	 * 
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public void linksUm() throws HamsterNichtInitialisiertException {

		try {
			checkInit("linksUm()");
			Workbench.getWorkbench().getSimulationController().getSimulationModel().turnLeft(this._intern_id);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} finally {
			sleep();
		}
	}

	/**
	 * Der aufgerufene Hamster legt ein Korn auf der Kachel ab, auf der er sich
	 * gerade befindet.
	 * 
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 * @throws MaulLeerException                  wird geworfen, wenn der Hamster
	 *                                            keine Koerner im Maul hat
	 */
	public void gib() throws HamsterNichtInitialisiertException, MaulLeerException {
		try {
			checkInit("gib()");
			Workbench.getWorkbench().getSimulationController().getSimulationModel().layDown(this._intern_id);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.MaulLeerException exc) {
			throw new MaulLeerException(this);
		} finally {
			sleep();
		}
	}

	/**
	 * Der aufgerufene Hamster frisst ein Korn auf der Kachel, auf der er sich
	 * gerade befindet.
	 * 
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 * @throws KachelLeerException                wird geworfen, wenn auf der
	 *                                            Kachel, auf der sich der Hamster
	 *                                            gerade befindet, kein Korn liegt
	 */
	public void nimm() throws HamsterNichtInitialisiertException, KachelLeerException {
		try {
			checkInit("nimm()");
			Workbench.getWorkbench().getSimulationController().getSimulationModel().pickUp(this._intern_id);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.KachelLeerException exc) {
			throw new KachelLeerException(this, exc.getReihe(), exc.getSpalte());
		} finally {
			sleep();
		}
	}

	/**
	 * liefert genau dann true, wenn sich in Blickrichtung vor dem aufgerufenen
	 * Hamster keine Mauer befindet (wenn sich der Hamster in Blickrichtung am Rand
	 * des Territoriums befindet, wird false geliefert)
	 * 
	 * @return true, wenn sich in Blickrichtung vor dem aufgerufenen Hamster keine
	 *         Mauer befindet; sonst false
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public boolean vornFrei() throws HamsterNichtInitialisiertException {
		try {
			checkInit("vornFrei()");
			boolean res = Workbench.getWorkbench().getSimulationController().getSimulationModel().free(this._intern_id);
			return res;
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} finally {
			sleep();
		}
	}

	/**
	 * liefert genau dann true, wenn der aufgerufene Hamster keine Koerner im Maul
	 * hat
	 * 
	 * @return true, wenn der aufgerufene Hamster keine Koerner im Maul hat; sonst
	 *         false
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public boolean maulLeer() throws HamsterNichtInitialisiertException {
		try {
			checkInit("maulLeer()");
			boolean res = Workbench.getWorkbench().getSimulationController().getSimulationModel()
					.mouthEmpty(this._intern_id);
			return res;
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} finally {
			sleep();
		}
	}

	/**
	 * liefert genau dann true, wenn auf der Kachel, auf der sich der aufgerufene
	 * Hamster gerade befindet, mindestens ein Korn liegt
	 * 
	 * @return true, wenn auf der Kachel, auf der sich der aufgerufene Hamster
	 *         gerade befindet, mindestens ein Korn liegt; sonst false
	 * @throws HamsterNichtInitialisiertException
	 */
	public boolean kornDa() throws HamsterNichtInitialisiertException {
		try {
			checkInit("kornDa()");
			boolean res = Workbench.getWorkbench().getSimulationController().getSimulationModel()
					.cornAvailable(this._intern_id);
			return res;
		} catch (at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterNichtInitialisiertException exc) {
			throw new HamsterNichtInitialisiertException(this);
		} finally {
			sleep();
		}
	}

	/**
	 * gibt den uebergebenen String (in einer Dialogbox) auf den Bildschirm aus
	 * 
	 * @param zeichenkette der auszugebende String
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public void schreib(String zeichenkette) throws HamsterNichtInitialisiertException {
		try {
			checkInit("schreib(\"" + zeichenkette + "\")");
		} finally {
			sleep();
		}
	}


	/**
	 * liefert die Reihe der Kachel des Territoriums, auf der sich der aufgerufene
	 * Hamster gerade befindet
	 * 
	 * @return die Reihe der Kachel des Territoriums, auf der sich der aufgerufene
	 *         Hamster gerade befindet
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch // nicht initialisiert worden
	 *                                            ist
	 */
	public int getReihe() throws HamsterNichtInitialisiertException {
		try {
			checkInit("getReihe()");
			at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Hamster h = Workbench.getWorkbench().getSimulationController()
					.getSimulationModel().getHamster(this._intern_id);
			int res = h.getY();
			return res;
		} finally {
			sleep();
		}
	}

	/**
	 * liefert die Spalte der Kachel des Territoriums, auf der sich der aufgerufene
	 * Hamster gerade befindet
	 * 
	 * @return die Spalte der Kachel des Territoriums, auf der sich der aufgerufene
	 *         Hamster gerade befindet
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public int getSpalte() throws HamsterNichtInitialisiertException {
		try {
			checkInit("getSpalte()");
			at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Hamster h = Workbench.getWorkbench().getSimulationController()
					.getSimulationModel().getHamster(this._intern_id);
			int res = h.getX();
			return res;
		} finally {
			sleep();
		}
	}

	/**
	 * liefert die Blickrichtung, in die der aufgerufene Hamster gerade schaut (die
	 * gelieferten Werte entsprechen den obigen Konstanten)
	 * 
	 * @return die Blickrichtung, in die der aufgerufene Hamster gerade schaut
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public int getBlickrichtung() throws HamsterNichtInitialisiertException {
		try {
			checkInit("getBlickrichtung()");
			at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Hamster h = Workbench.getWorkbench().getSimulationController()
					.getSimulationModel().getHamster(this._intern_id);
			int res = h.getDir();
			return res;
		} finally {
			sleep();
		}
	}

	/**
	 * liefert die Anzahl der Koerner, die der aufgerufene Hamster gerade im Maul
	 * hat
	 * 
	 * @return die Anzahl der Koerner, die der aufgerufene Hamster gerade im Maul
	 *         hat
	 * @throws HamsterNichtInitialisiertException wird geworfen, wenn der Hamster
	 *                                            noch nicht initialisiert worden
	 *                                            ist
	 */
	public int getAnzahlKoerner() throws HamsterNichtInitialisiertException {
		try {
			checkInit("getAnzahlKoerner()");
			at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Hamster h = Workbench.getWorkbench().getSimulationController()
					.getSimulationModel().getHamster(this._intern_id);
			int res = h.getMouth();
			return res;
		} finally {
			sleep();
		}
	}

	/**
	 * liefert den Standard-Hamster, das ist der Hamster, der sich standardmaessig
	 * im Territorium befindet, ohne explizit erzeugt werden zu muessen
	 * 
	 * @return der Standard-Hamster
	 */
	public static Hamster getStandardHamster() {
		return _intern_standardHamster;
	}

	public static Hamster getStandardHamsterIntern() {
		return _intern_standardHamster;
	}

	/**
	 * liefert die Gesamtzahl an erzeugten und initialisierten Hamstern im
	 * Territorium (inkl. dem Standard-Hamster)
	 * 
	 * @return die Gesamtzahl an erzeugten und initialisierten Hamstern im
	 *         Territorium
	 */
	public static int getAnzahlHamster() {
		int res = _intern_hamsters.size();
		return res;
	}

	/**
	 * Methode, die einen Klon des aufgerufenen Hamsters erzeugt und liefert, d.h.
	 * die Werte der Attribute des neuen Hamsters sind identisch zu den Werten des
	 * aufgerufenen Hamsters. Wenn der aufgerufene Hamster noch nicht initialisiert
	 * ist, wird der neu erzeugte Hamster auch nicht initialisiert (ueberschreibt
	 * die entsprechende von der Klasse Object geerbte Methode).
	 * 
	 * @return ein Klon des aufgerufenen Hamsters
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		if (!this._intern_init) {
			return new Hamster();
		}
		return new Hamster(this);
	}

	/**
	 * Methode, die ueberprueft, ob die Werte der Attribute des aufgerufenen
	 * Hamsters gleich der Attributwerte des uebergebenen Hamsters sind (zwei nicht
	 * initialisierte Hamster sind auch gleich) (ueberschreibt die entsprechende von
	 * der Klasse Object geerbte Methode)
	 * 
	 * @param hamster muss ein Objekt der Klasse Hamster oder einer davon
	 *                abgeleiteten Klasse sein
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	// public boolean equals(Object hamster) {
	// boolean res = false;
	// if (hamster == null || !(hamster instanceof Hamster)) {
	// res = false;
	// } else {
	// Hamster ham = (Hamster) hamster;
	// if (!ham._intern_init) {
	// res = this._intern_init == ham._intern_init;
	// } else {
	// hamsterEvaluation.simulation.at.ac.htlinn.hamsterbackend.model.Hamster hThis = _intern_simulationModel
	// .getHamster(this._intern_id);
	// hamsterEvaluation.simulation.at.ac.htlinn.hamsterbackend.model.Hamster hOther = _intern_simulationModel
	// .getHamster(ham._intern_id);
	// res = this._intern_init == ham._intern_init
	// && hThis.getY() == hOther.getY()
	// && hThis.getX() == hOther.getX()
	// && hThis.getDir() == hOther.getDir()
	// && hThis.getMouth() == hOther.getMouth();
	// }
	// }
	// _intern_logPanel.logEntry("equals(hamster.Hamster)", (res ? "true"
	// : "false"), true, this._intern_id);
	// return res;
	// }
	/**
	 * Methode, die eine String-Repraesentation der folgenden Art fuer den
	 * aufgerufenen Hamster liefert: "Hamster steht auf Kachel (0/0) mit
	 * Blickrichtung OST und 2 Koernern im Maul" Wenn der aufgerufene Hamster noch
	 * nicht initialisiert ist, wird folgender String geliefert: "Hamster ist nicht
	 * initialisiert" (ueberschreibt die entsprechende von der Klasse Object geerbte
	 * Methode)
	 * 
	 * @return eine String-Repraesentation des aktuellen Hamster-Zustands
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String res = "";
		if (!this._intern_init) {
			res = "Hamster ist nicht initialisiert";
		} else {
			int a = 0;
			String b = "";
			at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Hamster hThis = Workbench.getWorkbench().getSimulationController()
					.getSimulationModel().getHamster(this._intern_id);
			switch (hThis.getDir()) {
			case NORD:
				b = "NORD";
				break;
			case SUED:
				b = "SUED";
				break;
			case OST:
				b = "OST";
				break;
			case WEST:
				b = "WEST";
				break;
			}

			res = "Hamster steht auf Kachel " + "(" + hThis.getY() + "/" + hThis.getX() + ") mit Blickrichtung " + b
					+ " und " + (a = hThis.getMouth()) + (a == 1 ? " Korn" : " Koernern") + " im Maul";
		}
		return res;
	}

	/**
	 * Der aufgerufene Hamster wird geloescht, d.h. er wird vom Bildschirm entfernt
	 * und seine Initialisierung wird rueckgaengig gemacht. Durch Aufruf der
	 * init-Methode kann der Hamster wieder reanimiert werden. Wird die Methode fuer
	 * den Standard-Hamster aufgerufen, passiert nichts.
	 * 
	 * Achtung: Die Methode gehoert nicht zum Standard-Hamster-Modell!
	 * 
	 */
	public void loeschen() {
		try {

			if (this == _intern_standardHamster || this._intern_init == false) {
				return;
			}
			_intern_hamsters.remove(this);
			Workbench.getWorkbench().getSimulationController().getSimulationModel().removeHamster(this._intern_id);
			this._intern_init = false;
		} finally {
			sleep();
		}
	}

	// private internal members

	private int _intern_id = -2;

	private boolean _intern_init = false;

	private void checkInit(String befehl) throws HamsterNichtInitialisiertException {
		if (!this._intern_init) {
			throw new HamsterNichtInitialisiertException(this);
		}
	}

	protected Hamster(boolean dummy) {
		this._intern_init = true;
		this._intern_id = -1;
		_intern_hamsters.add(this);
	}

	public void sleep() {
		// DO NOTHING
	}

	static class ArrayListIntern extends ArrayList {
		public boolean remove(Object obj) {
			for (int i = 0; i < this.size(); i++) {
				Object elem = this.get(i);
				if (elem == obj) {
					this.remove(i);
				}
			}
			return true;
		}
	}

}