package no.stelvio.common.security.authorization.method;

import java.util.Iterator;

/**
 * Filter strategy interface.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public interface DataFilterer {

	/**
	 * Gets the filtered collection or array.
	 * 
	 * @return the filtered collection or array
	 */
	Object getFilteredObject();

	/**
	 * Returns an iterator over the filtered collection or array.
	 * 
	 * @return an Iterator
	 */
	Iterator iterator();

	/**
	 * Removes the the given object from the resulting list.
	 * 
	 * @param object
	 *            the object to be removed
	 */
	void remove(Object object);

	/**
	 * Removes all the items in the collection.
	 */
	void clear();
}
