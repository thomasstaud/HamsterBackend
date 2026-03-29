package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterNichtInitialisiertException extends HamsterException
		implements Serializable {
	public HamsterNichtInitialisiertException(Hamster hamster) {
		super(hamster, "3");
	}

	public String toString() {
		return "hamster.HamsterNichtInitialisiertException";
	}
}