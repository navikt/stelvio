package no.stelvio.common.security.acegi;
import java.util.Iterator;
public interface DataFilterer {
	 
	//~ Methods ========================================================================================================
    /**
     * Gets the filtered collection or array.
     *
     * @return the filtered collection or array
     */
    public Object getFilteredObject();

    /**
     * Returns an iterator over the filtered collection or array.
     *
     * @return an Iterator
     */
    public Iterator iterator();
    
    /**
     * Removes the the given object from the resulting list.
     *
     * @param object the object to be removed
     */
    public void remove(Object object);

    /**
     * Removes all the items in the collection.
     */
    public void clear();
}
