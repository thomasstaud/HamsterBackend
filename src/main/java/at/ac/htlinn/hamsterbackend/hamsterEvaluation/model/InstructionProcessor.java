package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

/**
 * Dieses Interface wird von Klassen implementiert, die Instruktionen
 * verarbeiten koennen.
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.2 $
 */
public interface InstructionProcessor {
	public void start();

	public void finished();

	public Object process(Instruction instruction);

	public void processException(Throwable e);
	
}
