package no.stelvio.common.codestable;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

import org.apache.commons.collections.Predicate;

/**
 * Interface defining functionality for retrieving a codetable's items or an item's decode. It is also possible to add
 * predicates to filter the items in the codestable.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @param <T>
 *            <code>AbstractCodesTableItem</code>'s or subclasses of <code>AbstractCodesTableItem</code> that the
 *            <code>CodesTable</code> can hold values of.
 * @param <K>
 *            an enum
 * @param <V>
 *            an object
 */
public interface CodesTable<T extends AbstractCodesTableItem<K, V>, K extends Enum, V> extends Serializable, Iterable<T> {
	/**
	 * Returns the set of codes table items which is filtered with the current list of predicates.
	 * 
	 * @return the set of code table items in the table.
	 */
	Set<T> getCodesTableItems();

	/**
	 * Returns the <code>CodesTableItem</code> in the <code>CodesTable</code> that corresponds to the specified
	 * <code>code</code>.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding <code>CodesTableItem</code>.
	 * @return The <code>CodesTableItem</code> that corresponds to the given code.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTableItem</code> does not exist in this <code>CodesTable</code>.
	 */
	T getCodesTableItem(K code) throws ItemNotFoundException;

	/**
	 * Returns the <code>CodesTableItem</code> in the <code>CodesTable</code> that corresponds to the specified
	 * <code>code</code>.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding <code>CodesTableItem</code>. This
	 *            must be one of this <code>CodesTable</code>'s corresponding <code>Enum</code>'s constants.
	 * @return The <code>CodesTableItem</code> that corresponds to the given code.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTableItem</code> does not exist in this <code>CodesTable</code> or
	 *             the code is not one of the corresponding <code>Enum</code>'s constants.
	 * @see #getCodesTableItem(Enum)
	 */
	T getCodesTableItem(String code) throws ItemNotFoundException;

	/**
	 * Add a predicate to the list of items in a <code>CodesTable</code>.
	 * 
	 * @param predicate
	 *            the <code>Predicate</code> to filter the items in the <code>CodesTable</code> with.
	 */
	void addPredicate(Predicate predicate);

	/** Removes all of the predicates on the <code>CodesTable</code>. */
	void resetPredicates();

	/**
	 * For a given code, returns the corresponding decode in this <code>CodesTable</code>.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding decode.
	 * @return The decode corresponding to the specified code, looked up from this <code>CodesTable</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTableItem</code> does not exist in this <code>CodesTable</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTableItem</code> without a decode.
	 */
	V getDecode(K code) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code and locale, returns the corresponding decode in this <code>CodesTable</code>. If the locale is not
	 * supported, the decode for the default locale is returned.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding decode.
	 * @param locale
	 *            the locale to find a decode for.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTable</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTableItem</code> does not exist in this <code>CodesTable</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTableItem</code> without a decode.
	 */
	V getDecode(K code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code, returns the corresponding decode in this <code>CodesTable</code>.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding decode. This must be one of this
	 *            <code>CodesTable</code>'s corresponding <code>Enum</code>'s constants.
	 * @return The decode corresponding to the specified code, looked up from this <code>CodesTable</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTableItem</code> does not exist in this <code>CodesTable</code> or
	 *             the code is not one of the corresponding <code>Enum</code>'s constants.
	 * @throws DecodeNotFoundException
	 *             if the input code maps to a <code>CodesTableItem</code> without a decode.
	 */
	V getDecode(String code) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code and locale, returns the corresponding decode in this <code>CodesTable</code>. If the locale is not
	 * supported, the decode for the default locale is returned.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding decode. This must be one of this
	 *            <code>CodesTable</code>'s corresponding <code>Enum</code>'s constants.
	 * @param locale
	 *            the locale to find a decode for.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTable</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTableItem</code> does not exist in this <code>CodesTable</code> or
	 *             the code is not one of the corresponding <code>Enum</code>'s constants.
	 * @throws DecodeNotFoundException
	 *             if the input code maps to a <code>CodesTableItem</code> without a decode.
	 */
	V getDecode(String code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * Checks that the code exists in this <code>CodesTable</code>.
	 * 
	 * @param code
	 *            the code to check for existence in this <code>CodesTable</code>.
	 * @return true if the code exists, false otherwise.
	 */
	boolean validateCode(Enum code);

	/**
	 * Checks that the code exists in this <code>CodesTable</code>.
	 * 
	 * @param code
	 *            the code to check for existence in this <code>CodesTable</code>.
	 * @return true if the code exists, false otherwise.
	 */
	boolean validateCode(String code);
}