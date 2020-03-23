package no.stelvio.common.codestable.factory;

import java.util.List;

import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * Interface defining functionality for creating codes table items. Must be implemented by the project using the codes
 * table API.
 *
 */
public interface CodesTableItemsFactory {

	/**
	 * Creates a list of <code>CodesTableItem</code>s.
	 *
	 * @param <T> <code>AbstractCodesTablePeriodicItem</code>'s or subclasses of 
	 * <code>AbstractCodesTablePeriodicItem</code> that the <code>codesTablePeriodicItemClass</code> can hold values of.
	 * @param <K> an enum type variable
	 * @param <V> an type variable
	 * @param codesTableItemClass the type of <code>CodesTableItem</code>s to create. Must be a subclass of
	 * <code>CodesTableItem</code>.
	 * @return a list of codes table items which is of type <code>codesTableItemClass</code>.
	 * @throws CodesTableNotFoundException thrown when the codes table which have the list of
	 * <code>CodesTableItem</code>s is not found.
	 */
	<T extends AbstractCodesTableItem<K, V>, K extends Enum, V>
		List<T> createCodesTableItems(Class<T> codesTableItemClass) throws CodesTableNotFoundException;

	/**
	 * Creates a list of <code>CodesTablePeriodicItem</code>s.
	 *
	 * @param <T> <code>AbstractCodesTablePeriodicItem</code>'s or subclasses of 
	 * <code>AbstractCodesTablePeriodicItem</code> that the <code>codesTablePeriodicItemClass</code> can hold values of.
	 * @param <K> an enum type variable
	 * @param <V> an type variable
	 * @param codesTablePeriodicItemClass the type of <code>CodesTableItem</code>s to create. Must be a subclass of
	 * <code>CodesTableItem</code>.
	 * @return a list of codes table items which is of type <code>codesTableItemClass</code>.
	 * @throws CodesTableNotFoundException thrown when the codes table which have the list of
	 * <code>CodesTableItem</code>s is not found.
	 */
	<T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V>
		List<T> createCodesTablePeriodicItems(Class<T> codesTablePeriodicItemClass) throws CodesTableNotFoundException;
}
