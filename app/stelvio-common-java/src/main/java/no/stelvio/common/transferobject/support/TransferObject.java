package no.stelvio.common.transferobject.support;

import java.io.Serializable;

import no.stelvio.common.context.RequestContext;

/**
 * Base class for transfer objects. Transfer objects are thought useful for wrapping parameters and return values of
 * business services.
 * 
 * @author personff564022aedd
 * @deprecated Use <code>ServiceRequest</code>/<code>ServiceResponse</code>.
 */
@Deprecated
public abstract class TransferObject implements Serializable {
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
