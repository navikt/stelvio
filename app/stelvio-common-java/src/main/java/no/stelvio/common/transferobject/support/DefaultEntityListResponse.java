package no.stelvio.common.transferobject.support;

import java.util.List;

import no.stelvio.common.transferobject.TransferObject;
import no.stelvio.common.transferobject.EntityListResponse;


/**
 * Generic response object for services that return a <code>List</code> of entities.
 * 
 * @author personff564022aedd
 */
public class DefaultEntityListResponse<T> extends TransferObject implements EntityListResponse<T> {

	private List<T> entities;
	
	public DefaultEntityListResponse(List<T> entities) {
		this.entities = entities;
	}

	/**  
	 * {@inheritDoc EntityListResponse#getEntities()}
	 */
	public List<T> getEntities() {
		
		return entities;
	}

}
