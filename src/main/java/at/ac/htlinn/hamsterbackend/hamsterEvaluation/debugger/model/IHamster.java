package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.*;

// dibo 27.06.2007
class HamsterThreadExcHandler implements Thread.UncaughtExceptionHandler {

	private IHamster ham;

	public HamsterThreadExcHandler(IHamster ham) {
		this.ham = ham;
	}

	public void uncaughtException(Thread t, Throwable e) {
		if (!(e instanceof java.lang.ThreadDeath)) {
			ham.schreib("$_dibo_intern$" + e);
		}
	}
}

/**
 * Dies ist die Oberklasse eines imperativen Hamsterprogramms. Ein imperatives
 * Hamsterprogramm steuert immer den Standardhamster und unterstuetzt dabei die
 * Befehle vor, linksUm, nimm, gib, vornFrei, maulLeer und kornDa. Ausserdem
 * kann ein imperatives Hamsterprogramm jetzt auch ueber die Methoden schreib,
 * liesZahl und liesZeichenkette eine gewisse Ein- und Ausgabe ermoeglichen.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.2 $
 */
public class IHamster extends Thread {
	/**
	 * Der InstructionProcessor
	 */
	 static InstructionProcessor processor;
	
	public static int $i$defColor = Hamster.BLUE;

	/**
	 * Ein imperativer Hamster hat immer die ID -1
	 */
	int id = -1;
	
	public int $i$color = $i$defColor;

	public static boolean parallel = false; // dibo 20.04.2005

	// dibo 27.6.2007

	// dibo 20.04.2005
	public void start() {
		this.parallel = true;
		HamsterThreadExcHandler eh = new HamsterThreadExcHandler(this);
		this.setUncaughtExceptionHandler(eh);
		super.start();
	}

	// dibo 20.04.2005
	private void _intern_sleep_200405() {
		try {
			if (!this.parallel) {
				return;
			}
			_intern_verzoegern();
			// int msec = (int) (Math.random() * 1000 * Thread.NORM_PRIORITY
			// /
			// this
			// .getPriority());
			// Thread.sleep(0); // dibo to change msec);
		} catch (Exception exc) {
			interrupt();
		}
	}

	// dibo 210807
	private void _intern_verzoegern() {
		
		int z = (int) (Math.random() * (2 + Thread.MAX_PRIORITY - this
				.getPriority()));
		if (z == 0) {
			schreib("$_dibo_p_intern$");
		}
		
	}

	public synchronized void vor() {
		Object o = processor.process(new HamsterInstruction(
				HamsterInstruction.FORWARD, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		if (o instanceof MauerDaException) {
			MauerDaException e = (MauerDaException) o;
			throw new WallInFrontException(new MauerDaException(null, e
					.getReihe(), e.getSpalte()));
		}
	}

	public synchronized void linksUm() {
		processor.process(new HamsterInstruction(HamsterInstruction.TURN_LEFT,
				id));
		_intern_sleep_200405(); // dibo 20.04.2005
	}

	public synchronized void nimm() {
		Object o = processor.process(new HamsterInstruction(
				HamsterInstruction.PICK_UP, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		if (o instanceof KachelLeerException) {
			KachelLeerException e = (KachelLeerException) o;
			throw new TileEmptyException(new KachelLeerException(null, e
					.getReihe(), e.getSpalte()));
		}
	}

	public synchronized void gib() {
		Object o = processor.process(new HamsterInstruction(
				HamsterInstruction.LAY_DOWN, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		if (o instanceof MaulLeerException)
			throw new MouthEmptyException(new MaulLeerException(null));
	}

	public synchronized boolean vornFrei() {
		Boolean b = (Boolean) processor.process(new HamsterInstruction(
				HamsterInstruction.FREE, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		return b.booleanValue();
	}

	public synchronized boolean maulLeer() {
		Boolean b = (Boolean) processor.process(new HamsterInstruction(
				HamsterInstruction.MOUTH_EMPTY, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		return b.booleanValue();
	}

	public synchronized boolean kornDa() {
		Boolean b = (Boolean) processor.process(new HamsterInstruction(
				HamsterInstruction.CORN_AVAILABLE, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		return b.booleanValue();
	}

	public synchronized void schreib(String text) {
		processor.process(new TerminalInstruction(TerminalInstruction.WRITE,
				id, text));
		if (!text.contains("$_dibo_p_intern$")) {
			_intern_sleep_200405(); // dibo 20.04.2005
		}
	}

	public synchronized int liesZahl(String message) {
		Integer i = (Integer) processor.process(new TerminalInstruction(
				TerminalInstruction.READ_INT, id, message));
		_intern_sleep_200405(); // dibo 20.04.2005
		return i.intValue();
	}

	public synchronized String liesZeichenkette(String message) {
		String res = (String) processor.process(new TerminalInstruction(
				TerminalInstruction.READ_STRING, id, message));
		_intern_sleep_200405(); // dibo 20.04.2005
		return res;
	}

	public synchronized void move() {
		vor();
	}

	public synchronized void turnLeft() {
		linksUm();
	}

	public synchronized void pickGrain() {
		nimm();
	}

	public synchronized void putGrain() {
		gib();
	}

	public synchronized boolean frontIsClear() {
		return vornFrei();
	}

	public synchronized boolean grainAvailable() {
		return kornDa();
	}

	public synchronized boolean mouthEmpty() {
		return maulLeer();
	}

	public synchronized void write(String message) {
		schreib(message);
	}

	public synchronized int readNumber(String message) {
		return liesZahl(message);
	}

	public synchronized String readString(String message) {
		return liesZeichenkette(message);
	}

	public int get$i$color() {
		return $i$color;
	}

}
