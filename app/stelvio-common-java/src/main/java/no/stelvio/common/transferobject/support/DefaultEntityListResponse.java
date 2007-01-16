package no.stelvio.common.transferobject.support;

import java.util.List;

import no.stelvio.common.transferobject.ContextContainer;
import no.stelvio.common.transferobject.EntityListResponse;

/**
 * Generic response object for services that return a <code>List</code> of
 * entities.
 * 
 * @author personff564022aedd
 * @version $Id$
 */
public class DefaultEntityListResponse<T> extends DefaultTransferObject
		implements EntityListResponse<T> {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7279019422169717947L;

	private List<T> entities;

	/**
	 * Constructor.
	 * 
	 * @param entities
	 *            List of entities.
	 */
	public DefaultEntityListResponse(List<T> entities) {
		this(null, entities);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Response context.
	 * @param entities
	 *            List of entities.
	 */
	public DefaultEntityListResponse(ContextContainer context, List<T> entities) {
		super(context);
		this.entities = entities;
	}

	/**
	 * {@inheritDoc EntityListResponse#getEntities()}
	 */
	public List<T> getEntities() {

		return entities;
	}

}
