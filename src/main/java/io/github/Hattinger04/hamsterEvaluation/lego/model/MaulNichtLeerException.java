package io.github.Hattinger04.hamsterEvaluation.lego.model;


import io.github.Hattinger04.hamsterEvaluation.model.HamsterException;
import io.github.Hattinger04.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;
import io.github.Hattinger04.hamsterEvaluation.workbench.Utils;
/**
 *
 * @author Christian
 */
public class MaulNichtLeerException extends HamsterException implements Serializable {
        
        protected Hamster hamster;
        
        public MaulNichtLeerException(Hamster hamster) {
                super(hamster, "6");
                this.hamster = hamster;
                
        }
        
        public String toString() {
                return "hamster.MaulNichtLeerException";
        }
        
        
        
        
}

