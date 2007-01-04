package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.context.RequestContext;

/**
 * Implementation of CodesTable used to handle a codestable, its beloning values and add
 * predicates to filter the items in the codestable. 
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @later use Commons Collection's LazyMap to initialize the filtered codes table map
 */
public class DefaultCodesTable<T extends CodesTableItem> implements CodesTable {

	//List of codestableitems
	private List<CodesTableItem> codesTableItems = new ArrayList<CodesTableItem>();
	
	//List of filtered codestableitems
	private List<CodesTableItem> filteredCodesTableItems = new ArrayList<CodesTableItem>();
	
	//List of predicates added to the codestableitems
	private List<Predicate> predicates = new ArrayList<Predicate>();
		
	/**
	 * {@inheritDoc}
	 */
	public void addCodesTableItem(CodesTableItem codesTableItem){
		this.codesTableItems.add(codesTableItem);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public CodesTableItem getCodesTableItem(Object code) {
		
		CodesTableItem cti = null;
								
		//There are no predicates for the items in the codestable  
		if(this.predicates.isEmpty()){
			for(CodesTableItem c : this.codesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
					break;
				}
			}
		} else if(!this.predicates.isEmpty()){ 
			for(CodesTableItem c : this.filteredCodesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
					break;
				}
			}
		}
			
		return cti;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered, or else the codestableitems in the filtered collection are filtered.
		synchronized (this.filteredCodesTableItems){
			if(this.predicates.isEmpty()){
				this.filteredCodesTableItems = (ArrayList<CodesTableItem>) CollectionUtils.select(this.codesTableItems, predicate);
			} else{
				this.filteredCodesTableItems = (ArrayList<CodesTableItem>) 
						CollectionUtils.select(this.filteredCodesTableItems, predicate);
			}
		}
		
		this.predicates.add(predicate);	
	}
	
	/** 
	 * {@inheritDoc}
	 * @deprecated Use {@link #resetPredicates()} instead
	 */
	public void resetPrediacte() {
		resetPredicates();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void resetPredicates() {
		this.predicates.clear();
		this.filteredCodesTableItems.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDecode(Object code) {		
		return getDecode(code, RequestContext.getLocale());
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getDecode(Object code, Locale locale) {
		
		String decode = null;
		String defaultDecode = null;

		//If there are predicates added to the codestableitems in a codestable,
		//getCodesTableItem() will only return an item if it belongs to the filtered collection
		//of codestableitems
		//TODO: optimize
		for(CodesTableItem cti : this.codesTableItems){			
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) ){
				 decode = cti.getDecode();
				 break;
			} else if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(RequestContext.getLocale())){
				defaultDecode = cti.getDecode();
			}
		}
		
		//If there doesn't exist a decode for the input code and locale, use the 
		//default decode of the input code, or throw an exception
		if(decode == null){
			if(defaultDecode != null){
				return defaultDecode;
			} else{
				throw new DecodeNotFoundException(code.toString());
			}
		}
		
		return decode;
	}
}