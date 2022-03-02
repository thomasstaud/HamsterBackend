package io.github.Hattinger04.hamster.lego.model;


import io.github.Hattinger04.hamster.model.HamsterException;
import io.github.Hattinger04.hamster.debugger.model.Hamster;
import java.io.Serializable;
import io.github.Hattinger04.hamster.workbench.Utils;
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
                return Utils.getResource("hamster.MaulNichtLeerException");
        }
        
        
        
        
}

