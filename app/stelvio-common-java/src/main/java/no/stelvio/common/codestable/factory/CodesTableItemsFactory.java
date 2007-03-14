package no.stelvio.common.codestable.factory;

import java.util.List;

import org.springmodules.cache.annotations.Cacheable;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodicItem;

/**
 * Interface defining functionality for creating codes table items. Must be implemented by the project using the codes
 * table API.
 *
 * @author personf8e9850ed756, Accenture
 * @version $Id$
 */
public interface CodesTableItemsFactory {
	static final String CACHE_MODEL_ID = "no.stelvio.common.codestable";

	/**
	 * Creates a list of <code>CodesTableItem</code>s.
	 *
	 * @param codesTableItemClass the type of <code>CodesTableItem</code>s to create. Must be a subclass of
	 * <code>CodesTableItem</code>.
	 * @return a list of codes table items which is of type <code>codesTableItemClass</code>.
	 * @throws CodesTableNotFoundException thrown when the codes table which have the list of
	 * <code>CodesTableItem</code>s is not found.
	 */
	@Cacheable(modelId = CACHE_MODEL_ID)
	<T extends CodesTableItem<K, V>, K extends Enum, V>
		List<T> createCodesTableItems(Class<T> codesTableItemClass) throws CodesTableNotFoundException;

	/**
	 * Creates a list of <code>CodesTablePeriodicItem</code>s.
	 *
	 * @param codesTablePeriodicItemClass the type of <code>CodesTableItem</code>s to create. Must be a subclass of
	 * <code>CodesTableItem</code>.
	 * @return a list of codes table items which is of type <code>codesTableItemClass</code>.
	 * @throws CodesTableNotFoundException thrown when the codes table which have the list of
	 * <code>CodesTableItem</code>s is not found.
	 */
	@Cacheable(modelId = CACHE_MODEL_ID)
	<T extends CodesTablePeriodicItem<K, V>, K extends Enum, V>
		List<T> createCodesTablePeriodicItems(Class<T> codesTablePeriodicItemClass) throws CodesTableNotFoundException;
}
