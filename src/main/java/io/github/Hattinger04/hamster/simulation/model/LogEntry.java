package io.github.Hattinger04.hamster.simulation.model;

import io.github.Hattinger04.hamster.model.Instruction;

/**
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class LogEntry {
	private Instruction instruction;
	private Object result;
	
	public LogEntry(Instruction instruction, Object result) {
		this.instruction = instruction;
		this.result = result;
	}
	
	public Instruction getInstruction() {
		return instruction;
	}
	public Object getResult() {
		return result;
	}
}
