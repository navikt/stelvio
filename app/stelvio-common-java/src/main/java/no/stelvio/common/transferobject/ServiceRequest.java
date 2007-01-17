package no.stelvio.common.transferobject;

import java.io.Serializable

import no.stelvio.common.context.RequestContext;


/**
 * Business requests are serializable and (may) contain a RequestContext.
 * 
 * @author personff564022aedd
 * @author person15754a4522e7
 * @author personf8e9850ed756
 * @version $Id$
 */
public interface ServiceRequest extends Serializable {
	/**
	 * @return {@link RequestContext}
	 */
	RequestContext getRequestContext();
}
