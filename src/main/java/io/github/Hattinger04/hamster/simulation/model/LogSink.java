package io.github.Hattinger04.hamster.simulation.model;

/**
 * @author Daniel
 */
public interface LogSink {
	public void logEntry(LogEntry logEntry);
	public void clearLog();
}
