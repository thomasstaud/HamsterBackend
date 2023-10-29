package at.ac.htlinn.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterEvaluation.debugger.model.Hamster;
import at.ac.htlinn.hamsterEvaluation.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MouthEmptyException extends MaulLeerException implements Serializable {
	public MouthEmptyException(Hamster hamster) {
		super(hamster);
	}
	
	public MouthEmptyException(MaulLeerException exc) {
		super(exc.hamster);
	}
}