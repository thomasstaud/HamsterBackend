package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterNotInitializedException extends
		HamsterNichtInitialisiertException implements Serializable {
	public HamsterNotInitializedException(Hamster hamster) {
		super(hamster);
	}

	public HamsterNotInitializedException(HamsterNichtInitialisiertException e) {
		super(e.hamster);
	}

}