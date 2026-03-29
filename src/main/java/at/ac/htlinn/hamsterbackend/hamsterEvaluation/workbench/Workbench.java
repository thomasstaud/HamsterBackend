package at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.controller.CompilerController;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.controller.DebuggerController;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.DebuggerModel;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.interpreter.Territorium;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.controller.LegoController;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.controller.SimulationController;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.SimulationModel;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.terrain.Field;
import at.ac.htlinn.hamsterbackend.terrain.HamsterObject;
import at.ac.htlinn.hamsterbackend.terrain.ViewDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;


/**
 * Diese Klasse implementiert den Controller der zentralen Werkbank. Die
 * dazugehoerige main-Methode wird beim Programmstart aufgerufen und erzeugt
 * eine Instanz des Controller. Der Controller erzeugt dann Instanzen der der
 * WorkbenchModel und der WorkbenchView. Ausserdem erzeugt er die
 * Controller-Komponenten von Compiler, Editor, Debugger und Simulation.
 * <p>
 * Die Werkbank stellt Methoden bereit, ueber die Funktionen von einzelnen
 * Komponenten (Editor, Compiler, ...) aufgerufen werden koennen.
 * <p>
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

    @Getter
    @Setter
    private TreeMap<String, String> jsonObject;

    protected Workbench(boolean simulatorOnly, SimulationModel simModel) {
        workbench = this; // Prolog
        settings = new Properties();

        jsonObject = new TreeMap<String, String>();

        model = new WorkbenchModel(simulatorOnly, simModel);
        simulation = new SimulationController(model.getSimulationModel(), this);
        compiler = new CompilerController(model.getCompilerModel(), this);
        debugger = new DebuggerController(model.getDebuggerModel(), this);
//		Example of how a program could start: 
//		startProgram("Programme/data.ham", "Programme/test.ter", new TerrainForm(10,10,new int[][] {{1,2}, {2,3}}, new int[] {1,1}, new int[][] {{0,0}, {1,0}, 1}, 0, 1));
    }

    public void compile(String path) {
        debugger.getDebuggerModel().setState(DebuggerModel.NOT_RUNNING);
        HamsterFile file = HamsterFile.createHamsterFile(path, HamsterFile.OBJECT);
        compiler.setActiveFile(file);
        file.setType(HamsterFile.OBJECT);
        ensureCompiled(file);
        //TODO: make sure if it is needed to compile as OOP file !
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class TerrainForm {

        private int laenge, breite, x, y, blickrichtung;

        private int[] cornAnzahl;
        private int[][] corn, wall;

    }

    /**
     * Starting a programm with standard terrain
     *
     * @param path
     */
    public TreeMap<String, String> startProgram(String path) {
        debugger.getDebuggerModel().setState(DebuggerModel.NOT_RUNNING);
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
    public TreeMap<String, String> startProgram(String path, String ter) {
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
    public TreeMap<String, String> startProgram(String path, String ter, TerrainForm form) {
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
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            fileWriter.close();
            sc.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Terrain createHamsterTerrain(List<Field> customFields, int height, int width, HamsterObject hamster) {
        Terrain terrain = new Terrain(width, height);
        customFields.forEach(field -> {
            terrain.setWall(field.getXCord(), field.getYCord(), field.isWall());
            terrain.setCornCount(field.getXCord(), field.getYCord(), field.getCntCorn());
        });
        terrain.getDefaultHamster().setDir(ViewDirection.viewDirectionIntegerMap.get(hamster.getViewDirection()));
        terrain.getDefaultHamster().setMouth(hamster.getCntCornInMouth());
        terrain.getDefaultHamster().setXY(hamster.getXCord(), hamster.getYCord());
        return terrain;
    }

    public Terrain createTerrain(TerrainForm form) {
        Terrain terrain = new Terrain(form.getLaenge(), form.getBreite());
        int save = 0;
        for (int i = 0; i < form.getCorn().length; i++) {
            for (int j = 0; j < form.getCorn()[i].length; j++) {
                if (j % 2 == 0) {
                    save = form.getCorn()[i][j];
                } else {
                    terrain.setCornCount(save, form.getCorn()[i][j], form.getCornAnzahl()[i]);
                }
            }
        }

        for (int i = 0; i < form.getWall().length; i++) {
            for (int j = 0; j < form.getWall()[i].length; j++) {
                if (j % 2 == 0) {
                    save = form.getWall()[i][j];
                } else {
                    terrain.setWall(save, form.getWall()[i][j], true);
                }

            }
        }
        terrain.getDefaultHamster().setXY(form.getX(), form.getY());
        terrain.getDefaultHamster().setDir(form.getBlickrichtung());
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
     * @param file Die nun editierte Datei.
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
     * @param args Als Argument kann der Simulator ein ham-File entgegennehmen
     */
    public static void main(String[] args) {
        Workbench wb = getWorkbench();
        wb.startProgram("src/main/resources/hamster/testuser/data.ham");
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
