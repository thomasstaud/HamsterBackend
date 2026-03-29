package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;


import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterException;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;

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

