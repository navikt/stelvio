package no.stelvio.common.transferobject;


/**
 * Generic response object for services that return an entity.
 * 
 * @author personff564022aedd
 */
public class EntityResponseImpl<T> extends TransferObject implements EntityResponse<T> {

	private T entity;
	
	public EntityResponseImpl(T entity) {
		this.entity = entity;
	}

	/**
	 * {@inheritDoc GenericEntityResponse#getEntity()}
	 */
	public T getEntity() {
		return entity;
	}
}
