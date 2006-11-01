package no.stelvio.common.transferobject;

import java.io.Serializable;


/**
 * Business requests are serializable and (may) contain a ContextContainer.
 * 
 * @author personff564022aedd
 */
public interface BusinessRequest extends Serializable {
	
	/**
	 * @return {@link ContextContainer}
	 */
	ContextContainer getContextContainer();
	
	/**
	 * @param contextContainer {@link RequestContext}
	 */
	void setContextContainer(ContextContainer contextContainer);
}
