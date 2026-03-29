package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.*;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;

/**
 * Dies ist die Oberklasse eines objektorientierten Hamsters.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.2 $
 */
public class Hamster extends IHamster {
	private int reihe;

	private int spalte;

	private int anzahlKoerner;

	private int blickrichtung;

	private boolean initialisiert = false;

	static int count = 0;

	static Hamster standard;

	public final static int NORD = 0;

	public final static int OST = 1;

	public final static int SUED = 2;

	public final static int WEST = 3;

	public final static int NORTH = 0;

	public final static int EAST = 1;

	public final static int SOUTH = 2;

	// Farben

	public final static int MIN_COLOR = 0;
	public final static int MAX_COLOR = 9;

	public final static int BLAU = 0;
	public final static int BLUE = 0;

	public final static int ROT = 1;
	public final static int RED = 1;

	public final static int GRUEN = 2;
	public final static int GREEN = 2;

	public final static int GELB = 3;
	public final static int YELLOW = 3;

	public final static int CYAN = 4;

	public final static int MAGENTA = 5;

	public final static int ORANGE = 6;

	public final static int PINK = 7;

	public final static int GRAU = 8;
	public final static int GRAY = 8;

	public final static int WEISS = 9;
	public final static int WHITE = 9;

	static void setProcessor(InstructionProcessor p) {
		processor = p;
	}

	public synchronized static Hamster getStandardHamster() {
		if (standard == null) {
			standard = new Hamster();
			standard.id = -1;
			standard.$i$color = $i$defColor;
			Territorium.addHamster(standard);
			standard.initialisiert = true;
			int[] data = (int[]) processor.process(new HamsterInstruction(
					HamsterInstruction.GET_DATA, -1));
			standard.reihe = data[0];
			standard.spalte = data[1];
			standard.blickrichtung = data[2];
			standard.anzahlKoerner = data[3];
		}
		return standard;
	}

	public synchronized static Hamster getDefaultHamster() {
		return getStandardHamster();
	}

	public synchronized static int getAnzahlHamster() {
		return Territorium.getAnzahlHamster();
	}

	public synchronized static int getNumberOfHamsters() {
		return getAnzahlHamster();
	}

	public Hamster() {
	}

	public Hamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner) {
		init(reihe, spalte, blickrichtung, anzahlKoerner);
	}

	public Hamster(Hamster h) {
		if (!h.initialisiert) {
			this.initialisiert = false;
		} else {
			init(h.reihe, h.spalte, h.blickrichtung, h.anzahlKoerner,
					h.$i$color);
		}
	}

	protected Hamster clone() {
		return new Hamster(this);
	}

	public Hamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner,
			int farbe) {
		init(reihe, spalte, blickrichtung, anzahlKoerner, farbe);
	}

	public synchronized void init(int reihe, int spalte, int blickrichtung,
			int anzahlKoerner) {
		init(reihe, spalte, blickrichtung, anzahlKoerner, -1);
	}

	public synchronized void init(int reihe, int spalte, int blickrichtung,
			int anzahlKoerner, int farbe) {

		if (initialisiert)
			return;
		id = count++;
		if (farbe < -1 || farbe > MAX_COLOR)
			farbe = -1;
		this.$i$color = farbe;
		Territorium.addHamster(this);
		Object o = processor.process(new CreateInstruction(id, reihe, spalte,
				blickrichtung, anzahlKoerner, this.$i$color));
		if (o instanceof HamsterInitialisierungsException) {
			throw new HamsterInitializationException(
					new HamsterInitialisierungsException(this));
		} else {
			initialisiert = true;
			this.reihe = reihe;
			this.spalte = spalte;
			this.blickrichtung = blickrichtung;
			this.anzahlKoerner = anzahlKoerner;
		}
	}

	public synchronized void vor() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		try {
			super.vor();
			switch (blickrichtung) {
			case NORD:
				reihe--;
				break;
			case OST:
				spalte++;
				break;
			case SUED:
				reihe++;
				break;
			case WEST:
				spalte--;
				break;
			}
		} catch (MauerDaException e) {
			e.setHamster(this);
			throw new WallInFrontException(e);
		}
	}

	public synchronized void linksUm() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		super.linksUm();
		blickrichtung = (blickrichtung + 3) % 4;
	}

	public synchronized void nimm() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		try {
			super.nimm();
			anzahlKoerner++;
		} catch (KachelLeerException e) {
			e.setHamster(this);
			throw new TileEmptyException(e);
		}
	}

	public synchronized void gib() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		try {
			super.gib();
			anzahlKoerner--;
		} catch (MaulLeerException e) {
			e.setHamster(this);
			throw new MouthEmptyException(e);
		}
	}

	public synchronized boolean vornFrei() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return super.vornFrei();
	}

	public synchronized boolean kornDa() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return super.kornDa();
	}

	public synchronized boolean maulLeer() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return super.maulLeer();
	}

	public synchronized int getReihe() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return ((Integer) processor.process(new HamsterInstruction(
				HamsterInstruction.GET_ROW, id))).intValue();
	}

	public synchronized int getRow() {
		return getReihe();
	}

	public synchronized int getSpalte() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return ((Integer) processor.process(new HamsterInstruction(
				HamsterInstruction.GET_COL, id))).intValue();
	}

	public synchronized int getColumn() {
		return getSpalte();
	}

	public synchronized int getBlickrichtung() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return ((Integer) processor.process(new HamsterInstruction(
				HamsterInstruction.GET_DIR, id))).intValue();
	}

	public synchronized int getDirection() {
		return getBlickrichtung();
	}

	public synchronized int getAnzahlKoerner() {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return ((Integer) processor.process(new HamsterInstruction(
				HamsterInstruction.GET_MOUTH, id))).intValue();
	}

	public synchronized int getNumberOfGrains() {
		return getAnzahlKoerner();
	}

	// Methode, die ueberprueft, ob die Werte der Attribute des aufgerufenen
	// Hamsters gleich der Attributwerte des uebergebenen Hamsters sind (zwei
	// nicht initialisierte Hamster sind auch gleich) hamster: muss ein Objekt
	// der Klasse Hamster oder einer davon abgeleiteten Klasse sein
	// (ueberschreibt die entsprechende von der Klasse Object geerbte Methode)
	// dibo (falsch)
	// public synchronized boolean equals(Object o) {
	// if (o == null)
	// return false;
	// if (!(o instanceof Hamster))
	// return false;
	// Hamster h = (Hamster) o;
	// if (!h.initialisiert && !initialisiert)
	// return true;
	// else if (!initialisiert)
	// return false;
	// else if (!h.initialisiert)
	// return false;
	// return h.reihe == reihe && h.spalte == spalte
	// && h.anzahlKoerner == anzahlKoerner
	// && h.blickrichtung == blickrichtung;
	// }

	// (ueberschreibt die entsprechende von der Klasse Object geerbte Methode)
	// public synchronized int hashCode() {
	// if (!initialisiert)
	// return 0;
	// int hash = 7;
	// hash = 31 * hash + this.reihe;
	// hash = 31 * hash + this.spalte;
	// hash = 31 * hash + this.blickrichtung;
	// hash = 31 * hash + this.anzahlKoerner;
	// return hash;
	// }

	// Methode, die eine String-Repraesentation der folgenden Art fuer den
	// aufgerufenen Hamster liefert:
	// "Hamster steht auf Kachel (0/0) mit Blickrichtung OST und 2 Koernern im
	// Maul"
	// Wenn der aufgerufene Hamster noch nicht initialisiert ist, wird
	// folgender String geliefert: "Hamster ist nicht initialisiert"
	// (ueberschreibt die entsprechende von der Klasse Object geerbte Methode)
	public synchronized String toString() {
		if (Utils.language.equals("en")) {
			if (!initialisiert) {
				return "Hamster has not been initialized yet";
			}
			String[] namen = new String[] { "NORTH", "EAST", "SOUTH", "WEST" };
			String s = "Hamster on tile (" + reihe + "/" + spalte + ") ";
			s += "looking in direction " + namen[blickrichtung] + " with ";
			s += anzahlKoerner + (anzahlKoerner == 1 ? " corn" : " corn");
			return s + " in its mouth";
		} else {
			if (!initialisiert) {
				return "Hamster ist nicht initialisiert";
			}
			String[] namen = new String[] { "NORD", "OST", "SUED", "WEST" };
			String s = "Hamster steht auf Kachel (" + reihe + "/" + spalte
					+ ") ";
			s += "mit Blickrichtung " + namen[blickrichtung] + " und ";
			s += anzahlKoerner + (anzahlKoerner == 1 ? " Korn" : " Koernern");
			return s + " im Maul";
		}
	}

	public synchronized void schreib(String text) {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		super.schreib(text);
	}

	public synchronized int liesZahl(String message) {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return super.liesZahl(message);
	}

	public synchronized String liesZeichenkette(String message) {
		if (!initialisiert)
			throw new HamsterNotInitializedException(
					new HamsterNichtInitialisiertException(this));
		return super.liesZeichenkette(message);
	}
}
