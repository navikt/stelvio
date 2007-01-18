package no.stelvio.common.transferobject.support;

import no.stelvio.common.transferobject.ContextContainer;
import no.stelvio.common.transferobject.EntityResponse;
import no.stelvio.common.transferobject.TransferObject;

/**
 * Generic response object for services that return an entity.
 * 
 * @author personff564022aedd
 * @version $Id$
 * @deprecated extend <code>ServiceResponse</code>.
 */
public class DefaultEntityResponse<T> extends TransferObject implements EntityResponse<T> {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -5317866932486814787L;

	private T entity;

	/**
	 * Constructor
	 * 
	 * @param entity Entity domain object.
	 */
	public DefaultEntityResponse(T entity) {
		this.entity = entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public T getEntity() {
		return entity;
	}
}
