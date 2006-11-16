package no.stelvio.common.security.acegi;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/**
 * A filter used to filter Collections.
 */
public class CollectionFilterer implements DataFilterer {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    private Collection collection;

    // collectionIter offers significant performance optimisations (as
    // per acegisecurity-developer mailing list conversation 19/5/05)
    private Iterator collectionIter;
    private Set<Object> removeList;

    //~ Constructors ===================================================================================================

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

    //~ Methods ========================================================================================================

    /**
     * 
     * @see org.acegisecurity.afterinvocation.Filterer#getFilteredObject()
     */
    public Object getFilteredObject() {
        // Now the Iterator has ended, remove Objects from Collection
        Iterator removeIter = removeList.iterator();
        while (removeIter.hasNext()) {
            collection.remove(removeIter.next());
        }
        return collection;
    }

    /**
     * 
     * @see org.acegisecurity.afterinvocation.Filterer#iterator()
     */
    public Iterator iterator() {
        collectionIter = collection.iterator();
        return collectionIter;
    }

    /**
     * 
     * @see org.acegisecurity.afterinvocation.Filterer#remove(java.lang.Object)
     */
    public void remove(Object object) {
        removeList.add(object);
    }
    
    public void clear(){
    	collection.clear();
    }
    
}
