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
public class ServiceRequest implements Serializable {
	/** Used to ensure backwards compatability when serializing instances. */
	private static final long serialVersionUID = -3970002011961058329L;
	/** The <code>RequestContext</code> instance to use. */
	private RequestContext requestContext;

	/**
	 * @return {@link RequestContext}
	 */
	public RequestContext getRequestContext() {
		return requestContext;
	}

	/**
	 * Should only be set by subclasses or by reflection through interceptors used when the transfer object goes through
	 * layers.
	 *
	 * @param requestContext The <code>RequestContext</code> instance to use.
	 */
	protected void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}
}
