package no.stelvio.common.transferobject;

import java.io.Serializable;

import no.stelvio.common.context.RequestContext;


/**
 * Business requests are serializable and (may) contain a ContextContainer.
 * 
 * @author personff564022aedd
 */
public interface ServiceRequest extends Serializable {
	
	/**
	 * @return {@link ContextContainer}
	 */
	ContextContainer getContextContainer();
	
	/**
	 * @param contextContainer {@link RequestContext}
	 */
	void setContextContainer(ContextContainer contextContainer);
}
