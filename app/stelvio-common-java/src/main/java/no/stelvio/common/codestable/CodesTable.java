package no.stelvio.common.codestable;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.Predicate;

/**
 * Interface defining functionality for retrieving a codetable's item or an items decode,
 * in addition - for adding predicates to filer the items in the codestable.
 * 
 * @param <T> <code>CodesTableItem</code>'s or subclasses of <code>CodesTableItem</code> 
 * that the <code>CodesTable</code> can hold values of.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTable<T extends CodesTableItem> {
	
	/**
	 * Returns the list of CodesTableItem, the filtered list if predicates have been added.	 * 
	 * @return the list of code table items in the table.
	 */
	List<T> getItems();
	
	/**
	 * Returns the <code>CodesTableItem</code> in the <code>CodesTable</code> that matches the specified <code>code</code>.
	 * 
	 * @param code the item's code.
	 * @return The <code>CodesTableItem</code> or <code>null</code> if the code does not exist in the <code>CodesTable</code>.
	 * @throws ItemNotFoundException if the input code doesn't exist in the <code>CodesTable</code>.
	 */
	T getCodesTableItem(Object code) throws ItemNotFoundException;
	
	/**
	 * Add a predicate to the list of items in a <code>CodesTable</code>.
	 * 
	 * @param predicate the <code>Predicate</code> to filter the items in the <code>CodesTable</code> with.
	 */
	void addPredicate(Predicate predicate);

	/**
	 * Removes all of the predicates on the <code>CodesTable</code>. 
	 */
	void resetPredicates();
	
	/**
	 * Returns the decode for a code in a <code>CodesTableItem</code> belonging to a <code>CodesTable</code>.
	 * 
	 * @param code the items code.
	 * @return The decode
	 * @throws ItemNotFoundException if no <code>CodesTableItem</code> is found for the code
	 * @throws DecodeNotFoundException if the input code maps to a <code>CodesTableItem</code> without a code 
	 */
	String getDecode(Object code) throws ItemNotFoundException, DecodeNotFoundException;
	
	/**
	 * Returns the decode for a code in <code>CodesTableItem</code> with a specific locale. 
	 * If the locale isn't supported, the decode that supports the default locale is returned - 
	 * if it exists.
	 * 
	 * @param code the items code.
	 * @param locale the internationalization code.
	 * @return The decode
	 * @throws ItemNotFoundException if no <code>CodesTableItem</code> is found for the code
	 * @throws DecodeNotFoundException if the input code maps to a <code>CodesTableItem</code> without a code
	 */
	String getDecode(Object code, Locale locale) throws DecodeNotFoundException;

	/**
	 * Checks that the code exists in the codes table.
	 *
	 * @param code the code to check for existence in the codes table.
	 * @return true if the code exists, false otherwise.
	 */
	boolean validateCode(String code);
}