package io.github.Hattinger04.hamsterEvaluation.workbench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import io.github.Hattinger04.hamsterEvaluation.compiler.controller.CompilerController;
import io.github.Hattinger04.hamsterEvaluation.debugger.controller.DebuggerController;
import io.github.Hattinger04.hamsterEvaluation.interpreter.Territorium;
import io.github.Hattinger04.hamsterEvaluation.lego.controller.LegoController;
import io.github.Hattinger04.hamsterEvaluation.model.HamsterFile;
import io.github.Hattinger04.hamsterEvaluation.simulation.controller.SimulationController;
import io.github.Hattinger04.hamsterEvaluation.simulation.model.SimulationModel;
import io.github.Hattinger04.hamsterEvaluation.simulation.model.Terrain;
import lombok.Getter;
import lombok.Setter;


/**
 * Diese Klasse implementiert den Controller der zentralen Werkbank. Die
 * dazugehoerige main-Methode wird beim Programmstart aufgerufen und erzeugt
 * eine Instanz des Controller. Der Controller erzeugt dann Instanzen der der
 * WorkbenchModel und der WorkbenchView. Ausserdem erzeugt er die
 * Controller-Komponenten von Compiler, Editor, Debugger und Simulation.
 * 
 * Die Werkbank stellt Methoden bereit, ueber die Funktionen von einzelnen
 * Komponenten (Editor, Compiler, ...) aufgerufen werden koennen.
 * 
 * TODO: Classpath einbauen
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.4 $
 */
public class Workbench {
	public static Workbench workbench;

	/**
	 * Das Model der Werkbank.
	 */
	private WorkbenchModel model;


	/**
	 * Der Controller des Compilers.
	 */
	private CompilerController compiler;

	/**
	 * Der Controller des Debuggers.
	 */
	private DebuggerController debugger;

	/**
	 * Der Controller des Editors.
	 */

	/**
	 * Der Controller der Simulation.
	 */
	private SimulationController simulation;

	private Properties settings;

	/* lego */
	private LegoController lego;

	// dibo
	public boolean simulatdorOnly;
	
	@Getter @Setter
	private HashMap<String, String> jsonObject; 
	
	protected Workbench(boolean simulatorOnly, SimulationModel simModel) {
		workbench = this; // Prolog
		settings = new Properties();

		jsonObject = new HashMap<String, String>(); 
		
		model = new WorkbenchModel(simulatorOnly, simModel);
		simulation = new SimulationController(model.getSimulationModel(), this);
		compiler = new CompilerController(model.getCompilerModel(), this);
		debugger = new DebuggerController(model.getDebuggerModel(), this);
//		Example of how a program could start: 
//		startProgram("Programme/data.ham", "Programme/test.ter", new TerrainForm(10,10,new int[][] {{1,2}, {2,3}}, new int[] {1,1}, new int[][] {{0,0}, {1,0}}, 0, 1));
	}
		
	@Getter @Setter
	public class TerrainForm {
		
		private int leange, breite, x, y;
		private int[] cornAnzahl; 
		private int[][] corn, wall;  
		
		/**
		 * Create a terrain: 
		 * 
		 * @param laenge
		 * @param breite
		 * @param corn
		 * @param cornAnzahl
		 * @param wall
		 * @param x
		 * @param y
		 */
		public TerrainForm(int laenge, int breite, int[][] corn, int[] cornAnzahl, int[][] wall, int x, int y) {
			this.leange = laenge; 
			this.breite = breite; 
			this.corn = corn; 
			this.cornAnzahl = cornAnzahl; 
			this.wall = wall; 
			this.x = x; 
			this.y = y; 
		}
	}
	
	/**
	 * Starting a programm with standard terrain
	 * 
	 * @param path
	 */
	public HashMap<String, String> startProgram(String path) {
		HamsterFile file = HamsterFile.createHamsterFile(path, HamsterFile.OBJECT); 
		compiler.setActiveFile(file);
		file.setType(HamsterFile.OBJECT);
		ensureCompiled(file);
		model.getDebuggerModel().start(file);
		return jsonObject; 
	}
	
	/**
	 * Starting program with already existing terrain
	 * 
	 * @param path
	 * @param ter
	 */
	public HashMap<String, String> startProgram(String path, String ter) {
		System.out.println("Loading Terrain...");
		Territorium.ladeTerritorium(ter);
		return startProgram(path);
	}
	
	/**
	 * Starting a programm and create new form
	 * 
	 * @param path
	 * @param ter
	 */
	public HashMap<String, String> startProgram(String path, String ter, TerrainForm form) {
		System.out.println("Creating Terrain...");
		Terrain t = new Terrain(createTerrain(form)); 
		createTerrainFile(t, ter); 
		Territorium.ladeTerritorium(ter);
		return startProgram(path);
	}
	
	/**
	 * Creates File for Terrain; 
	 * returns true if successful
	 * 
	 * @param terrain
	 * @param path
	 * @return
	 */
	public boolean createTerrainFile(Terrain terrain, String path) {
		try {
			System.out.println("Creaing Terrain File...");
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file.getPath(), file.createNewFile());
			fileWriter.write(terrain.toString());
			Scanner sc = new Scanner(file); 
			while(sc.hasNextLine()) {
				System.out.println(sc.nextLine());
			}
			fileWriter.close();
			sc.close();
			return true; 
		} catch (IOException e) {
			return false; 
		}
	}
	
	public Terrain createTerrain(TerrainForm form) {
		Terrain terrain = new Terrain(form.getLeange(), form.getBreite()); 
		int save = 0; 
		for(int i = 0; i < form.getCorn().length; i++) {
			for(int j = 0; j < form.getCorn()[i].length; j++) {
				if(j % 2 == 0) {
					save = form.getCorn()[i][j]; 
				} else {
					System.out.println(save + " " + form.getCorn()[i][j] + " " + form.getCornAnzahl()[i]);
					terrain.setCornCount(save, form.getCorn()[i][j], form.getCornAnzahl()[i]);
				}
			}
		}
		
		for(int i = 0; i < form.getWall().length; i++) {
			for(int j = 0; j < form.getWall()[i].length; j++) {
				if(j % 2 == 0) {
					save = form.getWall()[i][j]; 
				} else {
					System.out.println(save + " " + form.getWall()[i][j]);
					terrain.setWall(save, form.getWall()[i][j], true);
				}
				
			}
		}
		terrain.getDefaultHamster().setXY(form.getX(), form.getY());
		return terrain; 
	}
	
	public static Workbench getWorkbench() {
		if (workbench == null)
			workbench = new Workbench(false, null);
		return workbench;
	}

	public static Workbench getOnlyWorkbench() {
		return workbench;
	}

	// dibo 11.01.2006
	public static Workbench getSimWorkbench(SimulationModel simModel) {
		if (workbench == null)
			workbench = new Workbench(true, simModel);
		return workbench;
	}

	// dibo 11.01.2006
	public SimulationController getSimulationController() {
		return simulation;
	}

	public DebuggerController getDebuggerController() {
		return debugger;
	}
	

	public boolean ensureCompiled(HamsterFile file) {
		return compiler.ensureCompiled(file);
	}

	/**
	 * Ueber diese Methode benachrichtigt der Editor den Compiler und den
	 * Debugger darueber, dass nun eine andere Datei editiert wird.
	 * 
	 * @param file
	 *            Die nun editierte Datei.
	 */
	public void setActiveFile(HamsterFile file) {
		compiler.setActiveFile(file);
		debugger.setActiveFile(file);
		/* lego */
		if (Utils.LEGO) {
			lego.setActiveFile(file);
		}
	}

	/**
	 * Diese Methode wird beim Start des Hamster-Simulators aufgerufen.
	 * 
	 * @param args
	 *            	 Als Argument kann der Simulator ein ham-File entgegennehmen
	 */
	public static void main(String[] args) {
		Workbench wb = getWorkbench();
	}

	/**
	 * Diese Methode liefert den Controller des Compilers.
	 * 
	 * @return Der Controller des Compilers.
	 */
	public CompilerController getComiler() {
		return compiler;
	}

	/**
	 * Diese Methode liefert den Controller des Debuggers.
	 * 
	 * @return Der Controller des Debuggers.
	 */
	public DebuggerController getDebugger() {
		return debugger;
	}


	/**
	 * Diese Methode liefert den Controller der Simulation.
	 * 
	 * @return Der Controller der Simulation.
	 */
	public SimulationController getSimulation() {
		return simulation;
	}

	/**
	 * Diese Methode liefert das WorkbenchModel.
	 * 
	 * @return Das WorkbenchModel.
	 */
	public WorkbenchModel getModel() {
		return model;
	}

	/**
	 * Diese Methode liefert die WorkbenchView.
	 * 
	 * @return Die WorkbenchView.
	 */

	public String getProperty(String key, String defaultValue) {
		return settings.getProperty(key, defaultValue);
	}

	public void setProperty(String key, String value) {
		settings.setProperty(key, value);
	}

	public void stop() {
		// TODO: do smth on stop
	}
}
