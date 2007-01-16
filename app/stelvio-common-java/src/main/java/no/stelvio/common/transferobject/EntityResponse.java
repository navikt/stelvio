package no.stelvio.common.transferobject;

/**
 * Generic response for services that return an entity.
 * 
 * @author personff564022aedd
 * @version $Id$
 */
public interface EntityResponse<T> extends ServiceResponse {
	
	/**
	 * Gets the entity contained in the response of a generic type. 
	 * @return entity T
	 */
	T getEntity();
}
