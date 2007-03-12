package no.stelvio.common.codestable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.Predicate;

/**
 * Interface defining functionality for retrieving a codetableperiodic's item or an items decode, in addition for adding
 * predicates to filer the items in the codestable. A <code>CodesTablePeriodic</code> is a <code>CodesTable</code> that
 * has a defined period of validity.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @param <T> <code>CodesTableItemPeriodic</code>'s or subclasses of <code>CodesTableItemPeriodic</code> that the
 * <code>CodesTablePeriodic</code> can hold values of.
 */
public interface CodesTablePeriodic<T extends CodesTableItemPeriodic> extends Serializable {
	
	/**
	 * Returns the set of CodesTableItemPeriodic, the filtered list if predicates have been added.
	 *
	 * @return the set of code table items in the table.
	 */
	Set<T> getCodeTableItems();

	/**
	 * Returns the list of CodesTableItemPeriodic, the filtered list if predicates have been added.
	 *
	 * @return the list of code table items in the table.
	 * @deprecated use {@link #getCodeTableItems()}
	 */
	List<T> getItems();

	/**
	 * Returns the <code>CodesTableItemPeriodic</code> in the specified <code>CodesTablePeriodic</code> that matches the
	 * specified <code>code</code>.
	 *
	 * @param code the item's code.
	 * @return The <code>CodesTableItemPeriodoc</code> or <code>null</code> if the code does not exist in the codestable.
	 * @throws ItemNotFoundException if the item doesn't exist in the <code>CodesTablePeriodic</code>.
	 */
	T getCodesTableItem(Enum code) throws ItemNotFoundException;

	/**
	 * Returns the <code>CodesTableItemPeriodic</code> in the specified <code>CodesTablePeriodic</code> that matches the
	 * specified <code>code</code>.
	 *
	 * @param code the item's code.
	 * @return The <code>CodesTableItemPeriodoc</code> or <code>null</code> if the code does not exist in the codestable.
	 * @throws ItemNotFoundException if the item doesn't exist in the <code>CodesTablePeriodic</code>.
	 * @deprecated use {@link #getCodesTableItem(Enum)}
	 */
	T getCodesTableItem(Object code) throws ItemNotFoundException;

	/**
	 * Add a predicate to the list of items in a <code>CodesTablePeriodic<code>.
	 *
	 * @param predicate the <code>Predicate</code> to filter the items in a <code>CodesTablePeriodic</code> with.
	 */
	void addPredicate(Predicate predicate);

	/** Removes all of the predicates on the <code>CodesTablePeriodic</code>. */
	void resetPredicates();

	/**
	 * Returns the decode for a code in a <code>CodesTableItempPriodic</code> belonging to a
	 * <code>CodesTablePeriodic</code>.
	 *
	 * @param code the items code.
	 * @param date the date the item must be valid.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTablePeriodic</code>.
	 * @throws ItemNotFoundException if no <code>CodesTableItemPeriodic</code> is found for the code
	 * @throws DecodeNotFoundException if the input code maps to a <code>CodesTableItemPeriodic</code> without a code
	 */
	String getDecode(Enum code, Date date) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * Returns the decode for a code in <code>CodesTableItemPeriodic</code> with a specific locale.
	 *
	 * @param code the items code.
	 * @param date the date the item must be valid.
	 * @param locale the internationalization code.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTablePeriodic</code>.
	 * @throws ItemNotFoundException if no <code>CodesTableItemPeriodic</code> is found for the code
	 * @throws DecodeNotFoundException if the input code maps to a <code>CodesTableItemPeriodic</code> without a code
	 */
	String getDecode(Enum code, Locale locale, Date date) throws DecodeNotFoundException;

	/**
	 * Returns the decode for a code in a <code>CodesTableItempPriodic</code> belonging to a
	 * <code>CodesTablePeriodic</code>.
	 *
	 * @param code the items code.
	 * @param date the date the item must be valid.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTablePeriodic</code>.
	 * @throws ItemNotFoundException if no <code>CodesTableItemPeriodic</code> is found for the code
	 * @throws DecodeNotFoundException if the input code maps to a <code>CodesTableItemPeriodic</code> without a code
	 * @deprecated use {@link #getDecode(Enum,Date)}
	 */
	String getDecode(Object code, Date date) throws ItemNotFoundException, DecodeNotFoundException;

	/**
	 * Returns the decode for a code in <code>CodesTableItemPeriodic</code> with a specific locale.
	 *
	 * @param code the items code.
	 * @param date the date the item must be valid.
	 * @param locale the internationalization code.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTablePeriodic</code>.
	 * @throws ItemNotFoundException if no <code>CodesTableItemPeriodic</code> is found for the code
	 * @throws DecodeNotFoundException if the input code maps to a <code>CodesTableItemPeriodic</code> without a code
	 * @deprecated use {@link #getDecode(Enum,Locale,Date)}
	 */
	String getDecode(Object code, Locale locale, Date date) throws DecodeNotFoundException;

	/**
	 * Checks that the code exists in the codes table.
	 *
	 * @param code the code to check for existence in the codes table.
	 * @return true if the code exists, false otherwise.
	 */
	boolean validateCode(Enum code);
}