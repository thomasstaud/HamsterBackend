package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

/*lego*/

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model.LHamster;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.InstructionProcessor;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterProgram;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Die Klasse hat zwei Funktionen. Zum einen wird ihre main-Methode bei der
 * Ausfuehrung eines Hamsterprogramms in der Client-Virtual-Maschine aufgerufen.
 * Zum anderen erzeugt sie den Simulator-seitigen RemoteProcessor und startet
 * und stoppt diesen.
 *
 * @author Daniel Jasper
 */
public class RemoteRunner {
    /**
     * Dies ist der Simulator-seitige RemoteProcessor
     */
    private RemoteProcessor remoteProcessor;

    /**
     * Mit diesem Konstruktor wird der RemoteRunner auf Server-Seite erzeugt.
     * Dieser erzeugt direkt den serverseitigen RemoteProcessor.
     *
     * @param processor An diesen Processor muessen die vom Client kommenden Befehle
     *                  weitergeleitet werden
     * @param p         Dies ist der Prozess, in dem die Client-VM lauuft. Ueber
     *                  dieses Objekt koennen die Ein- und Ausgabestroeme verbunden
     *                  werden.
     */
    public RemoteRunner(InstructionProcessor processor, Process p) {
        remoteProcessor = new RemoteProcessor(p.getInputStream(),
                p.getOutputStream(), processor);
    }

    /**
     * Diese Methode stoppt den RemoteProcessor
     */
    public void stop() {
        remoteProcessor.terminate();
    }

    /**
     * Diese Methode startet den Simulator-seitigen RemoteProcessor, wodurch der
     * Simulator dann die Befehle des Clients empfangen kann.
     * <p>
     * Removed Thread!
     */
    public void start() {
        remoteProcessor.run();
    }

    /**
     * Leitet RemoteProcessor.setDelay(int delay) weiter.
     *
     * @param delay Die Verzoegerung nach der Ausfuehrung jedes Befehls.
     */
    public void setDelay(int delay) {
        remoteProcessor.setDelay(delay);
    }

    /**
     * Dies ist die main-Methode, die aufgerufen wird, wenn ein Hamsterprogramm
     * in einer Client-VM ausgefuehrt wird.
     *
     * @param args Es wird ein Parameter uebergeben, der Name des auszufuehrenden
     *             Hamsterprogramms
     */
    public static void main(String[] args) {
        try {
            Utils.loadProperties(); // dibo
            // diboC
            // InputStream commIn = new FileInputStream("result");
            // OutputStream commOut = new PrintStream(new FileOutputStream(
            // "command"));
            // RemoteProcessor rp = new RemoteProcessor(commIn, commOut);
            RemoteProcessor rp = new RemoteProcessor(System.in, System.out);
            String syso = Utils.LOGFOLDER + "sysout.txt";
            String syse = Utils.LOGFOLDER + "syserr.txt";
            try {
                System.setOut(new PrintStream(new FileOutputStream(syso)));
                System.setErr(new PrintStream(new FileOutputStream(syse)));
            } catch (Throwable t) {
                System.setOut(new PrintStream(
                        new FileOutputStream("sysout.txt")));
                System.setErr(new PrintStream(
                        new FileOutputStream("syserr.txt")));
                if (Utils.language.equals("en")) {
                    System.err
                            .println(t
                                    + "\n(Please replace all \\-characters by /-characters in the logfolder-property!)");

                } else {
                    System.err
                            .println(t
                                    + "\n(Bitte im Property logfolder alle \\-Zeichen durch /-Zeichen ersetzen!)");
                }
            }
            /* lego */
            LHamster.processor = rp;
            IHamster.processor = rp;
            Territorium.processor = rp;
            String className = args[0];
            Object hamsterObject;

            try {
                Class hamsterClass = Class.forName(className);
                hamsterObject = hamsterClass.newInstance();
                HamsterProgram hamster = (HamsterProgram) hamsterObject;
                Hamster.getStandardHamster();
                if (Utils.security) { // dibo
                    System.setSecurityManager(new SecurityManager());
                }
                hamster.main();
            } catch (Exception e) {
                rp.processException(e);
                e.printStackTrace();
            } catch (Error e) {
                rp.processException(new RuntimeException(e));
                e.printStackTrace();
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }
}
