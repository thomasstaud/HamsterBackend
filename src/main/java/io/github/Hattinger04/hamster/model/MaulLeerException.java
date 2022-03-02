package io.github.Hattinger04.hamster.model;

import java.io.Serializable;

import io.github.Hattinger04.hamster.debugger.model.Hamster;
import io.github.Hattinger04.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MaulLeerException extends HamsterException implements Serializable {
	public MaulLeerException(Hamster hamster) {
		super(hamster, "1");
	}

	public String toString() {
		return Utils.getResource("hamster.MaulLeerException");
	}
}