package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;

/**
 *
 * @author Christian
 */
public class MouthNotEmptyException extends MaulNichtLeerException implements Serializable {
        
        /** Creates a new instance of MouthNotEmptyException */
        public MouthNotEmptyException(Hamster hamster) {
                super(hamster);
        }
        
        
        public MouthNotEmptyException(MaulNichtLeerException e) {
                super(e.hamster);
        }
        
}
    