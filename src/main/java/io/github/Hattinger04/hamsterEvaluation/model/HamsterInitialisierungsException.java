package io.github.Hattinger04.hamsterEvaluation.model;

import java.io.Serializable;

import io.github.Hattinger04.hamsterEvaluation.debugger.model.Hamster;
import io.github.Hattinger04.hamsterEvaluation.workbench.Utils;

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
