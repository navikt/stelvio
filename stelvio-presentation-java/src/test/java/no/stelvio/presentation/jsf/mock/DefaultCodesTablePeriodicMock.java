package no.stelvio.presentation.jsf.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * @version $Id$
 * 
 * @param <T>
 *            An AbstractCodesTablePeriodicItem.
 * @param <K>
 *            An enum.
 * @param <V>
 *            The decoded value.
 */
public class DefaultCodesTablePeriodicMock<T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> implements
		CodesTablePeriodic<T, K, V> {

	private static final long serialVersionUID = -6774085947829732794L;

	private List<T> codesTableItems;
	private HashMap<String, T> codesTableItemsHash;
	private HashSet<T> codesTableItemsSet;

	/**
	 * @param codesTableItems
	 *            the input.
	 */
	public DefaultCodesTablePeriodicMock(List<T> codesTableItems) {
		this.codesTableItems = codesTableItems;
		this.codesTableItemsHash = new HashMap<>();
		this.codesTableItemsSet = new HashSet<>();
		for (T t : this.codesTableItems) {
			this.codesTableItemsHash.put(t.getCodeAsString(), t);
			this.codesTableItemsSet.add(t);
		}

	}

	/** {@inheritDoc} */
	public String getDecode(Object code, Date date) throws DecodeNotFoundException {
		return null;
		// (String) decode(code, date)
	}

	/** {@inheritDoc} */
	public V getDecode(String code, Date date) throws DecodeNotFoundException {
		return null;
		// (String) decode(code, date)
	}

	/** {@inheritDoc} */
	public String getDecode(Object code, Locale locale, Date date) throws DecodeNotFoundException {
		return null;
		// (String) decode(code, locale, date)
	}

	@Override
	public void addPredicate(Predicate<T> predicate) {
		

	}

	/** {@inheritDoc} */
	public Set<T> getCodeTableItems() {
		return this.codesTableItemsSet;
	}

    @Override
	public Set<T> getCodesTableItems() {
		return this.codesTableItemsSet;
	}

	/** {@inheritDoc} */
	public T getCodesTableItem(Object code) throws ItemNotFoundException {
		return this.codesTableItemsHash.get(code);
	}

	/** {@inheritDoc} */
	public List<T> getItems() {
		
		return this.codesTableItems;
	}

	@Override
	public void resetPredicates() {

	}

    @Override
	public boolean validateCode(Enum code) {
		return false;
	}

    @Override
	public boolean validateCode(String code) {
		return false;
	}

    @Override
	public T getCodesTableItem(String code) throws ItemNotFoundException {
		return this.codesTableItemsHash.get(code);
	}

    @Override
	public V getDecode(K code) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public V getDecode(String code) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public V getDecode(K code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public V getDecode(String code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public V getDecode(K code, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public V getDecode(K code, Locale locale, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public V getDecode(String code, Locale locale, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return null;
	}

    @Override
	public Iterator<T> iterator() {
		return codesTableItems.iterator();
	}

	/**
	 * Get decode.
	 * 
	 * @param code
	 *            the code to decode
	 * @return decoded string
	 * @throws ItemNotFoundException
	 *             when an item is not found
	 * @throws DecodeNotFoundException
	 *             when decode not found
	 */
	public String getDecode(Object code) throws ItemNotFoundException, DecodeNotFoundException {
		
		return null;
	}

	/**
	 * Get decode.
	 * 
	 * @param code --
	 * @param locale --
	 * @return --
	 * @throws ItemNotFoundException --
	 * @throws DecodeNotFoundException --
	 */
	public String getDecode(Object code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException {
		
		return null;
	}

	/**
	 * Get CodesTableItemsValidToday.
	 * 
	 * @return items
	 */
	@Override
	public Set<T> getCodesTableItemsValidToday() {
		return this.codesTableItemsSet;
	}

	/**
	 * Get coestable item.
	 * 
	 * @param code Code.
	 * @return <T>
	 *            An AbstractCodesTablePeriodicItem.
	 * @throws ItemNotFoundException exception.
	 */
	@Override
	public T getCodesTableItem(K code) throws ItemNotFoundException {
		if (code instanceof Enum) {
			return this.codesTableItemsHash.get(code.name());
		} else {
			return null;
		}
	}

	/**
	 * Get CodesTableItemsValidForDate.
	 * @param date
	 *            An AbstractCodesTablePeriodicItem.
	 * @return set &lt;T&gt;
	 */
	@Override
	public Set<T> getCodesTableItemsValidForDate(Date date) {
		return null;
	}

	/**
	 * Get codestable item.
	 * 
	 * @param code Code.
	 * @param date date.
	 * @return &lt;T&gt;
	 */
	@Override
	public T getCodesTableItem(K code, Date date) {
		return null;
	}
	/**
	 * Get codestable item.
	 * 
	 * @param code Code.
	 * @param date date.
	 * @return &lt;T&gt;
	 */
	@Override
	public T getCodesTableItem(String code, Date date) {
		return null;
	}
	/**
	 * Get codetables items.
	 * 
	 * @param code Code.
	 * @return Set&lt;T&gt;
	 */
	@Override
	public Set<T> getCodesTableItems(K code) {
		return null;
	}
	/**
	 * Get codestable items.
	 * 
	 * @param code Code.
	 * @return Set&lt;T&gt;
	 */
	@Override
	public Set<T> getCodesTableItems(String code) {
		return null;
	}
}
