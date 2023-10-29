package at.ac.htlinn.hamsterEvaluation.simulation.model;

/**
 * @author Daniel
 */
public interface LogSink {
	public void logEntry(LogEntry logEntry);
	public void clearLog();
}
