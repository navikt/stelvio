package no.stelvio.common.transferobject;

/**
 * Generic response for services that return an entity.
 * 
 * @author personff564022aedd
 * @version $Id$
 * @deprecated extend <code>ServiceResponse</code>. 
 */
public interface EntityResponse<T> {
	
	/**
	 * Gets the entity contained in the response of a generic type. 
	 * @return entity T
	 */
	T getEntity();
}
