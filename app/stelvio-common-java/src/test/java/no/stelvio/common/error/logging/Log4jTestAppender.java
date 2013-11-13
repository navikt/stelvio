package no.stelvio.common.error.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import no.stelvio.common.error.support.Severity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Appender used to test components logging capabilities.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class Log4jTestAppender extends ConsoleAppender {

	private Map<LogEntry, State> called = new HashMap<LogEntry, State>();

	/**
	 * Verify state.
	 * 
	 * @return true if ok
	 */
	public boolean verify() {
		boolean result = true;
		for (State state : called.values()) {
			if ((!state.shouldCall() && state.wasCalled()) || (state.shouldCall() && !state.wasCalled())) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * Get log entries.
	 * 
	 * @return log entries
	 */
	public Set<LogEntry> getLogEntries() {
		return called.keySet();
	}

	/**
	 * Print results on stdout.
	 */
	public void printResults() {

		for (State state : called.values()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Logging message '").append(state.getMsg()).append("' ");
			sb.append("at level '").append(state.getLogLevel()).append("' ");
			sb.append("was ").append(state.shouldCall() ? "expected " : "not expected ");
			sb.append(state.wasCalled() ? "and called" : "and was not called");
			System.out.println(sb.toString());
		}
	}

	/**
	 * Can be used in conjuction with verify, to get a message that explains why verify returned false. Typically used in a
	 * unitTest like this: <code>
	 * assertTrue(getVerificationResults(),verify)
	 * </code>
	 * 
	 * @return verificationresults
	 */
	public String getVerificationResults() {
		StringBuffer sb = new StringBuffer();
		for (State state : called.values()) {
			sb.append("Logging message '").append(state.getMsg()).append("' ");
			sb.append("at level '").append(state.getLogLevel()).append("' ");
			sb.append("was ").append(state.shouldCall() ? "expected " : "not expected ");
			sb.append(state.wasCalled() ? "and called" : "and was not called");
		}
		return sb.toString();
	}

	/**
	 * Adds message that is expected to be logged at a certain level.
	 * 
	 * @param msg message
	 * @param logLevel
	 *            defined by {@link Severity}
	 */
	public void addMessageToVerify(String msg, Severity logLevel) {
		Level log4Level = findLog4jLevel(logLevel);
		called.put(new LogEntry(msg, log4Level), new State(msg, true, false, log4Level));
	}

	/***
	 * Adds message that is expected to be logged at a certain level.
	 * 
	 * @param msg message
	 * @param logLevel
	 *            as a String
	 */
	public void addMessageToVerify(String msg, String logLevel) {
		Level log4Level = findLog4jLevel(logLevel);
		called.put(new LogEntry(msg, log4Level), new State(msg, true, false, log4Level));
	}

	/**
	 * Adds message to verify.
	 * 
	 * @param msg message
	 * @param log4jLevel
	 *            defined by {@link Level}
	 */
	public void addMessageToVerify(String msg, Level log4jLevel) {
		called.put(new LogEntry(msg, log4jLevel), new State(msg, true, false, log4jLevel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void doAppend(LoggingEvent loggingEvent) {
		String msg = (loggingEvent.getMessage() != null) ? loggingEvent.getMessage().toString() : "null";
		LogEntry key = new LogEntry(msg, findSeverity(loggingEvent.getLevel()));
		if (called.containsKey(key)) {
			((State) called.get(key)).setCalled(true);
		} else {
			called.put(key, new State(msg, false, true, key.getLogLevel()));
		}
	}

	/**
	 * Find log level.
	 * 
	 * @param severity severity
	 * @return log level
	 */
	private Level findLog4jLevel(String severity) {
		Level log4jLevel = null;
		if (severity.equalsIgnoreCase("debug")) {
			log4jLevel = Level.DEBUG;
		} else if (severity.equalsIgnoreCase("info")) {
			log4jLevel = Level.INFO;
		} else if (severity.equalsIgnoreCase("warn")) {
			log4jLevel = Level.WARN;
		} else if (severity.equalsIgnoreCase("ERROR")) {
			log4jLevel = Level.ERROR;
		} else if (severity.equalsIgnoreCase("FATAL")) {
			log4jLevel = Level.FATAL;
		}
		return log4jLevel;
	}

	/**
	 * Find log level.
	 * 
	 * @param severity severity
	 * @return log level
	 */
	private Level findLog4jLevel(Severity severity) {
		Level log4jLevel = null;
		switch (severity) {
		case DEBUG:
			log4jLevel = Level.DEBUG;
			break;
		case INFO:
			log4jLevel = Level.INFO;
			break;
		case WARN:
			log4jLevel = Level.WARN;
			break;
		case ERROR:
			log4jLevel = Level.ERROR;
			break;
		case FATAL:
			log4jLevel = Level.FATAL;
			break;
		default:
			break;
		}
		return log4jLevel;

	}

	/**
	 * Find severity.
	 * 
	 * @param logLevel log level
	 * @return severity
	 */
	private Severity findSeverity(Level logLevel) {
		Severity level = null;
		if (logLevel.equals(Level.FATAL)) {
			level = Severity.FATAL;
		} else if (logLevel.equals(Level.ERROR)) {
			level = Severity.ERROR;
		} else if (logLevel.equals(Level.WARN)) {
			level = Severity.WARN;
		} else if (logLevel.equals(Level.INFO)) {
			level = Severity.INFO;
		} else if (logLevel.equals(Level.DEBUG)) {
			level = Severity.DEBUG;
		}
		return level;
	}

	/**
	 * Check if message is logged.
	 * 
	 * @param errorMessage message
	 * @return true if logged
	 */
	public boolean isMessageLogged(String errorMessage) {
		boolean isLogged = false;
		for (State state : called.values()) {
			if (state.msg.indexOf(errorMessage) != -1) {
				isLogged = true;
				break;
			}
		}
		return isLogged;
	}

	/**
	 * Log entry. 
	 */
	public class LogEntry {
		private String msg;
		private Level log4LogLevel;

		/**
		 * Creates a new instance of LogEntry.
		 *
		 * @param msg message
		 * @param logLevel log level
		 */
		public LogEntry(String msg, Severity logLevel) {
			this.msg = msg;
			this.log4LogLevel = findLog4jLevel(logLevel);
		}

		/**
		 * Creates a new instance of LogEntry.
		 *
		 * @param msg message
		 * @param logLevel log level
		 */
		public LogEntry(String msg, Level logLevel) {
			this.msg = msg;
			this.log4LogLevel = logLevel;
		}

		/**
		 * {@inheritDoc}
		 */
		public int hashCode() {
			HashCodeBuilder builder = new HashCodeBuilder();
			builder.append(msg);
			builder.append(log4LogLevel);
			return builder.toHashCode();
		}

		/**
		 * Get log level.
		 * 
		 * @return log level
		 */
		public Level getLogLevel() {
			return log4LogLevel;
		}

		/**
		 * Get message.
		 * 
		 * @return message
		 */
		public String getMsg() {
			return msg;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean equals(Object other) {
			boolean equal = false;
			if (other instanceof LogEntry) {
				LogEntry logEntry = (LogEntry) other;
				EqualsBuilder equals = new EqualsBuilder();
				equals.append(this.getMsg(), logEntry.getMsg());
				equals.append(this.getLogLevel(), logEntry.getLogLevel());
				equal = equals.isEquals();
			}
			return equal;
		}
	}

	/**
	 * State.
	 */
	class State {

		private String msg;
		private boolean shouldCall;
		private boolean called;
		private Level logLevel;

		/**
		 * Creates a new instance of State.
		 *
		 * @param msg message
		 * @param shouldCall should call
		 * @param wasCalled was called
		 * @param logLevel log level
		 */
		public State(String msg, boolean shouldCall, boolean wasCalled, Level logLevel) {
			this.msg = msg;
			this.shouldCall = shouldCall;
			this.called = wasCalled;
			this.logLevel = logLevel;
		}

		/**
		 * Get message.
		 * 
		 * @return message
		 */
		public String getMsg() {
			return msg;
		}

		/**
		 * Should call.
		 * 
		 * @return should call
		 */
		public boolean shouldCall() {
			return shouldCall;
		}

		/**
		 * Was called.
		 * 
		 * @return was called
		 */
		public boolean wasCalled() {
			return called;
		}

		/**
		 * Set was called.
		 * 
		 * @param called was called
		 */
		public void setCalled(boolean called) {
			this.called = called;
		}

		/**
		 * Get log level.
		 * 
		 * @return log level
		 */
		public Level getLogLevel() {
			return logLevel;
		}
	}
}
