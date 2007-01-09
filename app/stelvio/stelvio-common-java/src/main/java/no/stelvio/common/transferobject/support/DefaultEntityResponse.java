package no.stelvio.common.transferobject.support;

import no.stelvio.common.transferobject.TransferObject;
import no.stelvio.common.transferobject.EntityResponse;


/**
 * Generic response object for services that return an entity.
 * 
 * @author personff564022aedd
 */
public class DefaultEntityResponse<T> extends TransferObject implements EntityResponse<T> {

	private T entity;
	
	public DefaultEntityResponse(T entity) {
		this.entity = entity;
	}

	/**
	 * {@inheritDoc GenericEntityResponse#getEntity()}
	 */
	public T getEntity() {
		return entity;
	}
}
