package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import java.io.Serializable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class KachelLeerException extends HamsterException
		implements
			Serializable {
	int reihe;
	int spalte;
	
	public KachelLeerException(Hamster hamster, int reihe, int spalte) {
		super(hamster, "0");
		this.reihe = reihe;
		this.spalte = spalte;
	}

	public int getReihe() {
		return reihe;
	}

	public int getSpalte() {
		return spalte;
	}
	
	public String toString() {
		return "hamster.KachelLeerException" + " (" + reihe + ", " + spalte + ")";
	}
}