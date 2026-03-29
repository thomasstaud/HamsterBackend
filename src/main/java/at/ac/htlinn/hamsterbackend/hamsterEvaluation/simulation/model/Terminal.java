package at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.2 $
 */
public interface Terminal {
	public void write(int hamsterid, String string);
	public int readInt(int hamsterid, String message);
	public String readString(int hamsterid, String message);
	public void write(Throwable t);

}
