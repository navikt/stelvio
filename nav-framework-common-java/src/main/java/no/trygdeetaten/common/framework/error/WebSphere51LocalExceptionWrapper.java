package no.trygdeetaten.common.framework.error;

import com.ibm.ejs.container.UnknownLocalException;

/**
 * LocalException wrapper for WebSphere Application Server 5.1.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: WebSphere51LocalExceptionWrapper.java 2471 2005-09-13 08:32:55Z skb2930 $
 */
public class WebSphere51LocalExceptionWrapper implements ExceptionWrapper {

	/** 
	 * {@inheritDoc}
	 */
	public Throwable getCause(Throwable t) {
		Throwable cause = t.getCause();

		while (cause instanceof UnknownLocalException) {
			// WebSphere Application Server 5.1 likes to wrap runtime exceptions
			// inside of a com.ibm.ejs.container.UnknownLocalException
			cause = cause.getCause();
		}

		return cause;
	}
}
