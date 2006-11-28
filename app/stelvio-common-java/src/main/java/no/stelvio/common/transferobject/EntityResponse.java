package no.stelvio.common.transferobject;

/**
 * Generic response for services that return an entity.
 * 
 * @author personff564022aedd
 */
public interface EntityResponse<T> extends ServiceResponse {
	
	/**
	 * @return entity T
	 */
	T getEntity();
}
