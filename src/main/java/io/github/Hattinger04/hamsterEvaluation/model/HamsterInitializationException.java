package io.github.Hattinger04.hamsterEvaluation.model;

import java.io.Serializable;

import io.github.Hattinger04.hamsterEvaluation.debugger.model.Hamster;
import io.github.Hattinger04.hamsterEvaluation.workbench.Utils;

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
