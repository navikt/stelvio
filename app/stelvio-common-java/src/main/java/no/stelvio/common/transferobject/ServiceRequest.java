package no.stelvio.common.transferobject;

import java.io.Serializable;

import no.stelvio.common.context.RequestContext;


/**
 * The base class for all service requests, which are serializable and may contain a <code>RequestContext</code>.
 * 
 * @author personff564022aedd
 * @author person15754a4522e7
 * @author personf8e9850ed756
 * @see RequestContext
 * @version $Id$
 */
public abstract class ServiceRequest implements Serializable {
	/** Used to ensure backwards compatability when serializing instances. */
	private static final long serialVersionUID = -3970002011961058329L;
	/** The <code>RequestContext</code> instance to use. */
	private RequestContext requestContext;

	/**
	 * Returns the <code>RequestContext</code> instance to use.
	 * 
	 * @return the <code>RequestContext</code> instance to use.
	 * @see no.stelvio.common.context.RequestContext
	 * @deprecated Application code should not work directly with this <code>RequestContext</code>, instead use
	 * {@link no.stelvio.common.context.RequestContextHolder} to get hold of the current instance.
	 */
	@Deprecated
	public RequestContext getRequestContext() {
		return requestContext;
	}

	/**
	 * Should only be set by subclasses or by reflection through interceptors used when the transfer object goes through
	 * layers.
	 *
	 * @param requestContext The <code>RequestContext</code> instance to use.
	 * @see no.stelvio.common.context.RequestContext
	 * @deprecated Application code should not work directly with this <code>RequestContext</code>, instead use
	 * {@link no.stelvio.common.context.RequestContextHolder} to get hold of the current instance.
	 */
	@Deprecated
	protected void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}
}
