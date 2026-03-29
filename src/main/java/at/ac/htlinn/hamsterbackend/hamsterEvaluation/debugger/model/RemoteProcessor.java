package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterException;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.Instruction;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.InstructionProcessor;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * Diese Klasse dient zur Weiterleitung von Hamsterbefehlen durch einen Ein- und
 * Ausgabestrom. Er leitet also alle Instruktione an einen entfernten
 * InstructionProcessor weiter. Ein Instanz dieser Klasse wird dabei auf beiden
 * Seiten des Stroms benutzt, die eine codiert die Befehle in den Strom, die
 * andere liest die Befehle, leitet sie an den entfernten Prozessor weiter und
 * codiert die Antworten wieder in den Strom zurueck.
 *
 * @author Daniel Jasper
 */
public class RemoteProcessor implements InstructionProcessor {
	/**
	 * Der Ausgabestrom der jeweiligen Seite.
	 */
	private ObjectOutputStream out;

	/**
	 * Der Eingabestrom der jeweiligen Seite.
	 */
	private ObjectInputStream in;

	/**
	 * Die ist der entfernte Prozessor. Bei der eine Seite des RemoteProcessors
	 * ist dieses Attribut daher null.
	 */
	private InstructionProcessor processor;

	/**
	 * �ber dieses Attribut wird dem RemoteProcessor signalisiert, dass die
	 * Ausfuehrunge des Programms beendet wird.
	 */
	boolean terminate;

	/**
	 * Dieses Attribut laesst den InstructionProcessor nach der Ausfuehrung
	 * jedes Befehls die angegebene Anzahl in Millisekunden zu warten. Ansonsten
	 * wuerde ein Hamsterprogramm so schnell laufen, dass der Ablauf nicht
	 * verfolgt werden kann.
	 */
	private int delay;

	/**
	 * Dieser Konstruktor erzeugt eine Instanz von RemoteProcessor, die auf der
	 * Client-Seite eingesetzt wird. Die Instanz codiert also die vom
	 * Hamsterprogramm kommenden Befehle in den Strom.
	 *
	 * @param in
	 *            Der Eingabestrom
	 * @param out
	 *            Der Ausgabestrom
	 */
	public RemoteProcessor(InputStream in, OutputStream out) {
		try {
			this.out = new ObjectOutputStream(new PrintStream(out, true));
			this.in = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dieser Konstruktor erzeugt eine Instanz von RemoteProcessor, die auf der
	 * Simulator-Seite eingesetzt wird. Die Instanz decodiert also Befehle aus
	 * dem Strom und leitet sie an einen anderen InstructionProcessor weiter.
	 *
	 * @param in
	 *            Der Eingabestrom
	 * @param out
	 *            Der Ausgabestrom
	 * @param processor
	 *            Der Processor
	 */
	public RemoteProcessor(InputStream in, OutputStream out, InstructionProcessor processor) {
		this.delay = 0;
		try {
			this.out = new ObjectOutputStream(new PrintStream(out, true));
			this.in = new ObjectInputStream(in);

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.processor = processor;
	}

	/**
	 * Diese Methode setzt delay
	 *
	 * @param delay
	 *            Der neue Wert von Delay
	 */
	public void setDelay(int delay) {
		this.delay = 1; 
	}

	// dibo
	public int getDelay() {
		return this.delay;
	}

	@Override
	public void start() {
	}

	@Override
	public void finished() {
	}

	/**
	 * Der RemoteProcessor auf Simulator-Seite liest in einem neuen Thread
	 * solange Befehle aus dem Strom, bis terminate gesetzt wird oder eine
	 * Exception auftritt. Tritt eine EOFException auf, so bedeutet dies, dass
	 * der Client-Prozess beendet wurde, daher ist dies kein Fehler sondern
	 * tritt im normalen Ablauf auf.
	 */
	public void run() {

		try {
			this.processor.start();
			while (!this.terminate) {
				Object obj = this.in.readObject();
				if (obj instanceof Instruction) {
					Object o = this.processor.process((Instruction) obj);
					this.out.writeObject(o);
				} else if (obj instanceof Throwable) {
					this.processor.processException((Throwable) obj);
				}
			}
		} catch (IOException e) {
			if (!(e instanceof EOFException)) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.processor.finished();
	}

	@Override
	public synchronized Object process(Instruction instruction) {
		try {
			this.out.writeObject(instruction);
			return this.in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public synchronized void processException(Throwable t) {
		try {
			while (t.getCause() != null) {
				t = t.getCause();
			}
			if (t instanceof HamsterException) {
				((HamsterException) t).setHamster(null);
			}
			this.out.writeObject(t);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void terminate() {
		this.terminate = true;
	}
}

class MyObjectInputStream extends ObjectInputStream {

	public MyObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	// @Override
	// protected void readStreamHeader() throws IOException,
	// StreamCorruptedException {
	//
	// }
}

class MyObjectOutputStream extends ObjectOutputStream {

	public MyObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	// @Override
	// protected void writeStreamHeader() throws IOException {
	// }
}
