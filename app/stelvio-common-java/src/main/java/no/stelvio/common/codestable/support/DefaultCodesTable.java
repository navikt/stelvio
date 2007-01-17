package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

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
	/** List of <code>CodesTableItem</code>s this <code>DefaultCodesTable</code> consists of. */
	private List<T> codesTableItems;
	
	/**
	 * Holds the filtered list of <code>CodesTableItem</code>s created by taking the full list and applying the
	 * predicates.
	 */
	private List<T> filteredCodesTableItems;

	/** List of predicates to use for filtering the list of <code>CodesTableItem</code>s */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	/**
	 * Creates a <code>DefaultCodesTable</code> with a list of <code>CodesTableItem</code>s.
	 *
	 * @param codesTableItems list of <code>CodesTableItem</code>s this <code>DefaultCodesTable</code> consists of. 
	 */
	public DefaultCodesTable(List<T> codesTableItems) {
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
								
		//There are no predicates for the items in the codestable  
		if (this.predicates.isEmpty()){
			for (T c : this.codesTableItems){
				if (c.getCode().equals(code.toString())){
					cti = c;
					break;
				}
			}
		} else { 
			for(T c : this.filteredCodesTableItems){
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
	 * {@inheritDoc}
	 */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered, or else the codestableitems in the filtered collection are filtered.
		synchronized (this.filteredCodesTableItems){
			if(this.predicates.isEmpty()){
				this.filteredCodesTableItems = (ArrayList<T>) CollectionUtils.select(this.codesTableItems, predicate);
			} else{
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
	public String getDecode(Object code) {		
		return getDecode(code, LocaleContextHolder.getLocale());
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getDecode(Object code, Locale locale) {
		String decode = null;
		String defaultDecode = null;

		// If there are predicates added to the codestableitems in a codestable,
		// getCodesTableItem() will only return an item if it belongs to the filtered collection
		// of codestableitems
		// TODO: optimize
		for(T cti : this.codesTableItems){
			if(cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(locale) ){
				 decode = cti.getDecode();
				 break;
			} else if (cti.equals(getCodesTableItem(code)) && cti.getLocale().equals(LocaleContextHolder.getLocale())){
				defaultDecode = cti.getDecode();
			}
		}
		
		// If there doesn't exist a decode for the input code and locale, use the
		// default decode of the input code, or throw an exception
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