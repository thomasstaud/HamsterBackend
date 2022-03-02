package io.github.Hattinger04.hamster.model;

import java.io.Serializable;

import io.github.Hattinger04.hamster.debugger.model.Hamster;
import io.github.Hattinger04.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class WallInFrontException extends MauerDaException implements Serializable {

	public WallInFrontException(Hamster hamster, int reihe, int spalte) {
		super(hamster, reihe, spalte);
	}
	
	public WallInFrontException(MauerDaException e) {
		super(e.hamster, e.reihe, e.spalte);

	}


}