package no.stelvio.common.codestable;

import java.util.Locale;

import org.apache.commons.collections.Predicate;

//TODO: un-comment exception-handling

/**
 * Interface defining functionality for retrieving a codetable's item or an items decode,
 * in addition for adding predicates to filer the items in the codestable.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTable {
	
	public CodesTableItem getCodesTableItem(Object code) ;
	public void addPredicate(Predicate predicate);
	public void resetPrediacte();
	public String getDecode(Object code);
	public String getDecode(Object code, Locale locale) ;
	public void setCodesTableItem(CodesTableItem codesTableItem);

}
