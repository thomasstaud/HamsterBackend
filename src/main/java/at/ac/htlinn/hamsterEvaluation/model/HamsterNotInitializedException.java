package at.ac.htlinn.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterEvaluation.debugger.model.Hamster;
import at.ac.htlinn.hamsterEvaluation.workbench.Utils;

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