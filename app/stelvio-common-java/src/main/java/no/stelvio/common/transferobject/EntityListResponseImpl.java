package no.stelvio.common.transferobject;

import java.util.List;


/**
 * Generic response object for services that return a <code>List</code> of entities.
 * 
 * @author personff564022aedd
 */
public class EntityListResponseImpl<T> extends TransferObject implements EntityListResponse<T> {

	private List<T> entities;
	
	public EntityListResponseImpl(List<T> entities) {
		this.entities = entities;
	}

	/**  
	 * {@inheritDoc EntityListResponse#getEntities()}
	 */
	public List<T> getEntities() {
		
		return entities;
	}

}
