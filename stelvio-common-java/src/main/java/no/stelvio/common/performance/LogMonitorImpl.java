package no.stelvio.common.performance;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Monitor implementation that using logging functionality.
 *
 * @author person7553f5959484
 * @version $Revision: 753 $ $Author: psa2920 $ $Date: 2004-06-23 12:03:02 +0200 (Wed, 23 Jun 2004) $
 */
public final class LogMonitorImpl extends AbstractMonitor {

	//	The name of the log to send messages to
	private static final String PERFORMANCE_LOG = "PERFORMANCE_LOG";

	// Delimiter	
	private static final String DELIM = ", ";

	//	The performance log
	private Log log = null;

	// The hasmap where the timestamps for each key are stored
	private HashMap hashMap = new HashMap();

	/**
	 * Constructs a default LogMonitorImpl using
	 * PERFORMANCE_LOG as the log name.
	 */
	public LogMonitorImpl() {
		log = LogFactory.getLog(PERFORMANCE_LOG);
	}

	/**
	 * @see no.stelvio.common.performance.Monitor#start(MonitorKey)
	 */
	public void start(MonitorKey key) {
		if (key.getLevel() <= level) {
			this.start(key, "");
		}
	}

	/**
	 * @see no.stelvio.common.performance.Monitor#fail(MonitorKey)
	 */
	public void fail(MonitorKey key) {
		if (key.getLevel() <= level) {
			this.fail(key, 0);
		}
	}

	/**
	 * @see no.stelvio.common.performance.Monitor#start(no.stelvio.common.performance.MonitorKey, java.lang.String)
	 */
	public void start(MonitorKey key, String contextName) {
		if (key.getLevel() <= level) {
			log.info("START, " + key + DELIM + contextName);
			try {
				TimeElement startTime = new TimeElement(System.currentTimeMillis());
				startTime.setContextName(contextName);

				// Check if the thread already contains monitoring info
				Stack stack = (Stack) hashMap.get((Object) Thread.currentThread());
				if (null == stack) {
					stack = new Stack();
				}
				stack.push(startTime);

				// Store the stack for the current thread
				hashMap.put((Object) Thread.currentThread(), stack);
			} catch (Throwable t) {
				log.error("Failed to start monitoring " + key + DELIM + contextName, t);
			}
		}
	}

	/**
	 * @see no.stelvio.common.performance.Monitor#end(no.stelvio.common.performance.MonitorKey)
	 */
	public void end(MonitorKey key) {
		if (key.getLevel() <= level) {
			long totalDuration = 0;
			long nestedDuration = 0;
			String contextName = "";
			try {
				long currentTime = System.currentTimeMillis();

				// Retrieve the stack for the current thread
				Stack stack = (Stack) hashMap.get((Object) Thread.currentThread());

				// Start must have been called for each end and opposite;
				// end must be called for each start that has been called
				TimeElement timeElement = (TimeElement) stack.pop();

				// Calculate total duration for current element
				totalDuration = currentTime - timeElement.getStartTime();
				nestedDuration = timeElement.getNestedDuration();

				// Get the contextName from the TimeElement
				contextName = timeElement.getContextName();

				// If the stack is not empty, this is a nested element
				// We need to tell the next element in the stack the
				// total duration of this time element
				try {
					TimeElement callerTimeElement = (TimeElement) stack.pop();
					callerTimeElement.addNestedDuration(totalDuration);

					// Push the updated time element into the stack again
					stack.push(callerTimeElement);
				} catch (EmptyStackException ese) {
					// This is not a nested time element, this is the top
					// Nothing more needed
					if (log.isDebugEnabled()) {
						log.debug("This is not a nested time element, this is the top. Nothing more needed");
					}
				}

				// Store the updated stack for the current thread
				hashMap.put((Object) Thread.currentThread(), stack);
			} catch (Throwable t) {
				log.error("Failed to end monitoring " + key + DELIM + contextName, t);
			}
			log.info("END  , " + key + DELIM + contextName + DELIM + totalDuration + DELIM + (totalDuration - nestedDuration));
		}
	}

	/**
	 * @see no.stelvio.common.performance.Monitor#fail(no.stelvio.common.performance.MonitorKey, int)
	 */
	public void fail(MonitorKey key, int lineNumber) {
		if (key.getLevel() <= level) {
			long totalDuration = 0;
			long nestedDuration = 0;
			String contextName = "";
			try {
				long currentTime = System.currentTimeMillis();

				// Retrieve the stack for the current thread
				Stack stack = (Stack) hashMap.get((Object) Thread.currentThread());

				// Start must have been called for each end and opposite;
				// end must be called for each start that has been called
				TimeElement timeElement = (TimeElement) stack.pop();

				// Calculate total duration for current element
				totalDuration = currentTime - timeElement.getStartTime();
				nestedDuration = timeElement.getNestedDuration();

				//get the context from the time element
				contextName = timeElement.getContextName();
				if (lineNumber != 0) {
					contextName = contextName + "[ " + String.valueOf(lineNumber) + " ]";
				}

				// If the stack is not empty, this is a nested element
				// We need to tell the next element in the stack the
				// total duration of this time element
				try {
					TimeElement callerTimeElement = (TimeElement) stack.pop();
					callerTimeElement.addNestedDuration(totalDuration);

					// Push the updated time element into the stack again
					stack.push(callerTimeElement);
				} catch (EmptyStackException ese) {
					// This is not a nested time element, this is the top
					// Nothing more needed
					if (log.isDebugEnabled()) {
						log.debug("This is not a nested time element, this is the top. Nothing more needed");
					}
				}

				// Store the updated stack for the current thread
				hashMap.put((Object) Thread.currentThread(), stack);
			} catch (Throwable t) {
				log.error("Failed to fail monitoring " + key + DELIM + contextName, t);
			}
			log.warn("FAIL , " + key + DELIM + contextName + DELIM + totalDuration + DELIM + (totalDuration - nestedDuration));
		}
	}
}
