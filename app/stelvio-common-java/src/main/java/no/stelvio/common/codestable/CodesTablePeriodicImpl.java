package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import no.stelvio.common.context.RequestContext;

/**
 * Implementation of CodesTablePeriodic for retrieving <code>CodesTable</code>s that has
 * defined a period of validity.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTablePeriodicImpl implements CodesTablePeriodic {

	//List of codestableitemperiodics 
	private List<CodesTableItemPeriodic> codesTableItems = new ArrayList<CodesTableItemPeriodic>();
	
	//List of filtered codestableitems
	private List<CodesTableItemPeriodic> filteredCodesTableItems = new ArrayList<CodesTableItemPeriodic>();
	
	//List of predicates added to the codestableitems
	private List<Predicate> predicates = new ArrayList<Predicate>();
		
	/**
	 * {@inheritDoc CacheTablePeriodic#addCodesTableItem()}
	 */
	public void addCodesTableItem(CodesTableItemPeriodic codesTableItemPeriodic){
		this.codesTableItems.add(codesTableItemPeriodic);
	}
	
	/** 
	 * {@inheritDoc CacheTablePeriodic#getCodesTableItem(Object)}
	 */
	public CodesTableItemPeriodic getCodesTableItem(Object code) {
		
		CodesTableItemPeriodic cti = null;
								
		//There are no predicates for the items of the codestableperiodic  
		if(this.predicates.isEmpty()){
			for(CodesTableItemPeriodic c : this.codesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
				}
			}
		}
		//There are defined predicates for the items of the codestableperiodic
		else if(!this.predicates.isEmpty()){ 
			for(CodesTableItemPeriodic c : this.filteredCodesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
				}
			}
		}
				
		return cti;
	}
		
	/**
	 * {@inheritDoc CodesTablePeriodic#addPredicate()}
	 */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestableperiodic, all of the codetableitemperiodics in a codetableperiodic
		//are filtered
		synchronized (this.filteredCodesTableItems){
			if(this.predicates.isEmpty()){
				this.filteredCodesTableItems = (ArrayList<CodesTableItemPeriodic>) CollectionUtils.select(this.codesTableItems, predicate);
			}
			//If there are previous predicates added to a codestableperiodic, the codetableitemperiodics in the filtered collection 
			//are filtered
			else{
				this.filteredCodesTableItems = (ArrayList<CodesTableItemPeriodic>) CollectionUtils.select(this.filteredCodesTableItems, predicate);
			}
		}
		
		this.predicates.add(predicate);	
	}
	
	/** 
	 * {@inheritDoc CodesTablePeriodic#resetPredicate()}
	 */
	public void resetPrediacte() {
		this.predicates.clear();
		this.filteredCodesTableItems.clear();
	}

	/**
	 * {@inheritDoc CodesTablePeriodic#getDecode()}
	 */
	public String getDecode(Object code, Date date){
		return getDecode(code, RequestContext.getLocale(), date);
	}
	
	/** 
	 * {@inheritDoc CodesTablePeriodic#getDecode()}
	 */
	public String getDecode(Object code, Locale locale, Date date) {
		//If there are predicates added to the codestableitems in a codestable,
		//codesTableItem will only return an item if it belongs to the filtered collection
		//of codestableitems
		for(CodesTableItemPeriodic cti : codesTableItems){
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) && cti.getFromDate().before(date) && cti.getToDate().after(date)){
				 return cti.getDecode();
			}
		}

		return null;
    }
}