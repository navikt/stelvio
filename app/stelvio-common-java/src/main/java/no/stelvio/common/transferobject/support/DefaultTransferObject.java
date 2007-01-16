package no.stelvio.common.transferobject.support;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.transferobject.ContextContainer;
import no.stelvio.common.transferobject.TransferObject;

/**
 * Base class for transfer objects. Transfer objects are thought useful for wrapping parameters and return values of
 * business services.
 * 
 * @author personff564022aedd
 * @version $Id$
 */
public abstract class DefaultTransferObject implements TransferObject  {

	private ContextContainer container;
	
	/**
	 * Constructor.
	 * @param context Context container.
	 */
	public DefaultTransferObject(ContextContainer context) {
		this.container = context;
	}

	/**
	 * Gets context.
	 * @return {@link RequestContext}
	 */
	public ContextContainer getContextContainer() {
		return container;
	}

	/**
	 * Sets contexts.
	 * @param contextContainer {@link RequestContext}
	 */
	public void setContextContainer(ContextContainer contextContainer) {
		this.container = contextContainer;
	}
}
