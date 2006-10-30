package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

import no.stelvio.common.context.RequestContext;

/**
 * Implementation of CodesTablePeriodic for retrieving codes tables that has
 * defined a period of validity.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTablePeriodicImpl extends CodesTableImpl implements CodesTablePeriodic {

	/**
	 * Returns the decode for a code in a codes table.
	 * 
	 * @param code the item's code.
	 * @param date the date an item must be valid.
	 * @return the decode or null if the code does not exist in the codes table.
	 */
	public String getDecode(Object code, Date date) {
		//TODO hente ut riktig requestcontext - hvordan skal denne bygges opp?
		Locale locale = new Locale(RequestContext.getLocale().substring(0,1), RequestContext.getLocale().substring(3,4));
		
		return getDecode(code, locale, date);
	}
	
	/**
	 * Returns the decode code in specified codes table.
	 * 
	 * @param code the item's code.
	 * @param locale the local for an item.
	 * @param date the date an item must be valid.
	 * @return the decode or null if the code does not exist in the codes table.
	 */
	public String getDecode(Object code, Locale locale, Date date) {
		//If there are predicates added to the codestableitems in a codestable,
		//codesTableItem will only return an item if it belongs to the filtered collection
		//of codestableitems
		for(CodesTableItem cti : codesTableItems){
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) && cti.getFromDato().before(date) && cti.getToDato().after(date)){
				 return cti.getDecode();
			}
		}
		
		//TODO: COMMENT IN: throw new CodesTableException();

		return null;	
	}
}