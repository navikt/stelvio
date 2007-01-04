package no.stelvio.common.codestable.support;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.context.RequestContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of CodesTablePeriodic for retrieving <code>CodesTable</code>s that has
 * defined a period of validity.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTablePeriodic<T extends CodesTableItem> implements CodesTablePeriodic {

	//List of codestableitemperiodics 
	private List<CodesTableItemPeriodic> codesTableItems = new ArrayList<CodesTableItemPeriodic>();
	
	//List of filtered codestableitems
	private List<CodesTableItemPeriodic> filteredCodesTableItems = new ArrayList<CodesTableItemPeriodic>();
	
	//List of predicates added to the codestableitems
	private List<Predicate> predicates = new ArrayList<Predicate>();
		
	/**
	 * {@inheritDoc}
	 */
	public void addCodesTableItem(CodesTableItemPeriodic codesTableItemPeriodic){
		this.codesTableItems.add(codesTableItemPeriodic);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public CodesTableItemPeriodic getCodesTableItem(Object code) {
		
		CodesTableItemPeriodic cti = null;
								
		//There are no predicates for the items of the codestableperiodic  
		if(this.predicates.isEmpty()){
			for(CodesTableItemPeriodic c : this.codesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
					break;
				}
			}
		} else { 
			for(CodesTableItemPeriodic c : this.filteredCodesTableItems){
				if (c.getCode() == code.toString()){
					cti = c;
					break;
				}
			}
		}
				
		return cti;
	}
		
	/**
	 * {@inheritDoc)}
	 */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestableperiodic, 
		//all of the codetableitemperiodics in a codetableperiodic
		//are filtered, or else the codestableitemperiodics in the filtered 
		//collection are filtered.
		synchronized (this.filteredCodesTableItems){
			if(this.predicates.isEmpty()){
				this.filteredCodesTableItems = (ArrayList<CodesTableItemPeriodic>) 
					CollectionUtils.select(this.codesTableItems, predicate);
			} else{
				this.filteredCodesTableItems = (ArrayList<CodesTableItemPeriodic>) 
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
	public String getDecode(Object code, Date date){
		return getDecode(code, RequestContext.getLocale(), date);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getDecode(Object code, Locale locale, Date date) {
		
		String decode = null;
		String defaultDecode = null;
		
		//If there are predicates added to the codestableitems in a codestable,
		//codesTableItem will only return an item if it belongs to the filtered collection
		//of codestableitems
		//TODO: optimize loop
		for(CodesTableItemPeriodic cti : codesTableItems){
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) 
					&& cti.getFromDate().before(date) && cti.getToDate().after(date)){ 
				decode = cti.getDecode();
				break;
			} else if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(RequestContext.getLocale()) 
					&& cti.getFromDate().before(date) && cti.getToDate().after(date)){
				defaultDecode = cti.getDecode();
			}
		}
		
		//If there doesn't exist a decode for the input code, date and locale, use the 
		//default decode of the input code and date, or throw an exception
		if(decode == null){
			if(defaultDecode != null){
				return defaultDecode;
			}
			else{
				throw new DecodeNotFoundException(code.toString());
			}
		}
		
		return decode;
    }
}