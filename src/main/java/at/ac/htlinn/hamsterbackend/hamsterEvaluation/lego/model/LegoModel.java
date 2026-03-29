package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;

import java.io.IOException;
import java.util.Observable;


/**
 * Dies ist das Model der Lego Komponente. Es kombiniert den LegoCompiler, die .nxj-Code Erzeugung und den Upload.
 *
 * @author Christian
 */
public class LegoModel extends Observable {

    /**
     * Dieses Argument benachrichtigt �ber die Aenderung des Uploadstatus`
     */
    public static final String LEGO_UPLOAD = "upload";

    /**
     * Dieses Argument informiert dar�ber, dass gerade kein Upload stattfindet.
     */
    public static final int NOT_CONNECTED = 0;

    /**
     * Dieses Argument benachrichtigt �ber den Erfolg eines Uploads.
     */
    public static final int SUCCESS = 1;

    /**
     * Dieses Argument benachrichtigt �ber einen gescheiterten Uploadversuch.
     */
    public static final int FAILURE = 2;

    private LegoCompiler legoCompiler;

    private LegoPrecompiler precompiler;

    private Workbench workbench;

    private int state;

    /**
     * Der Konstruktor von LegoModel erzeugt lediglich Instanzen von
     * LegoCompiler, dem Lego-Precompiler und Upload.
     */
    public LegoModel() {
        state = NOT_CONNECTED;
        legoCompiler = new LegoCompiler();
        precompiler = new LegoPrecompiler();
    }


    /**
     * Ruft den Precompiler und den JavaCompiler auf.
     *
     * @param file Das HamsterFile, das vom JavaCompiler kompiliert werden soll.
     * @throws java.io.IOException
     */
    public void legoCompile(HamsterFile file) throws IOException {
        precompiler.precompile(file);
        legoCompiler.compile(file);
    }


    /**
     * Liefert den Upload-Status.
     *
     * @return Der aktuelle Status.
     */
    public int getState() {
        return state;
    }

    /**
     * Setzt den Status des Uploads.
     *
     * @param state Der neue Status.
     */
    public void setState(int state) {
        this.state = state;
        setChanged();
        notifyObservers(LEGO_UPLOAD);

    }
}
