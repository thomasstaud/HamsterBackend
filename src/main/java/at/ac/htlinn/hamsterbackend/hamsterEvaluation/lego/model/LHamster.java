package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.*;

/**
 * Diese Klasse erm�glicht es, Lego-Hamster Programme auch im Simulator zu
 * starten. Ist identisch mit der Klasse IHamster, bis auf die Ein- und
 * Ausgabemethoden. Sie erbt aber nicht von dieser, da sie die Methoden im
 * Simulationsmodel selbst aufrufen muss und die Ein- und Ausgabemethoden nicht
 * nutzbar sein sollen.
 *
 * @author Christian Rolfes
 */
public class LHamster extends Thread {
	/**
	 * Ein LegoHamster hat immer die ID -2
	 */
	int id = -2;

	public LHamster() {
		super();
	}

	/**
	 * Der InstructionProcessor
	 */
	public static InstructionProcessor processor;

	public boolean parallel = false; // dibo 20.04.2005

	// dibo 20.04.2005
	public void start() {
		this.parallel = true;
		super.start();
	}

	// dibo 20.04.2005
	private void _intern_sleep_200405() {
		if (!this.parallel) {
			return;
		}
		int msec = (int) (Math.random() * 1000 * Thread.NORM_PRIORITY / this.getPriority());
//			Thread.sleep(0); // dibo to change msec);

	}

	public void vor() {
		Object o = processor.process(new HamsterInstruction(HamsterInstruction.FORWARD, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		if (o instanceof MauerDaException) {
			MauerDaException e = (MauerDaException) o;
			throw new WallInFrontException(new MauerDaException(null, e.getReihe(), e.getSpalte()));
		} else if (o instanceof KornDaException) {
			KornDaException e = (KornDaException) o;
			throw new CornThereException(new KornDaException(null, e.getReihe(), e.getSpalte()));
		}
	}

	public void linksUm() {
		processor.process(new HamsterInstruction(HamsterInstruction.TURN_LEFT, id));
		_intern_sleep_200405(); // dibo 20.04.2005
	}

	public void nimm() {
		Object o = processor.process(new HamsterInstruction(HamsterInstruction.PICK_UP, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		if (o instanceof KachelLeerException) {
			KachelLeerException e = (KachelLeerException) o;
			throw new TileEmptyException(new KachelLeerException(null, e.getReihe(), e.getSpalte()));
		} else if (o instanceof MaulNichtLeerException) {
			MaulNichtLeerException e = (MaulNichtLeerException) o;
			throw new MouthNotEmptyException(new MaulNichtLeerException(null));
		} else if (o instanceof MauerDaException) {
			MauerDaException e = (MauerDaException) o;
			throw new WallInFrontException(new MauerDaException(null, e.getReihe(), e.getSpalte()));
		}
	}

	public void gib() {
		Object o = processor.process(new HamsterInstruction(HamsterInstruction.LAY_DOWN, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		if (o instanceof MaulLeerException) {
			throw new MouthEmptyException(new MaulLeerException(null));
		} else if (o instanceof MauerDaException) {
			MauerDaException e = (MauerDaException) o;
			throw new WallInFrontException(new MauerDaException(null, e.getReihe(), e.getSpalte()));
		} else if (o instanceof KornDaException) {
			KornDaException e = (KornDaException) o;
			throw new CornThereException(new KornDaException(null, e.getReihe(), e.getSpalte()));
		}
	}

	public boolean kornDa() {
		Boolean b = (Boolean) processor.process(new HamsterInstruction(HamsterInstruction.CORN_AVAILABLE, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		return b.booleanValue();
	}

	public boolean vornFrei() {
		Boolean b = (Boolean) processor.process(new HamsterInstruction(HamsterInstruction.FREE, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		return b.booleanValue();
	}

	public boolean maulLeer() {
		Boolean b = (Boolean) processor.process(new HamsterInstruction(HamsterInstruction.MOUTH_EMPTY, id));
		_intern_sleep_200405(); // dibo 20.04.2005
		return b.booleanValue();
	}

	public void move() {
		vor();
	}

	public void turnLeft() {
		linksUm();
	}

	public void pickGrain() {
		nimm();
	}

	public void putGrain() {
		gib();
	}

	public boolean frontIsClear() {
		return vornFrei();
	}

	public boolean grainAvailable() {
		return kornDa();
	}

	public boolean mouthEmpty() {
		return maulLeer();
	}

}
