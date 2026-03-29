package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterInitialisierungsException extends HamsterException
		implements Serializable {
	public HamsterInitialisierungsException(Hamster hamster) {
		super(hamster, "4");
	}

	public String toString() {
		return "hamster.HamsterInitialisierungsException";
	}
}
