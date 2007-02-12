package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

import org.apache.commons.collections.Predicate;

/**
 * Implementation of CodesTable used to handle a codestable, its belonging values and predicates for filtering the items
 * in the codestable.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @todo use Commons Collection's LazyMap to initialize the filtered codes table map
 * @todo add safe copying of input/output, that is, constructor and getItems()
 */
public class DefaultCodesTable<T extends CodesTableItem> implements CodesTable {
	
	/** Map containing Codes-CodesTableItem pairs*/
	private Map<Object, T> codeCodesTableItemMap;
	
	/**
	 * Holds the filtered map of <code>CodesTableItem</code>s created by taking the full list and applying the
	 * predicates.
	 */
	private Map<Object, T> filteredCodeCodesTableItemMap;	
	

	/** List of predicates to use for filtering the list of <code>CodesTableItem</code>s */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	/**
	 * Copy-constructor
	 * Returns a new DefaultCodesTable, based on the codestable used as input
	 * @param defaultCodesTable
	 */
	public DefaultCodesTable(DefaultCodesTable<T> defaultCodesTable){
		this.predicates = new ArrayList<Predicate>(defaultCodesTable.predicates);
		this.codeCodesTableItemMap = new HashMap<Object, T>(defaultCodesTable.codeCodesTableItemMap);
		this.filteredCodeCodesTableItemMap = new HashMap<Object, T>(defaultCodesTable.filteredCodeCodesTableItemMap);
	}
	
	
	/**
	 * Creates a <code>DefaultCodesTable</code> with a list of <code>CodesTableItem</code>s.
	 *
	 * @param codesTableItems list of <code>CodesTableItem</code>s this <code>DefaultCodesTable</code> consists of. 
	 */
	public DefaultCodesTable(List<T> codesTableItems) {	
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
	 * {@inheritDoc}
	 */
	public void addPredicate(Predicate predicate) {
		
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered, or else the codestableitems in the filtered map are filtered.
		synchronized (this.filteredCodeCodesTableItemMap){
			if(this.predicates.isEmpty()){
				this.filteredCodeCodesTableItemMap = new HashMap<Object, T>(codeCodesTableItemMap);
			}
			
			ArrayList<Object> keysFilteredOut = new ArrayList<Object>();
			
			//Loop through filter map and remove items that fails eval of filter rules
			for (Object code : filteredCodeCodesTableItemMap.keySet()) {
				//If CodesTableItem doesn't evaluate according to predicate, remove from filtered list
				if(!predicate.evaluate(filteredCodeCodesTableItemMap.get(code))){
					keysFilteredOut.add(code);
				}
			}
			for (Object filteredOutKey : keysFilteredOut) {
				filteredCodeCodesTableItemMap.remove(filteredOutKey);
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
	
	public CodesTable copy() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDecode(Object code) throws ItemNotFoundException, DecodeNotFoundException{		
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
	 * @Depricated CodesTables no longer exposes locale. Use {@link getDecode(Object)}
	 */
	@Deprecated
	public String getDecode(Object code, Locale locale) {
		//Locale no longer plays a part of CodesTable, call method witout locale
		return getDecode(code);
	}

}