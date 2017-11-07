package no.stelvio.common.codestable.support;

import java.util.Collection;
import java.util.Comparator;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableEmptyException;
import no.stelvio.common.codestable.DuplicateItemsException;

/**
 * Implementation of CodesTable used to handle a codestable, its belonging values and predicates for filtering the items
 * in the codestable.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 *
 *@param <T> a Throwable type variable
 *@param <K> an enum type variable
 *@param <V> a type variable
 */
public class DefaultCodesTable<T extends AbstractCodesTableItem<K, V>, K extends Enum, V>
		extends AbstractCodesTable<T, K, V> implements CodesTable<T, K, V> {
	private static final long serialVersionUID = -1882474781384872740L;

	/**
	 * Creates a <code>DefaultCodesTable</code> with a collection of <code>CodesTableItem</code>s.
	 *
	 * @param codesTableItems collection of <code>CodesTableItem</code>s this <code>DefaultCodesTable</code> consists of.
	 * @param codesTableItemsClass the class of the items to hold.
	 * @throws CodesTableEmptyException if the collection is null or empty.
	 * @throws DuplicateItemsException if the collection has duplicate entries.
	 */
	public DefaultCodesTable(Collection<T> codesTableItems, Class<? extends AbstractCodesTableItem> codesTableItemsClass)
			throws CodesTableEmptyException, DuplicateItemsException {
		init(codesTableItems, codesTableItemsClass);
	}
	
	/**
	 * Creates a <code>DefaultCodesTable</code> with a collection of <code>CodesTableItem</code>s.
	 *
	 * @param codesTableItems collection of <code>CodesTableItem</code>s this <code>DefaultCodesTable</code> consists of.
	 * @param codesTableItemsClass the class of the items to hold.
	 * @param comparator a Comparator object
	 * @throws CodesTableEmptyException if the collection is null or empty.
	 * @throws DuplicateItemsException if the collection has duplicate entries.
	 */
	public DefaultCodesTable(Collection<T> codesTableItems, Class<? 
			extends AbstractCodesTableItem> codesTableItemsClass, Comparator comparator)
			throws CodesTableEmptyException, DuplicateItemsException {
		init(codesTableItems, codesTableItemsClass, comparator);
	}

}
