/**
 * 
 */
package no.stelvio.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception wrapper class used to hold (properties of) a chain of nested exceptions. This class is used to avoid potential
 * marshalling exceptions when propagated through the integration platform (bus)
 * 
 * @author test@example.com
 */
public class RuntimeFault extends RuntimeException {
	private static final long serialVersionUID = 1722270206043293494L;

	private static final StackTraceElement[] EMPTY_STACK_TRACE_ELEMENTS = new StackTraceElement[0];

	private ThrowableInfo[] causes;

	public RuntimeFault(Throwable t) {
		super(t.getMessage());
		List<ThrowableInfo> causes = new ArrayList<ThrowableInfo>();
		addException(t, causes);
		this.causes = causes.toArray(new ThrowableInfo[causes.size()]);
		setStackTrace(EMPTY_STACK_TRACE_ELEMENTS);
	}

	public ThrowableInfo[] getExceptions() {
		return causes;
	}

	private static void addException(Throwable t, List<ThrowableInfo> exceptions) {
		exceptions.add(new ThrowableInfo(t));
		Throwable cause = t.getCause();
		if (cause != null) {
			addException(cause, exceptions);
		}
	}
}