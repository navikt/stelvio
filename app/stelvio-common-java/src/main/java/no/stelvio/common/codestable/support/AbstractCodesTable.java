package no.stelvio.common.codestable.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

import no.stelvio.common.codestable.CodesTableEmptyException;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

/**
 * @todo write javadoc
 * @author personf8e9850ed756, Accenture
 */
abstract class AbstractCodesTable<T extends AbstractCodesTableItem<K, V>, K extends Enum, V> implements Serializable {
	private static final long serialVersionUID = -4072918237022568146L;

	/** Map containing Codes-AbstractCodesTableItem pairs */
	private Map<K, T> codeCodesTableItemMap;
	/**
	 * Holds the filtered map of <code>CodesTableItemPeriodic</code>s created by taking the full list and applying the
	 * predicates.
	 */
	private Map<K, T> filteredCodeCodesTableItemMap;
	/** List of predicates to use for filtering the list of <code>CodesTableItemPeriodic</code>s */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	protected void init(List<T> codesTableItems) {
		if (CollectionUtils.isEmpty(codesTableItems)) {
			throw new CodesTableEmptyException(codesTableItems.getClass().getTypeParameters()[0].getName());
		}

		codeCodesTableItemMap = new HashMap<K, T>(codesTableItems.size());

		for (T codesTableItem : codesTableItems) {
			codeCodesTableItemMap.put(codesTableItem.getCode(), codesTableItem);
		}

		this.filteredCodeCodesTableItemMap = new HashMap<K, T>(codeCodesTableItemMap);
	}

	/** {@inheritDoc} */
	public Set<T> getCodeTableItems() {
		return new HashSet<T>(filteredCodeCodesTableItemMap.values());
	}

	/** {@inheritDoc} */
	public List<T> getItems() {
		return new ArrayList<T>(getCodeTableItems());
	}

	/** {@inheritDoc} */
	public T getCodesTableItem(K code) throws ItemNotFoundException {
		T cti = findCodesTableItem(code.name());

		if (cti == null) {
			throw new ItemNotFoundException(code);
		}

		return cti;
	}

	/** {@inheritDoc} */
	public T getCodesTableItem(Object code) throws ItemNotFoundException {
		T cti = findCodesTableItem(code);

		if (cti == null) {
			throw new ItemNotFoundException(code);
		}

		return cti;
	}

	/** {@inheritDoc)} */
	public void addPredicate(Predicate predicate) {
		//If there are no previous predicates added to a codestable, all of the codetableitems in a codetable
		//are filtered, or else the codestableitems in the filtered map are filtered.
		synchronized (filteredCodeCodesTableItemMap) {
			if (predicates.isEmpty()) {
				filteredCodeCodesTableItemMap = new HashMap<K, T>(codeCodesTableItemMap);
			}

			predicates.add(predicate);
			List<K> keysFilteredOut = new ArrayList<K>();

			//Loop through filter map and remove items that fails eval of filter rules
			for (K code : filteredCodeCodesTableItemMap.keySet()) {
				//If CodesTableItem doesn't evaluate according to predicate, remove from filtered list
				if (!predicate.evaluate(filteredCodeCodesTableItemMap.get(code))) {
					keysFilteredOut.add(code);
				}
			}

			for (K filteredOutKey : keysFilteredOut) {
				filteredCodeCodesTableItemMap.remove(filteredOutKey);
			}
		}
	}

	/** {@inheritDoc} */
	public void resetPredicates() {
		predicates.clear();
		//This is done in addPredicate if no predicates exists,
		//doing it here anyway to avoid stale version of filteredCodeCodesTableItemMap
		filteredCodeCodesTableItemMap = new HashMap<K, T>(codeCodesTableItemMap);
	}

	/**
	 * FIXME: This method doesn't use the input Date to retrieve decode. FIXME: Method should probably change
	 * implementation or signature in the future. {@inheritDoc}
	 */
	protected V decode(Object code, Date... date) throws ItemNotFoundException, DecodeNotFoundException {
		// If there are predicates added to the codestable,
		// getCodesTableItem() will only return an item if it belongs to the filtered collection
		// of codestableitems, otherwise it will throw an exception
		T codesTableItem = getCodesTableItem(code);
		V decode = codesTableItem.getDecode();

		// If for some reason a code in the map maps to a null value, throw exception
		if (decode == null) {
			throw new DecodeNotFoundException(code);
		}

		return decode;
	}

	/** {@inheritDoc} */
	protected V decode(Object code, Locale locale, Date... date) throws DecodeNotFoundException {
		return decode(code, date);
	}

	/** {@inheritDoc} */
	public boolean validateCode(Enum code) {
		return validateCode(code.name());
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
