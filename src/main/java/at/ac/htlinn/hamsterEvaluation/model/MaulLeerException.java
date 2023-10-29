package at.ac.htlinn.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterEvaluation.debugger.model.Hamster;
import at.ac.htlinn.hamsterEvaluation.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MaulLeerException extends HamsterException implements Serializable {
	public MaulLeerException(Hamster hamster) {
		super(hamster, "1");
	}

	public String toString() {
		return "hamster.MaulLeerException";
	}
}