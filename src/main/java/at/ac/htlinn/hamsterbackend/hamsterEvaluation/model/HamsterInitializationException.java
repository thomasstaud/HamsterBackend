package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterInitializationException extends HamsterInitialisierungsException
		implements Serializable {
	public HamsterInitializationException(Hamster hamster) {
		super(hamster);
	}
	public HamsterInitializationException(HamsterInitialisierungsException e) {
		super(e.hamster);
	}

}
