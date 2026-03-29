/*
 * CornThereException.java
 *
 * Created on 10. Oktober 2007, 10:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;

import java.io.Serializable;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.Hamster;
/**
 *
 * @author Christian
 */
public class CornThereException extends KornDaException implements Serializable{
        
        /** Creates a new instance of CornThereException */
        public CornThereException(Hamster hamster, int reihe, int spalte) {
                super(hamster, reihe, spalte);
        }
        
        public CornThereException(KornDaException e) {
		super(e.hamster, e.reihe, e.spalte);
	}
        
}
