package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

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