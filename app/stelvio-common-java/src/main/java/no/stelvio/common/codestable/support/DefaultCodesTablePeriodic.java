package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

import org.apache.commons.collections.Predicate;

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
	
	/** Map containing Codes-CodesTableItemPeriodic pairs*/
	private Map<Object, T> codeCodesTableItemMap;
	
	/**
	 * Holds the filtered map of <code>CodesTableItemPeriodic</code>s created by taking the full list and applying the
	 * predicates.
	 */
	private Map<Object, T> filteredCodeCodesTableItemMap;		
	
	/** List of predicates to use for filtering the list of <code>CodesTableItemPeriodic</code>s */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	/**
	 * Creates a <code>DefaultCodesTablePeriodic</code> with a list of <code>CodesTableItemPeriodic</code>s.
	 *
	 * @param codesTableItems list of <code>CodesTableItemPeriodic</code>s this <code>DefaultCodesTablePeriodic</code> consists of.
	 */	                                                                                    
	public DefaultCodesTablePeriodic(List<T> codesTableItems) {
		this.codeCodesTableItemMap = new HashMap<Object, T>();
		if(codesTableItems != null){
			for (T codesTableItem : codesTableItems) {
				codeCodesTableItemMap.put(codesTableItem.getCode(), codesTableItem);
			}
		}
		this.filteredCodeCodesTableItemMap = new HashMap<Object, T>(codeCodesTableItemMap);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getItems() {
		if(predicates.isEmpty()){
			return new ArrayList<T>(codeCodesTableItemMap.values());
		}
		return new ArrayList<T>(filteredCodeCodesTableItemMap.values());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public T getCodesTableItem(Object code) throws ItemNotFoundException {
		T cti = null;
		
		//There are no predicates for the items in the codestable  
		if (this.predicates.isEmpty()){
			cti = this.codeCodesTableItemMap.get(code);
		} else { 
			cti = this.filteredCodeCodesTableItemMap.get(code);
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
	
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered, or else the codestableitems in the filtered map are filtered.
		synchronized (this.filteredCodeCodesTableItemMap){
			
			//List with items that should be removed from the filtered map
			ArrayList<Object> keysFilteredOut = new ArrayList<Object>();
			
			if(this.predicates.isEmpty()){
				this.filteredCodeCodesTableItemMap = new HashMap<Object, T>(codeCodesTableItemMap);
			}
			//Loop through filter map and remove items that fails eval of filter rules
			for (Object code : filteredCodeCodesTableItemMap.keySet()) {
				//If CodesTableItem doesn't evaluate according to predicate, remove from filtered list
				if(!predicate.evaluate(filteredCodeCodesTableItemMap.get(code))){
					keysFilteredOut.add(code);
				}
			}
			//Access to map is illegal while looping through it, remove items after previous loop concluded
			for (Object key : keysFilteredOut) {
				filteredCodeCodesTableItemMap.remove(key);
			}
		}		
		
		this.predicates.add(predicate);	
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetPredicates() {
		this.predicates.clear();
		//This is done in addPredicate if no predicates exists, 
		//doing it here anyway to avoid stale version of filteredCodeCodesTableItemMap
		this.filteredCodeCodesTableItemMap = new HashMap<Object, T>(codeCodesTableItemMap);
	}

	/**
	 * FIXME: This method doesn't use the input Date to retrieve decode. 
	 * FIXME: Method should probably change implementation or signature in the future.
	 * {@inheritDoc}
	 */
	public String getDecode(Object code, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		String decode = null;

		// If there are predicates added to the codestable,
		// getCodesTableItem() will only return an item if it belongs to the filtered collection
		// of codestableitems, otherwise it will throw an exception
		T codesTableItem = getCodesTableItem(code);
		decode = codesTableItem.getDecode();
		
		//If for some reason a code in the map maps to a null value, throw exception
		if(decode == null){
			throw new DecodeNotFoundException(code.toString());
		}
		
		return decode;
	}
	
	/** 
	 * {@inheritDoc}
	 * @Depricated CodesTables no longer exposes locale. Use {@link getDecode(Object, Date)}
	 */
	@Deprecated
	public String getDecode(Object code, Locale locale, Date date) throws DecodeNotFoundException {
		return getDecode(code, date);
    }
}