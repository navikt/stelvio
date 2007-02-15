package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
class AbstractCodesTable<T extends AbstractCodesTableItem> {
	/** Map containing Codes-CodesTableItemPeriodic pairs*/
	private Map<Object, T> codeCodesTableItemMap;
	/**
	 * Holds the filtered map of <code>CodesTableItemPeriodic</code>s created by taking the full list and applying the
	 * predicates.
	 */
	private Map<Object, T> filteredCodeCodesTableItemMap;
	/** List of predicates to use for filtering the list of <code>CodesTableItemPeriodic</code>s */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	protected void init(List<T> codesTableItems) {
		codeCodesTableItemMap = new HashMap<Object, T>();

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
		return new ArrayList<T>(filteredCodeCodesTableItemMap.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public T getCodesTableItem(Object code) throws ItemNotFoundException {
		T cti = findCodesTableItem(code);

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
		synchronized (filteredCodeCodesTableItemMap){
			if(predicates.isEmpty()){
				filteredCodeCodesTableItemMap = new HashMap<Object, T>(codeCodesTableItemMap);
			}

			predicates.add(predicate);
			ArrayList<Object> keysFilteredOut = new ArrayList<Object>();

			//Loop through filter map and remove items that fails eval of filter rules
			for (Object code : filteredCodeCodesTableItemMap.keySet()) {
				//If CodesTableItem doesn't evaluate according to predicate, remove from filtered list
				if (!predicate.evaluate(filteredCodeCodesTableItemMap.get(code))) {
					keysFilteredOut.add(code);
				}
			}
			
			for (Object filteredOutKey : keysFilteredOut) {
				filteredCodeCodesTableItemMap.remove(filteredOutKey);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetPredicates() {
		predicates.clear();
		//This is done in addPredicate if no predicates exists,
		//doing it here anyway to avoid stale version of filteredCodeCodesTableItemMap
		filteredCodeCodesTableItemMap = new HashMap<Object, T>(codeCodesTableItemMap);
	}

	/**
	 * FIXME: This method doesn't use the input Date to retrieve decode.
	 * FIXME: Method should probably change implementation or signature in the future.
	 * {@inheritDoc}
	 */
	protected String getDecode(Object code, Date... date) throws ItemNotFoundException, DecodeNotFoundException {
		// If there are predicates added to the codestable,
		// getCodesTableItem() will only return an item if it belongs to the filtered collection
		// of codestableitems, otherwise it will throw an exception
		T codesTableItem = getCodesTableItem(code);
		String decode = codesTableItem.getDecode();

		// If for some reason a code in the map maps to a null value, throw exception
		if(decode == null){
			throw new DecodeNotFoundException(code.toString());
		}

		return decode;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getDecode(Object code, Locale locale, Date... date) throws DecodeNotFoundException {
		return getDecode(code, date);
    }

	/** {@inheritDoc} */
	public boolean validateCode(String code) {
		T codesTableItem = findCodesTableItem(code);

		return codesTableItem != null;
	}

	/**
	 * Makes a <code>String</code> representation of the codes table.
	 *
	 * @return a <code>String</code> representation of the codes table.
	 */
	public String toString() {
		return new ToStringBuilder(this).
				append("filteredCodeCodesTableItemMap", filteredCodeCodesTableItemMap).toString();
	}

	/**
	 * Finds the codes table item.
	 *
	 * @param code the code used to find the codes table item.
	 * @return the found codes table item or null if not found.
	 */
	private T findCodesTableItem(Object code) {
		return filteredCodeCodesTableItemMap.get(code);
	}
}
