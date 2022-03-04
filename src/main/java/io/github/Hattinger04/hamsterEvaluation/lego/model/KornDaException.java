package io.github.Hattinger04.hamsterEvaluation.lego.model;

import io.github.Hattinger04.hamsterEvaluation.model.HamsterException;
import io.github.Hattinger04.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;
import io.github.Hattinger04.hamsterEvaluation.workbench.Utils;
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
                return Utils.getResource("hamster.KornDaException") + " (" + reihe + ", " + spalte + ")";
        }
        
        
}
