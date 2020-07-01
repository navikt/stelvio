package no.stelvio.common.codestable;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * Interface defining functionality for retrieving a codetableperiodic's items or an item's decode. It is also possible to add
 * predicates to filter the items in the codestable. A <code>CodesTablePeriodic</code> is a <code>CodesTable</code> that has a
 * defined period of validity.
 * 
 * @version $Id$
 * @param <T>
 *            <code>AbstractCodesTablePeriodicItem</code>'s or subclasses of <code>CodesTablePeriodicItem</code> that the
 *            <code>CodesTablePeriodic</code> can hold values of.
 * @param <K>
 *            an enum type variable
 * @param <V>
 *            an type variable
 * @see no.stelvio.common.codestable.CodesTable
 */
public interface CodesTablePeriodic<T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> extends Serializable,
		Iterable<T> {

	/**
	 * Returns the code table item associated with the given code, in the date period in which the given date belongs to.
	 * 
	 * The parameter date (used to determine the period) time is set to 12:00:00. The fromDate time is set to 00:00:00 by
	 * default, and the toDate is set to 23:59:59.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>.
	 * @param date
	 *            the date determining which period to retrieve the <code>CodesTablePeriodicItem</code>.
	 * @return the <code>CodesTablePeriodicItem</code> that corresponds to the given code.
	 */
	T getCodesTableItem(K code, Date date);

	/**
	 * Returns the code table item associated with the given code, in the date period in which the given date belongs to.
	 * 
	 * The parameter date (used to determine the period) time is set to 12:00:00. The fromDate time is set to 00:00:00 by
	 * default, and the toDate is set to 23:59:59.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>.
	 * @param date
	 *            the date determining which period to retrieve the <code>CodesTablePeriodicItem</code>.
	 * @return the <code>CodesTablePeriodicItem</code> that corresponds to the given code.
	 */
	T getCodesTableItem(String code, Date date);

	/**
	 * Returns the set of code table items associated with the given code.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>s.
	 * @return the set of code table items associated with the given code.
	 */
	Set<T> getCodesTableItems(K code);

	/**
	 * Returns the set of code table items associated with the given code.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>s.
	 * @return the set of code table items associated with the given code.
	 */
	Set<T> getCodesTableItems(String code);

	/**
	 * Returns the set of codes table items which is filtered with the current list of predicates.
	 * 
	 * @return the set of code table items in the table.
	 */
	Set<T> getCodesTableItems();

	/**
	 * Returns the <code>CodesTablePeriodicItem</code> in the <code>CodesTablePeriodic</code> that corresponds to the specified
	 * <code>code</code>.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>.
	 * @return The <code>CodesTablePeriodicItem</code> that corresponds to the given code.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodicItem</code> does not exist in this
	 *             <code>CodesTablePeriodic</code>.
	 */
	T getCodesTableItem(K code) throws ItemNotFoundException;

	/**
	 * Returns the <code>CodesTablePeriodicItem</code> in the <code>CodesTablePeriodic</code> that corresponds to the specified
	 * <code>code</code>.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>. This
	 *            must be one of this <code>CodesTablePeriodic</code>'s corresponding <code>Enum</code>'s constants.
	 * @return The <code>CodesTablePeriodicItem</code> that corresponds to the given code.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodicItem</code> does not exist in this
	 *             <code>CodesTablePeriodic</code> or the code is not one of the corresponding <code>Enum</code>'s constants.
	 * @see #getCodesTableItem(Enum)
	 */
	T getCodesTableItem(String code) throws ItemNotFoundException;

	/**
	 * Add a predicate to the list of items in a <code>CodesTablePeriodic</code>.
	 * 
	 * @param predicate
	 *            the <code>Predicate</code> to filter the items in a <code>CodesTablePeriodic</code> with.
	 */
	void addPredicate(Predicate<T> predicate);

	/** Removes all of the predicates on the <code>CodesTablePeriodic</code>. */
	void resetPredicates();

	/**
	 * For a given code, returns the corresponding decode in this <code>CodesTablePeriodoc</code> that is valid for today's
	 * date.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding decode.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(K code) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code and locale, returns the corresponding decode in this <code>CodesTablePeriodoc</code> that is valid for
	 * today's date.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding decode.
	 * @param locale
	 *            the locale to find a decode for.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(K code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code, returns the corresponding decode in this <code>CodesTablePeriodoc</code> that is valid for today's
	 * date.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding decode. This must be one of this
	 *            <code>CodesTablePeriodoc</code>'s corresponding <code>Enum</code>'s constants.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(String code) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code and locale, returns the corresponding decode in this <code>CodesTablePeriodoc</code> that is valid for
	 * today's date.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding decode. This must be one of this
	 *            <code>CodesTablePeriodoc</code>'s corresponding <code>Enum</code>'s constants.
	 * @param locale
	 *            the locale to find a decode for.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(String code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code and date, returns the corresponding decode in this <code>CodesTablePeriodoc</code>.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding decode.
	 * @param date
	 *            the date the item must be valid.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(K code, Date date) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code, locale and date, returns the corresponding decode in this <code>CodesTablePeriodoc</code>.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding decode.
	 * @param date
	 *            the date the decode must be valid for.
	 * @param locale
	 *            the locale to find a decode for.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(K code, Locale locale, Date date) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code and date, returns the corresponding decode in this <code>CodesTablePeriodoc</code>.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding decode. This must be one of this
	 *            <code>CodesTablePeriodoc</code>'s corresponding <code>Enum</code>'s constants.
	 * @param date
	 *            the date the item must be valid.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(String code, Date date) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * For a given code, locale and date, returns the corresponding decode in this <code>CodesTablePeriodoc</code>.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding decode. This must be one of this
	 *            <code>CodesTablePeriodoc</code>'s corresponding <code>Enum</code>'s constants.
	 * @param date
	 *            the date the decode must be valid for.
	 * @param locale
	 *            the locale to find a decode for.
	 * @return The decode corresponding to the specified keys, looked up from this <code>CodesTablePeriodoc</code>.
	 * @throws ItemNotFoundException
	 *             if the code's corresponding <code>CodesTablePeriodocItem</code> does not exist in this
	 *             <code>CodesTablePeriodoc</code>.
	 * @throws DecodeNotFoundException
	 *             if the code maps to a <code>CodesTablePeriodicItem</code> without a decode.
	 */
	V getDecode(String code, Locale locale, Date date) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * Checks that the code exists in the codes table.
	 * 
	 * @param code
	 *            the code to check for existence in the codes table.
	 * @return true if the code exists, false otherwise.
	 */
	boolean validateCode(Enum code);

	/**
	 * Checks that the code exists in the codes table.
	 * 
	 * @param code
	 *            the code to check for existence in the codes table.
	 * @return true if the code exists, false otherwise.
	 */
	boolean validateCode(String code);

	/**
	 * Method used to get a set of {@link AbstractCodesTableItem} valid today.
	 * 
	 * @return Set of {@link AbstractCodesTablePeriodicItem} valid today
	 */
	Set<T> getCodesTableItemsValidToday();

	/**
	 * Method used to get a set of {@link AbstractCodesTableItem} valid for a specified date.
	 * 
	 * @param date
	 *            the date
	 * @return Set of {@link AbstractCodesTablePeriodicItem} valid for specified date
	 */
	Set<T> getCodesTableItemsValidForDate(Date date);
}
