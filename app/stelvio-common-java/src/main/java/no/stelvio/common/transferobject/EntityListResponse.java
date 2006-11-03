package no.stelvio.common.transferobject;

import java.util.List;

/**
 * Generic response for services that return a <code>List</code> of entities.
 * 
 * @author personff564022aedd
 */
public interface EntityListResponse<T> extends BusinessResponse {

	/**
	 * @return a List of entities of type T
	 */
	List<T> getEntities();
}
