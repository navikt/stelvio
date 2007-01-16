package no.stelvio.common.transferobject.support;

import no.stelvio.common.transferobject.ContextContainer;
import no.stelvio.common.transferobject.EntityResponse;

/**
 * Generic response object for services that return an entity.
 * 
 * @author personff564022aedd
 * @version $Id$
 */
public class DefaultEntityResponse<T> extends DefaultTransferObject implements
		EntityResponse<T> {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -5317866932486814787L;

	private T entity;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Response context.
	 * @param entity
	 *            Entity domain.
	 */
	public DefaultEntityResponse(ContextContainer context, T entity) {
		super(context);
		this.entity = entity;

	}

	/**
	 * Constructor
	 * 
	 * @param entity
	 *            Entity domain object.
	 */
	public DefaultEntityResponse(T entity) {
		this(null, entity);
	}

	/**
	 * {@inheritDoc GenericEntityResponse#getEntity()}
	 */
	public T getEntity() {
		return entity;
	}
}
