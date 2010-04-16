/**
 * 
 */
package no.stelvio.common.exception;

import java.io.Serializable;

/**
 * ADT used to represent all properties of Throwable
 * 
 * @author test@example.com
 */
public class ThrowableInfo implements Serializable {
	private static final long serialVersionUID = -541054667291423334L;

	private String className;
	private String localizedMessage;
	private StackTraceElement[] stackTrace;

	public ThrowableInfo(Throwable t) {
		this(t.getClass().getName(), t.getLocalizedMessage(), t.getStackTrace());
	}

	public ThrowableInfo(String className, String localizedMessage, StackTraceElement[] stackTrace) {
		this.className = className;
		this.localizedMessage = localizedMessage;
		this.stackTrace = stackTrace;
	}

	public String getClassName() {
		return className;
	}

	public String getLocalizedMessage() {
		return localizedMessage;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	@Override
	public String toString() {
		if (localizedMessage == null) {
			return className;
		} else {
			return new StringBuilder(className.length() + 2 + localizedMessage.length()).append(className).append(": ").append(
					localizedMessage).toString();
		}
	}
}