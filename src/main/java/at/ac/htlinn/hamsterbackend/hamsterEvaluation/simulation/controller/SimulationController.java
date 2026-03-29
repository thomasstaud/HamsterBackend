package at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.controller;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.SimulationModel;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;


/**
 * @author $Author: djasper $
 * @version $Revision: 1.3 $
 */
public class SimulationController{
	public static final String NEW = "new";
	public static final String OPEN = "open";
	public static final String SAVE_AS = "saveas";
	public static final String TURN = "turn hamster";
	public static final String HAMSTER_CORN = "hamster corn";
	public static final String RESET = "reset";
	public static final String ZOOM_IN_3D = "zoom in 3d";
	public static final String ZOOM_OUT_3D = "zoom out 3d";
	public static final String TURN_LEFT_3D = "turn left 3d";
	public static final String TURN_RIGHT_3D = "turn right 3d";
	public static final String LOOK_UP_3D = "look up 3d";
	public static final String LOOK_DOWN_3D = "look down 3d";
	public static final String TOGGLE_GRID_3D = "toggle grid 3d";
	public static final String TOGGLE_MUSIC_3D = "toggle music 3d";
	public static final String TOGGLE_SOUND_3D = "toggle sound 3d";
	public static final String PERSPECTIVE_3D = "perspective 3d";

	Workbench workbench;

	SimulationModel simulationModel;

	public SimulationController(SimulationModel simulationModel, Workbench workbench) {
		this.workbench = workbench;
		this.simulationModel = simulationModel;
	}


	public void setWall(int x1, int y1, int x2, int y2) {
		for (int i = x1; i <= x2; i++) {
			if (i < 0 || i >= simulationModel.getTerrain().getWidth())
				continue;
			for (int j = y1; j <= y2; j++) {
				if (j < 0 || j >= simulationModel.getTerrain().getHeight())
					continue;
				if (!simulationModel.containsHamster(i, j)) {
					simulationModel.getTerrain().setWall(i, j, true);
					// Entferne K�rner an dieser Position..
					simulationModel.getTerrain().setCornCount(i, j, 0);
				}
			}
		}
		simulationModel.setChanged();
		simulationModel.notifyObservers(SimulationModel.TERRAIN);


	}

	public void setEmpty(int x1, int y1, int x2, int y2) {
		for (int i = x1; i <= x2; i++) {
			if (i < 0 || i >= simulationModel.getTerrain().getWidth())
				continue;
			for (int j = y1; j <= y2; j++) {
				if (j < 0 || j >= simulationModel.getTerrain().getHeight())
					continue;
				simulationModel.getTerrain().setWall(i, j, false);
				simulationModel.getTerrain().setCornCount(i, j, 0);
			}
		}
		simulationModel.setChanged();
		simulationModel.notifyObservers(SimulationModel.TERRAIN);

	
	}

	public void setCorn(int x1, int y1, int x2, int y2, int count) {
		for (int i = x1; i <= x2; i++) {
			if (i < 0 || i >= simulationModel.getTerrain().getWidth())
				continue;
			for (int j = y1; j <= y2; j++) {
				if (j < 0 || j >= simulationModel.getTerrain().getHeight())
					continue;
				if (!simulationModel.getTerrain().getWall(i, j))
					simulationModel.getTerrain().setCornCount(i, j, count);
			}
		}
		simulationModel.setChanged();
		simulationModel.notifyObservers(SimulationModel.TERRAIN);


	}

	public boolean containsValidCornCell(int x1, int y1, int x2, int y2) {
		for (int i = x1; i <= x2; i++) {
			if (i < 0 || i >= simulationModel.getTerrain().getWidth())
				continue;
			for (int j = y1; j <= y2; j++) {
				if (j < 0 || j >= simulationModel.getTerrain().getHeight())
					continue;
				if (!simulationModel.getTerrain().getWall(i, j))
					return true;
			}
		}
		return false;
	}

	public void setHamsterPos(int x, int y) {
		if (!simulationModel.getTerrain().getWall(x, y)) {
			simulationModel.getTerrain().getDefaultHamster().setXY(x, y);
		}
		simulationModel.setChanged();
		simulationModel.notifyObservers(SimulationModel.TERRAIN);


	}

	public void turnHamster() {
		simulationModel.turnLeft(-1);
		simulationModel.setChanged();
		simulationModel.notifyObservers(SimulationModel.TERRAIN);

	}


	public Workbench getWorkbench() {
		return workbench;
	}

	// dibo 11.01.2006
	public SimulationModel getSimulationModel() {
		return simulationModel;
	}

}