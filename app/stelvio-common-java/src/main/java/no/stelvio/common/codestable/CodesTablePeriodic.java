package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.collections.Predicate;

/**
 * Interface defining functionality for retrieving a codetableperiodic's item or an items decode,
 * in addition for adding predicates to filer the items in the codestable. 
 * A <code>CodesTablePeriodic</code> is a <code>CodesTable</code> that has a defined 
 * period of validity.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTablePeriodic<T extends CodesTableItemPeriodic> {
	
	/**
	 * Adds a <code>CodesTableItemPeriodic</code> to the <code>CodesTablePeriodic</code>.
	 * 
	 * @param codesTableItemPeriodic the item to add 
	 */
	public void addCodesTableItem(T codesTableItemPeriodic);
	
	/**
	 * Returns the <code>CodesTableItemPeriodic</code> in the specified <code>CodesTablePeriodic</code> that matches the specified <code>code</code>.
	 * 
	 * @param code the item's code
	 * @return The <code>CodesTableItemPeriodoc</code> or <code>null</code> if the code does not exist in the codestable.
	 */
	public T getCodesTableItem(Object code);
	
	/**
	 * Add a predicate to the list of items in a <code>CodesTablePeriodic<code>.
	 * 
	 * @param predicate the <code>Predicate</code> to filter the items in a <code>CodesTablePeriodic<code> with
	 */
	public void addPredicate(Predicate predicate);
	
	/** Removes all of the predicates on the <code>CodesTablePeriodic<code>. */
	public void resetPrediacte();
	
	/**
	 * Returns the decode for a code in a <code>CodesTableItempPriodic</code> belonging to a <code>CodesTablePeriodic</code>.
	 * 
	 * @param code the items code
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTablePeriodic</code>.
	 */
	public String getDecode(Object code, Date date);
	
	/**
	 * Returns the decode for a code in <code>CodesTableItemPeriodic</code> with a specific locale.
	 * @param code the items code
	 * @param locale the internationalization code.
	 * @return The decode or <code>null</code> if the code doesn't exist in the <code>CodesTablePeriodic</code>.
	 */
	public String getDecode(Object code, Locale locale, Date date);
}