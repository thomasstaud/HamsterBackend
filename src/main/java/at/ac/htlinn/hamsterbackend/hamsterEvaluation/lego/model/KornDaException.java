package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterException;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;

/**
 *
 * @author Christian
 */
public class KornDaException extends HamsterException implements Serializable {
        
        int reihe;
        int spalte;
        protected Hamster hamster;
        
        public KornDaException(Hamster hamster, int reihe, int spalte) {
                super(hamster, "5");
                this.hamster = hamster;
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
                return "hamster.KornDaException" + " (" + reihe + ", " + spalte + ")";
        }
        
        
}
