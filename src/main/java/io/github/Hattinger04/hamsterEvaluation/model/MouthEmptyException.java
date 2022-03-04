package io.github.Hattinger04.hamsterEvaluation.model;

import java.io.Serializable;

import io.github.Hattinger04.hamsterEvaluation.debugger.model.Hamster;
import io.github.Hattinger04.hamsterEvaluation.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MouthEmptyException extends MaulLeerException implements Serializable {
	public MouthEmptyException(Hamster hamster) {
		super(hamster);
	}
	
	public MouthEmptyException(MaulLeerException exc) {
		super(exc.hamster);
	}
}