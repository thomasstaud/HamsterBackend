package at.ac.htlinn.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterEvaluation.debugger.model.Hamster;
import at.ac.htlinn.hamsterEvaluation.workbench.Utils;

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
