package at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model;

/**
 * @author Daniel
 */
public interface LogSink {
	public void logEntry(LogEntry logEntry);
	public void clearLog();
}
