package no.stelvio.common.error.old;

import java.rmi.RemoteException;

/**
 * RemoteException wrapper for WebSphere Application Server 5.1.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: WebSphere51RemoteExceptionWrapper.java 2471 2005-09-13 08:32:55Z skb2930 $
 * @deprecated Check the new implementation
 */
public class WebSphere51RemoteExceptionWrapper implements ExceptionWrapper {

	/** 
	 * {@inheritDoc}
	 */
	public Throwable getCause(Throwable t) {
		Throwable cause = t.getCause();

		while (cause instanceof RemoteException) {
			// WebSphere Application Server 5.1 likes to wrap a java.rmi.RemoteException 
			// inside of a java.rmi.ServerException
			cause = cause.getCause();
		}
		
		return cause;
	}
}
