package at.ac.htlinn.hamsterEvaluation.lego.model;


import at.ac.htlinn.hamsterEvaluation.model.HamsterException;
import at.ac.htlinn.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;
import at.ac.htlinn.hamsterEvaluation.workbench.Utils;
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

