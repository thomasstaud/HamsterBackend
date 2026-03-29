package at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model.KornDaException;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model.MaulNichtLeerException;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.*;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;

/**
 * Dies ist das Model der Simulationskomponente. Es implementiert das
 * InstructionProcessor Interface, so dass es die Befehle von Hamsterprogrammen
 * ausfuehren kann. Die verschiedenen View-Komponenten melden sich ueber die
 * Observer-Schnittstelle an dieser Komponente an, so dass sie die jeweiligen
 * Veraenderungen darstellen koennen. Ausserdem koennen sich diverse LogSinks an
 * am SimulationModel anmelden, die entsprechende Logausgaben darstellen.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.3 $
 */
public class SimulationModel extends Observable implements InstructionProcessor {
	static final int[] DX = { 0, 1, 0, -1 };

	static final int[] DY = { -1, 0, 1, 0 };

	/**
	 * This argument notifies observers of a state change.
	 */
	public static final String STATE = "state change";

	/**
	 * This argument notifies observers of a change of the terrain.
	 */
	public static final String TERRAIN = "terrain";

	/**
	 * In this state, no simulation is running.
	 */
	public static final int NOT_RUNNING = 0;

	/**
	 * In this state, a simulation is running.
	 */
	public static final int RUNNING = 1;

	/**
	 * In this state, a simulation is running, but suspended.
	 */
	public static final int SUSPENDED = 2;

	public List<Integer> hamsterTurns;
	/**
	 * This array contains hamsters, that where constructed during the current
	 * simulation.
	 */
	private ArrayList<Hamster> hamster;

	/**
	 * This is the current terrain.
	 */
	private Terrain terrain;
	private boolean error = false;
	public Terrain savedTerrain;

	/**
	 * This is the current state of the simulation.
	 */
	private int state;

	private Terminal terminal;

	private ArrayList<LogSink> logSinks;

	public SimulationModel() {
		hamster = new ArrayList<Hamster>();
		terrain = new Terrain(14, 10); // dibo
		logSinks = new ArrayList<LogSink>();
		hamsterTurns = new ArrayList<Integer>();
	}

	public void addLogSink(LogSink sink) {
		logSinks.add(sink);
	}

	public void log(LogEntry entry) {
		for (int i = 0; i < logSinks.size(); i++) {
			((LogSink) logSinks.get(i)).logEntry(entry);
		}
	}

	public void clearLogs() {
		for (int i = 0; i < logSinks.size(); i++) {
			((LogSink) logSinks.get(i)).clearLog();
		}
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public ArrayList<Hamster> getHamster() {
		return hamster;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
		hamster = new ArrayList<Hamster>();
		setChanged();
		notifyObservers(TERRAIN);
	}

	public boolean containsHamster(int x, int y) {
		Hamster h = terrain.getDefaultHamster();
		if (h.getX() == x && h.getY() == y)
			return true;
		for (int i = 0; i < hamster.size(); i++) {
			h = (Hamster) hamster.get(i);
			if (h.getX() == x && h.getY() == y)
				return true;
		}
		return false;
	}

	public int getState() {
		return state;
	}

	public void removeHamster() {
		hamster.removeAll(hamster);
		setChanged();
		notifyObservers(TERRAIN);
	}

	// dibo
	public void removeHamster(int id) {
		Hamster ham = this.getHamster(id);
		hamster.remove(ham);
		setChanged();
		notifyObservers(TERRAIN);
	}

	public void start() {
		removeHamster();
		clearLogs();
		savedTerrain = new Terrain(terrain);
	}

	public void reset() {
		if (savedTerrain == null)
			return;
		terrain = savedTerrain;
		hamster = new ArrayList<Hamster>();
		setChanged();
		notifyObservers(TERRAIN);
		savedTerrain = null;
		// .hamsterEvaluation.workbench.Workbench.getWorkbench().getSimulation()
		// .getSimulationTools().getResetAction().setEnabled(false);

		// Prolog
	}

	public void finished() {
		if (!error) {
			setState(SimulationModel.NOT_RUNNING);
			for (int i = 0; i < hamsterTurns.size(); i++) {
				Workbench.getWorkbench().getJsonObject().put(String.valueOf(i), String.valueOf(hamsterTurns.get(i)));
			}
			Workbench.getWorkbench().getJsonObject().put("finished", "working");
		}
		error = false;
		hamsterTurns.clear();
		reset();
	}

	public synchronized Object processHamsterInstruction(HamsterInstruction instruction) {
		synchronized (terrain) { // dibo 120907
			int id = instruction.getHamster();
			if (instruction instanceof CreateInstruction) {
				CreateInstruction ci = (CreateInstruction) instruction;
				if (terrain.getWall(ci.getCol(), ci.getRow()) || ci.getDir() < 0 || ci.getDir() > 3
						|| ci.getMouth() < 0)
					throw new HamsterInitialisierungsException(null);
				hamster.add(new Hamster(id, ci.getCol(), ci.getRow(), ci.getDir(), ci.getMouth(), ci.getColor()));
				setChanged();
				notifyObservers(TERRAIN);
				return "ok";
			} else if (instruction instanceof TerminalInstruction) {
				TerminalInstruction ti = (TerminalInstruction) instruction;
				return "ok";
			} else {
				hamsterTurns.add(instruction.getType());
				switch (instruction.getType()) {
				case HamsterInstruction.FORWARD:
					return forward(id);
				case HamsterInstruction.TURN_LEFT:
					return turnLeft(id);
				case HamsterInstruction.LAY_DOWN:
					return layDown(id);
				case HamsterInstruction.PICK_UP:
					return pickUp(id);
				case HamsterInstruction.FREE:
					return new Boolean(free(id));
				case HamsterInstruction.CORN_AVAILABLE:
					return new Boolean(cornAvailable(id));
				case HamsterInstruction.MOUTH_EMPTY:
					return new Boolean(mouthEmpty(id));

				case HamsterInstruction.GET_ROW:
					return new Integer(getHamster(id).getY());
				case HamsterInstruction.GET_COL:
					return new Integer(getHamster(id).getX());
				case HamsterInstruction.GET_DIR:
					return new Integer(getHamster(id).getDir());
				case HamsterInstruction.GET_MOUTH:
					return new Integer(getHamster(id).getMouth());

				case HamsterInstruction.GET_DATA:
					return new int[] { getHamster(id).getY(), getHamster(id).getX(), getHamster(id).getDir(),
							getHamster(id).getMouth() };
				}
			}
			return null;
		}
	}

	public synchronized Object processTerrainCellInstruction(TerrainCellInstruction tci) {
		synchronized (terrain) { // dibo 120907
			switch (tci.getType()) {
			case TerrainCellInstruction.IS_WALL:
				return new Boolean(terrain.getWall(tci.getCol(), tci.getRow()));
			case TerrainCellInstruction.COUNT_CORN:
				return new Integer(terrain.getCornCount(tci.getCol(), tci.getRow()));
			case TerrainCellInstruction.COUNT_HAMSTER:
				return new Integer(getHamsterIDs(tci.getCol(), tci.getRow()).size());
			case TerrainCellInstruction.GET_HAMSTER:
				return getHamsterIDs(tci.getCol(), tci.getRow());
			}
			return null;
		}
	}

	public synchronized Object processTerrainInstruction(TerrainInstruction instruction) {
		synchronized (terrain) { // dibo 120907
			switch (instruction.getType()) {
			case TerrainInstruction.COUNT_ROWS:
				return new Integer(terrain.getHeight());
			case TerrainInstruction.COUNT_COLS:
				return new Integer(terrain.getWidth());
			case TerrainInstruction.COUNT_CORN:
				return new Integer(countTotalCorn());
			case TerrainInstruction.COUNT_HAMSTER:
				return new Integer(hamster.size() + 1);
			case TerrainInstruction.GET_HAMSTER:
				return getHamsterIDs();
			}
			return null;
		}
	}

	public synchronized Object processTerminalInstruction(TerminalInstruction instruction) {
		int id = instruction.getHamster();
		String message = instruction.getMessage();
		switch (instruction.getType()) {
		case TerminalInstruction.WRITE:
			blockingFlag = true;
			terminal.write(id, message);
			blockingFlag = false;
			return "ok";
		case TerminalInstruction.READ_INT:
			blockingFlag = true;
			int res = terminal.readInt(id, message);
			blockingFlag = false;
			return new Integer(res);
		case TerminalInstruction.READ_STRING:
			blockingFlag = true;
			String resS = terminal.readString(id, message);
			blockingFlag = false;
			return resS;
		}
		return null;
	}

	public synchronized Object process(final Instruction instruction) {
		synchronized (terrain) { // dibo 120907
			try {
				Object result = null;
				if (instruction instanceof TerminalInstruction) {
					result = processTerminalInstruction((TerminalInstruction) instruction);
				} else if (instruction instanceof HamsterInstruction) {
					result = processHamsterInstruction((HamsterInstruction) instruction);
				} else if (instruction instanceof TerrainCellInstruction) {
					result = processTerrainCellInstruction((TerrainCellInstruction) instruction);
				} else if (instruction instanceof TerrainInstruction) {
					result = processTerrainInstruction((TerrainInstruction) instruction);
				}
//				log(new LogEntry(instruction, result));
				return result;
			} catch (HamsterException e) {
//				log(new LogEntry(instruction, e));
				for (int i = 0; i < hamsterTurns.size(); i++) {
					Workbench.getWorkbench().getJsonObject().put(String.valueOf(i),
							String.valueOf(hamsterTurns.get(i)));
				}
				Workbench.getWorkbench().getJsonObject().put("finished", e.toString());
				error = true;
				return e;
			}
		}

	}

	public synchronized ArrayList<Integer> getHamsterIDs() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < hamster.size(); i++) {
			Hamster h = (Hamster) hamster.get(i);
			ids.add(new Integer(h.getId()));
		}
		ids.add(new Integer(-1));
		return ids;
	}

	public synchronized ArrayList<Integer> getHamsterIDs(int col, int row) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < hamster.size(); i++) {
			Hamster h = (Hamster) hamster.get(i);
			if (h.getX() != col)
				continue;
			if (h.getY() != row)
				continue;
			ids.add(new Integer(h.getId()));
		}
		Hamster h = terrain.getDefaultHamster();
		if (h.getX() == col && h.getY() == row)
			ids.add(new Integer(-1));
		return ids;
	}

	public synchronized int countTotalCorn() {
		int total = 0;
		for (int i = 0; i < terrain.getWidth(); i++) {
			for (int j = 0; j < terrain.getHeight(); j++) {
				total += terrain.getCornCount(i, j);
			}
		}
		return total;
	}

	public boolean blockingFlag = false; // dibo 10032016

	public synchronized void processException(Throwable t) {
		blockingFlag = true;

		blockingFlag = false;
	}

	public synchronized Hamster getHamster(int id) {
		if (id == -1 || id == -2)
			return terrain.getDefaultHamster();
		for (int i = 0; i < hamster.size(); i++) {
			Hamster h = (Hamster) hamster.get(i);
			if (h.getId() == id)
				return h;
		}
		return null;
	}

	public synchronized Object newHamster(int id, int r, int s, int b, int k, int c) {
		if (terrain.getWall(s, r) || b < 0 || b > 3 || k < 0)
			throw new HamsterInitialisierungsException(null);
		hamster.add(new Hamster(id, s, r, b, k, c));
		setChanged();
		notifyObservers(TERRAIN);
		return "ok";
	}

	int int_color = 1;

	public synchronized Object newHamster(int id, int r, int s, int b, int k) {
		if (terrain.getWall(s, r) || b < 0 || b > 3 || k < 0)
			throw new HamsterInitialisierungsException(null);
		hamster.add(new Hamster(id, s, r, b, k, int_color++));
		setChanged();
		notifyObservers(TERRAIN);
		return "ok";
	}

	public synchronized Object turnLeft(int id) {
		Hamster hamster = getHamster(id);
		if (!checkonly) {
			hamster.setDir((hamster.getDir() + 3) % 4);
			setChanged();
			notifyObservers(TERRAIN);
		}
		return "ok";
	}

	public synchronized Object forward(int id) {
		Hamster hamster = getHamster(id);
		int nextX = hamster.getX() + DX[hamster.getDir()];
		int nextY = hamster.getY() + DY[hamster.getDir()];
		if (terrain.getWall(nextX, nextY)) {
			throw new MauerDaException(null, nextY, nextX);
		} /* lego */else if (id == -2 && terrain.getCornCount(nextX, nextY) > 0) {
			throw new KornDaException(null, nextY, nextX);
		} else {
			if (!checkonly) {
				hamster.setXY(nextX, nextY);
				setChanged();
				notifyObservers(TERRAIN);
			}
			return "ok";
		}
	}

	public synchronized Object pickUp(int id) {
		Hamster hamster = getHamster(id);
		/* lego */if (id == -2) {
			int nextX = hamster.getX() + DX[hamster.getDir()];
			int nextY = hamster.getY() + DY[hamster.getDir()];

			if (terrain.getWall(nextX, nextY)) {
				throw new MauerDaException(null, nextY, nextX);
			} else if (!mouthEmpty(id)) {
				throw new MaulNichtLeerException(null);
			} else {
				if (!checkonly) {
					hamster.setXY(nextX, nextY);
					setChanged();
					notifyObservers(TERRAIN);
				}
			}
		} //
		int count = terrain.getCornCount(hamster.getX(), hamster.getY());
		if (count == 0) {
			throw new KachelLeerException(null, hamster.getY(), hamster.getX());

		} else {
			if (!checkonly) {
				hamster.setMouth(hamster.getMouth() + 1);
				terrain.setCornCount(hamster.getX(), hamster.getY(), count - 1);
				setChanged();
				notifyObservers(TERRAIN);
				/* lego */if (id == -2) {

					turnLeft(id);
					turnLeft(id);
					forward(id);
					turnLeft(id);
					turnLeft(id);

				}
			}
			return "ok";
		}
	}

	public synchronized Object layDown(int id) {
		/* lego */if (id == -2) {
			forward(id);

		}
		Hamster hamster = getHamster(id);
		int count = terrain.getCornCount(hamster.getX(), hamster.getY());
		if (hamster.getMouth() == 0) {
			throw new MaulLeerException(null);
		} else {
			if (!checkonly) {
				hamster.setMouth(hamster.getMouth() - 1);
				terrain.setCornCount(hamster.getX(), hamster.getY(), count + 1);
				setChanged();
				notifyObservers(TERRAIN);
				/* lego */if (id == -2) {
					turnLeft(id);
					turnLeft(id);
					forward(id);
					turnLeft(id);
					turnLeft(id);

				}
			}
			return "ok";
		}
	}

	public synchronized void setChanged() {
		super.setChanged();
	}

	public synchronized boolean free(int id) {
		Hamster hamster = getHamster(id);
		return !terrain.getWall(hamster.getX() + DX[hamster.getDir()], hamster.getY() + DY[hamster.getDir()]);
	}

	public synchronized boolean cornAvailable(int id) {
		Hamster hamster = getHamster(id);
		/* lego */if (id == -2) {
			int nextX = hamster.getX() + DX[hamster.getDir()];
			int nextY = hamster.getY() + DY[hamster.getDir()];
			return terrain.getCornCount(hamster.getX() + DX[hamster.getDir()],
					hamster.getY() + DY[hamster.getDir()]) != 0;
		} else
			return terrain.getCornCount(hamster.getX(), hamster.getY()) != 0;
	}

	public synchronized boolean mouthEmpty(int id) {
		Hamster hamster = getHamster(id);
		return hamster.getMouth() == 0;
	}

	private synchronized void setState(int state) {
		this.state = state;
		setChanged();
		notifyObservers(STATE);
	}

	/**
	 * @param terminal The terminal to set.
	 */
	public synchronized void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	protected boolean checkonly = false;

	public void checkOnly(boolean mode) {
		checkonly = mode;
	}

	public boolean isCheckOnly() {
		return checkonly;
	}
}