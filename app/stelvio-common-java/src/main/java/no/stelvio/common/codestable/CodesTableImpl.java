package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import no.stelvio.common.context.RequestContext;

/**
 * Implementation of CodesTable used to handle a codestable, its beloning values and add
 * predicates to filter the items in the codestable. 
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableImpl implements CodesTable {

	//List of codestableitems
	private List<CodesTableItem> codesTableItems = new ArrayList<CodesTableItem>();
	
	//List of filtered codestableitems
	private List<CodesTableItem> filteredCodesTableItems = new ArrayList<CodesTableItem>();
	
	//List of predicates added to the codestableitems
	private List<Predicate> predicates = new ArrayList<Predicate>();
		
	/**
	 * {@inheritDoc CodesTable#addCodesTableItem(CodesTableItem codesTableItem)}
	 */
	public void addCodesTableItem(CodesTableItem codesTableItem){
		this.codesTableItems.add(codesTableItem);
	}
	
	/** 
	 * {@inheritDoc CodesTable#getCodesTableItem()}
	 */
	public CodesTableItem getCodesTableItem(Object code) {
		
		CodesTableItem cti = null;
								
		//There are no predicates for the items in the codestable  
		if(this.predicates.isEmpty()){
			for(CodesTableItem c : this.codesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
				}
			}
		}
		//There are defined predicates for the items in the codestable
		else if(!this.predicates.isEmpty()){ 
			for(CodesTableItem c : this.filteredCodesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
				}
			}
		}
			
		return cti;
	}
		
	/**
	 * {@inheritDoc CodesTable#addPredicate()}
	 */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered
		synchronized (this.filteredCodesTableItems){
			if(this.predicates.isEmpty()){
				this.filteredCodesTableItems = (ArrayList<CodesTableItem>) CollectionUtils.select(this.codesTableItems, predicate);
			}
			//If there are previous predicates added to a codestable, the codetableitems in the filtered collection 
			//are filtered
			else{
				this.filteredCodesTableItems = (ArrayList<CodesTableItem>) CollectionUtils.select(this.filteredCodesTableItems, predicate);
			}
		}
		
		this.predicates.add(predicate);	
	}
	
	/** 
	 * {@inheritDoc CodesTable#resetPredicate()}
	 */
	public void resetPrediacte() {
		this.predicates.clear();
		this.filteredCodesTableItems.clear();
	}

	/**
	 * {@inheritDoc CodesTable#getDecode()}
	 */
	public String getDecode(Object code) {		
		return getDecode(code, RequestContext.getLocale());
	}

	/** 
	 * {@inheritDoc CodesTable#getDecode()}
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
		
		return null;
	}
}