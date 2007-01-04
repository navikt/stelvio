package no.stelvio.common.codestable;

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
	 * Adds a <code>CodesTableItem</code> to the <code>CodesTable</code>.
	 * 
	 * @param codesTableItem the item to add.
     * @todo LATER: remove this and use constructor in impl instead. 
	 */
	void addCodesTableItem(T codesTableItem);
	
	/**
	 * Returns the <code>CodesTableItem</code> in the <code>CodesTable</code> that matches the specified <code>code</code>.
	 * 
	 * @param code the item's code.
	 * @return The <code>CodesTableItem</code> or <code>null</code> if the code does not exist in the <code>CodesTable</code>.
	 */
	T getCodesTableItem(Object code);
	
	/**
	 * Add a predicate to the list of items in a <code>CodesTable</code>.
	 * 
	 * @param predicate the <code>Predicate</code> to filter the items in the <code>CodesTable</code> with.
	 */
	void addPredicate(Predicate predicate);
	
	/** 
	 * Removes all of the predicates on the <code>CodesTable</code>. 
	 * @deprecated Use {@link #resetPredicates()} instead
	 */
	void resetPrediacte();

	/** 
	 * Removes all of the predicates on the <code>CodesTable</code>. 
	 */
	void resetPredicates();
	
	/**
	 * Returns the decode for a code in a <code>CodesTableItem</code> belonging to a <code>CodesTable</code>.
	 * 
	 * @param code the items code.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTable</code>.
	 */
	String getDecode(Object code);
	
	/**
	 * Returns the decode for a code in <code>CodesTableItem</code> with a specific locale. 
	 * If the locale isn't supported, the decode that supports the default locale is returned - 
	 * if it exists.
	 * 
	 * @param code the items code.
	 * @param locale the internationalization code.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTable<code>.
	 * @throws DecodeNotFoundException thrown if the input code doesn't exist in the <code>CodesTable</code>.
	 */
	String getDecode(Object code, Locale locale) throws DecodeNotFoundException;
}