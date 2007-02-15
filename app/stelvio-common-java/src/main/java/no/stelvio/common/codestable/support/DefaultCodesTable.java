package no.stelvio.common.codestable.support;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.Predicate;

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
public class DefaultCodesTable<T extends CodesTableItem> extends AbstractCodesTable<T> implements CodesTable {
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
		init(codesTableItems);
	}

	/** {@inheritDoc} */
	public List<T> getItems() {
		return super.getItems();
	}

	/** {@inheritDoc} */
	public T getCodesTableItem(Object code) throws ItemNotFoundException {
		return super.getCodesTableItem(code);
	}

	/** {@inheritDoc} */
	public void addPredicate(Predicate predicate) {
		super.addPredicate(predicate);
	}

	/** {@inheritDoc} */
	public void resetPredicates() {
		super.resetPredicates();
	}
	
	public CodesTable copy() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	public String getDecode(Object code) throws ItemNotFoundException, DecodeNotFoundException {
		return super.getDecode(code);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDecode(Object code, Locale locale) {
		return super.getDecode(code, locale);
	}

	/** {@inheritDoc} */
	public boolean validateCode(String code) {
		return super.validateCode(code);
	}
}