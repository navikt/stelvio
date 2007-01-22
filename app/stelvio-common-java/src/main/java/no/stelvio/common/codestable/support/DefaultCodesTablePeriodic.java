package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

/**
 * Implementation of CodesTablePeriodic for retrieving <code>CodesTable</code>s that has defined a period of validity.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @todo add safe copying of input/output, that is, constructor and getItems()
 * @todo should this check that the period is not overlapping for "like" rows? -> just an equal/hashcode impl that
 * throws exception when 2 rows have wrapping periods, hashcode uses code/date_from/is_approved, equals checks for overlapping  
 */
public class DefaultCodesTablePeriodic<T extends CodesTableItemPeriodic> implements CodesTablePeriodic {
	/** List of <code>CodesTableItemPeriodic</code>s this <code>DefaultCodesTablePeriodic</code> consists of. */
	private List<T> codesTableItems;
	
	/**
	 * Holds the filtered list of <code>CodesTableItemPeriodic</code>s created by taking the full list and applying the
	 * predicates.
	 */
	private List<T> filteredCodesTableItems;
	
	/** List of predicates to use for filtering the list of <code>CodesTableItemPeriodic</code>s */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	/**
	 * Creates a <code>DefaultCodesTablePeriodic</code> with a list of <code>CodesTableItemPeriodic</code>s.
	 *
	 * @param codesTableItems list of <code>CodesTableItemPeriodic</code>s this <code>DefaultCodesTablePeriodic</code> consists of.
	 */	                                                                                    
	public DefaultCodesTablePeriodic(List<T> codesTableItems) {
		this.codesTableItems = codesTableItems;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getItems() {
		return codesTableItems;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public T getCodesTableItem(Object code) throws ItemNotFoundException {
		T cti = null;
								
		// There are no predicates for the items of the codestableperiodic
		if (this.predicates.isEmpty()){
			for (T c : this.codesTableItems) {
				if (c.getCode().equals(code.toString())) {
					cti = c;
					break;
				}
			}
		} else { 
			for (T c : this.filteredCodesTableItems){
				if (c.getCode().equals(code.toString())){
					cti = c;
					break;
				}
			}
		}

		if (cti == null) {
			throw new ItemNotFoundException(code);
		}
				
		return cti;
	}
		
	/**
	 * {@inheritDoc)}
	 */
	public void addPredicate(Predicate predicate) {
		// If there are no previous predicates added to a codestableperiodic,
		// all of the codetableitemperiodics in a codetableperiodic
		// are filtered, or else the codestableitemperiodics in the filtered
		// collection are filtered.
		synchronized (this.filteredCodesTableItems){
			if (this.predicates.isEmpty()){
				this.filteredCodesTableItems = (ArrayList<T>)
					CollectionUtils.select(this.codesTableItems, predicate);
			} else {
				this.filteredCodesTableItems = (ArrayList<T>)
					CollectionUtils.select(this.filteredCodesTableItems, predicate);
			}
		}
		
		this.predicates.add(predicate);	
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
	public String getDecode(Object code, Date date) throws DecodeNotFoundException {
		return getDecode(code, LocaleContextHolder.getLocale(), date);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getDecode(Object code, Locale locale, Date date) throws DecodeNotFoundException {
		String decode = null;
		String defaultDecode = null;
		
		//If there are predicates added to the codestableitems in a codestable,
		//codesTableItem will only return an item if it belongs to the filtered collection
		//of codestableitems
		//TODO: optimize loop
		for(T cti : codesTableItems){
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) 
					&& cti.getFromDate().before(date) && cti.getToDate().after(date)){ 
				decode = cti.getDecode();
				break;
			} else if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(LocaleContextHolder.getLocale())
					&& cti.getFromDate().before(date) && cti.getToDate().after(date)){
				defaultDecode = cti.getDecode();
			}
		}
		
		// If there doesn't exist a decode for the input code, date and locale, use the
		// default decode of the input code and date, or throw an exception
		if (decode == null){
			if (defaultDecode != null){
				return defaultDecode;
			} else {
				throw new DecodeNotFoundException(code.toString());
			}
		}
		
		return decode;
    }
}