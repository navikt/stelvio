package no.stelvio.common.codestable.support;

import java.util.HashMap;
import java.util.HashMap;
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
public class DefaultCodesTable<T extends CodesTableItem> extends AbstractCodesTable<T> implements CodesTable<T> {
	private static final long serialVersionUID = -1882474781384872740L;

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
	public String getDecode(Enum code) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code.name());
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getDecode(Enum code, Locale locale) throws DecodeNotFoundException {
		return decode(code.name(), locale);
	}
		
	/**
	 * {@inheritDoc}
	 */
	public String getDecode(Object code) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code);
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
		return decode(code, locale);
	}
}
