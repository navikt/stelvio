package no.stelvio.common.transferobject;

import java.io.Serializable;

/**
 * Base Transfer Object interface.
 * <p>
 * Transfer Objects are used as input (Request) and output (Response) in business interface methods.
 * All transfer objects contain a container that can be used to hold contect information.  
 *  
 * @author personff564022aedd
 * @author person15754a4522e7
 * @version $Id$
 */
public interface TransferObject extends Serializable {

	/**
	 * Get context container.
	 * 
	 * @return {@link ContextContainer}
	 */
	ContextContainer getContextContainer();

	/**
	 * Sets context container.
	 * 
	 * @param contextContainer
	 *            {@link ContextContainer}
	 */
	void setContextContainer(ContextContainer contextContainer);
}
