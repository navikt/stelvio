package no.stelvio.common.security.authorization.method;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A filter used to filter Collections.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class CollectionFilterer implements DataFilterer {

	private Collection collection;

	private Iterator collectionIter;

	private Set<Object> removeList;

	/**
	 * Sets the collection to filter.
	 * 
	 * @param collection
	 *            the collection to filter.
	 */
	public CollectionFilterer(Collection collection) {
		this.collection = collection;

		// We create a Set of objects to be removed from the Collection,
		// as ConcurrentModificationException prevents removal during
		// iteration, and making a new Collection to be returned is
		// problematic as the original Collection implementation passed
		// to the method may not necessarily be re-constructable (as
		// the Collection(collection) constructor is not guaranteed and
		// manually adding may lose sort order or other capabilities)
		removeList = new HashSet<Object>();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getFilteredObject() {
		// Now the Iterator has ended, remove Objects from Collection
		Iterator<Object> removeIter = removeList.iterator();
		while (removeIter.hasNext()) {
			collection.remove(removeIter.next());
		}
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator iterator() {
		collectionIter = collection.iterator();
		return collectionIter;
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(Object object) {
		removeList.add(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		collection.clear();
	}

}
