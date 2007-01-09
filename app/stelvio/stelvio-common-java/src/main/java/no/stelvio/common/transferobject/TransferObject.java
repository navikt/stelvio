package no.stelvio.common.transferobject;

import java.io.Serializable;
import no.stelvio.common.context.RequestContext;

/**
 * Base class for transfer objects. Transfer objects are thought useful for wrapping parameters and return values of
 * business services.
 * 
 * @author personff564022aedd
 */
public abstract class TransferObject implements Serializable {

	private ContextContainer container;

	/**
	 * @return {@link RequestContext}
	 */
	public ContextContainer getContextContainer() {
		return container;
	}

	/**
	 * @param contextContainer {@link RequestContext}
	 */
	public void setContextContainer(ContextContainer contextContainer) {
		this.container = contextContainer;
	}
}
