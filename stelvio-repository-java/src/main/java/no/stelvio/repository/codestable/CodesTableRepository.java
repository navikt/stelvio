package no.stelvio.repository.codestable;

import java.util.List;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

/**
 * Interface for the repository used to retrieve codestable items.
 * 
 *
 */
public interface CodesTableRepository {

	/**
	 * Retrieves all codestable items specified by the CodesTableItem class.
	 * 
	 * @param codestableItem
	 *            AbstractCodesTableItem implementation that may be instantiated (non-abstract)
	 * @param <V>
	 *            an object type
	 * @param <T>
	 *            item type
	 * @return list of specified CodesTableItem
	 */
	<T extends AbstractCodesTableItem<? extends Enum, V>, V> List<T> findCodesTableItems(Class<T> codestableItem);
}
