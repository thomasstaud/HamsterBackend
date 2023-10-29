package at.ac.htlinn.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterEvaluation.debugger.model.Hamster;
import at.ac.htlinn.hamsterEvaluation.workbench.Utils;

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