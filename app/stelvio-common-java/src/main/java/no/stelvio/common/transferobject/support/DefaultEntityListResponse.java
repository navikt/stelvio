package no.stelvio.common.transferobject.support;

import java.util.List;

import no.stelvio.common.transferobject.ContextContainer;
import no.stelvio.common.transferobject.EntityListResponse;
import no.stelvio.common.transferobject.TransferObject;

/**
 * Generic response object for services that return a <code>List</code> of
 * entities.
 * 
 * @author personff564022aedd
 * @version $Id$
 * @deprecated extend <code>ServiceResponse</code>. 
 */
public class DefaultEntityListResponse<T> extends TransferObject implements EntityListResponse<T> {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7279019422169717947L;

	private List<T> entities;

	/**
	 * Constructor.
	 * 
	 * @param entities List of entities.
	 */
	public DefaultEntityListResponse(List<T> entities) {
		this.entities = entities;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getEntities() {
		return entities;
	}
}
