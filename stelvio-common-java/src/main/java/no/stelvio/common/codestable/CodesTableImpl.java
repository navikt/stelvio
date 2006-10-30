package no.stelvio.common.codestable;

import java.util.Locale;
import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.codestable.CodesTable;

/**
 * Implementation of CodesTable used to handle a codestable, its beloning values and add
 * predicates to filter the items in the codestable. 
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableImpl implements CodesTable {

	/**
	 * List of codestableitems in a codestable.
	 */
	protected ArrayList<CodesTableItem> codesTableItems = null;
	
	//List of filtered codestableitems
	private ArrayList<CodesTableItem> filteredCodesTableItems = null;
	
	//List of predicates added to the codestableitems
	private ArrayList<Predicate> predicates = null;
		
	/** 
	 * Returns the item in the specified codes table that matches the specified code.
	 * 
	 * @param code the item's code.
	 * @return the codes table item or null if the code does not exist in the codes table.
	 * @throws CodesTableException 
	 */
	public CodesTableItem getCodesTableItem(Object code) {
		
		CodesTableItem cti = null;
								
		//There are no predicates for the items of the codestable  
		if(this.predicates == null){
			for(CodesTableItem c : this.codesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
				}
			}
		}
		//There are defined predicates for the items of the codestable
		else if(this.predicates != null){ 
			for(CodesTableItem c : this.filteredCodesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
				}
			}
		}
		
		if(cti == null){
			//TODO: comment in exception
			//throw new CodesTableException();
		}
		
		return cti;
	}
		
	/**
	 * Add a predicate on the list of items in a codes table.
	 * 
	 * @param predicate the predicate to filter the items in a codes table.
	 */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered
		if(predicates == null){
			this.filteredCodesTableItems = (ArrayList<CodesTableItem>) CollectionUtils.select(this.codesTableItems, predicate);
		}
		//If there are previous predicates added to a codestable, the codetableitems in the filtered collection 
		//are filtered
		else{
			this.filteredCodesTableItems = (ArrayList<CodesTableItem>) CollectionUtils.select(this.filteredCodesTableItems, predicate);
		}
		
		this.predicates.add(predicate);	
	}

	/**
	 * Returns the decode code in specified codes table.
	 * 
	 * @param code the item's code.
	 * @return decode or null if the code does not exist in the codes table.
	 */
	public String getDecode(Object code) {
		//TODO hente ut riktig requestcontext - hvordan skal denne bygges opp?
		Locale locale = new Locale(RequestContext.getLocale().substring(0,1), RequestContext.getLocale().substring(3,4));
		
		return getDecode(code, locale);
	}

	/** 
	 * Returns the decode code in specified codes table.
	 * 
	 * @param code the item's code.
	 * @param locale the internationalization code.
	 * @return decode or null if the code does not exist in the codes table.
	 * @throws CodesTableException 
	 */
	public String getDecode(Object code, Locale locale) {
			
		//If there are predicates added to the codestableitems in a codestable,
		//getCodesTableItem() will only return an item if it belongs to the filtered collection
		//of codestableitems
		for(CodesTableItem cti : this.codesTableItems){
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) ){
				 return cti.getDecode();
			}
		}
		
		//TODO: COMMENT IN: throw new CodesTableException();

		return null;	
	}

	/** 
	 * Removes all of the predicates on a CodesTable/CodesTablePeriodic.
	 */
	public void resetPrediacte() {
		this.predicates.clear();
		this.filteredCodesTableItems.clear();
	}
}