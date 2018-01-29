package no.stelvio.common.codestable.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import no.stelvio.common.codestable.CodesTableEmptyException;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.DuplicateItemsException;
import no.stelvio.common.codestable.ItemNotFoundException;
import no.stelvio.common.error.InvalidArgumentException;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

/**
 * Holds common functionality for <code>CodesTable</code> and <code>CodesTablePeriodic</code>.
 * 
 * @author personf8e9850ed756, Accenture
 * @author person983601e0e117 (Accenture)
 * 
 * @param <T>
 *            <code>AbstractCodesTableItem</code>'s or subclasses of <code>AbstractCodesTableItem</code> that the
 *            <code>AbstractCodesTable</code> can hold values of.
 * @param <K>
 *            an enum type variable
 * @param <V>
 *            an type variable
 */
abstract class AbstractCodesTable<T extends AbstractCodesTableItem<K, V>, K extends Enum, V> implements
		ApplicationContextAware, Serializable {
	private static final long serialVersionUID = -4072918237022568146L;

	/** Map containing Codes-Set<AbstractCodesTableItem> pairs. */
	private SortedMap<String, Set<T>> codeCodesTableItemSetMap = null;

	/**
	 * Holds the filtered map of <code>AbstractCodesTableItem</code>s created by taking the full list and applying the
	 * predicates. Only filtered if eagerFiltering is true
	 */
	private SortedMap<String, Set<T>> filteredCodeCodesTableItemSetMap = null;

	/**
	 * A comparator used by the codestable to sort the content according to the users preference. a null value causes the sorted
	 * map to be sorted by the keys natural ordering instead by this comparator
	 */
	private Comparator sortedMapComparator = null;

	/**
	 * List of predicates to use for filtering the list of <code>AbstractCodesTableItem</code>s.
	 */
	private List<Predicate> predicates = new ArrayList<Predicate>();

	/** The item class for this codes table. */
	private Class<? extends AbstractCodesTableItem> codesTableItemsClass;

	/**
	 * The current Spring application context for looking up locale beans for each code table.
	 */
	private ApplicationContext applicationContext;

	/**
	 * Indicates whether filtering should be performed each time a predicate is added This property may be adjusted to tune
	 * performance. Default value is fault. This will cause filtering each time the table is accessed (only if at least one
	 * predicate is set
	 */
	private boolean eagerFiltering = false;

	/**
	 * Initiates the codes table with the collection of items to hold. Checks that the input collection does not have any
	 * duplicates (defined by the <code>Object.equals()</code> contract).
	 * 
	 * @param codesTableItems
	 *            the collection of items to hold.
	 * @param codesTableItemsClass
	 *            the class of the items to hold.
	 * @throws CodesTableEmptyException
	 *             if the collection is null or empty.
	 * @throws DuplicateItemsException
	 *             if the collection has duplicate entries.
	 */	
	@SuppressWarnings("unchecked")
	protected void init(Collection<T> codesTableItems, Class<? extends AbstractCodesTableItem> codesTableItemsClass)
			throws CodesTableEmptyException, DuplicateItemsException {
		if (CollectionUtils.isEmpty(codesTableItems)) {
			throw new CodesTableEmptyException("No codes table items found for " + codesTableItemsClass.toString());
		}

		if (codesTableItemsClass == null) {
			throw new InvalidArgumentException("codesTableItemsClass is null");
		}

		this.codesTableItemsClass = codesTableItemsClass;
		codeCodesTableItemSetMap = new TreeMap<String, Set<T>>(sortedMapComparator);
		filteredCodeCodesTableItemSetMap = new TreeMap<String, Set<T>>(sortedMapComparator);

		/*
		 * Checks that the item can be added, examples of checks are duplicates and time overlap
		 */
		for (T codesTableItem : codesTableItems) {
			if (!isAddable(codeCodesTableItemSetMap, codesTableItem)) {
				continue;
			}

			// First item with this code, init Set for this code in map
			if (codeCodesTableItemSetMap.get(codesTableItem.getCodeAsString()) == null) {
				codeCodesTableItemSetMap.put(codesTableItem.getCodeAsString(), new TreeSet<T>());
				filteredCodeCodesTableItemSetMap.put(codesTableItem.getCodeAsString(), new TreeSet<T>());
			}

			// Add item to set in map
			codeCodesTableItemSetMap.get(codesTableItem.getCodeAsString()).add(codesTableItem);
			filteredCodeCodesTableItemSetMap.get(codesTableItem.getCodeAsString()).add(codesTableItem);

		}

		// for this to work, must the equals/hashcode be implemented correctly
		// on the item classes
		// if (codesTableItems.size() != codeCodesTableItemSetMap.size()) {
		// throw new DuplicateItemsException(codesTableItems);
		// }
	}

	/**
	 * Checks if an item exists in the mapped item set. Throws an exception if it exists, otherwise returns true
	 * 
	 * @param codeCodesTableItemMap
	 *            the map the element is to be added to
	 * @param item
	 *            the item to be checked
	 * @return the decision on whether this element should be added or not
	 * @throws DuplicateItemsException
	 *             if the item already exists in the mapped codesTableItem set
	 */
	protected boolean isAddable(SortedMap<String, Set<T>> codeCodesTableItemMap, T item) throws DuplicateItemsException {

		/* Check for duplicate items */
		Set<T> mappedItems = codeCodesTableItemMap.get(item.getCodeAsString());
		if (mappedItems == null) { // First item being added with that code
			return true;
		}

		for (T mappedItem : mappedItems) {
			if (mappedItem != null && mappedItem.equals(item)) {
				throw new DuplicateItemsException("There are duplicate items in the collection");
			}
		}

		return (true);
	}

	/**
	 * Initiates the codes table with the collection of items to hold based on the Comparator specified. Checks that the input
	 * collection does not have any duplicates (defined by the <code>Object.equals()</code> contract).
	 * 
	 * @param codesTableItems
	 *            the collection of items to hold.
	 * @param codesTableItemsClass
	 *            the class of the items to hold.
	 * @param comparator
	 *            a user supplied comparator used by the codestable to sort the content
	 * @throws CodesTableEmptyException
	 *             if the collection is null or empty.
	 * @throws DuplicateItemsException
	 *             if the collection has duplicate entries.
	 */
	protected void init(Collection<T> codesTableItems, Class<? extends AbstractCodesTableItem> codesTableItemsClass,
			Comparator comparator) throws CodesTableEmptyException, DuplicateItemsException {

		sortedMapComparator = comparator;

		init(codesTableItems, codesTableItemsClass);

	}

	/**
	 * Method that returns a set of codesTables with a code equal to the parameter code
	 * 
	 * NB! This method has absolutely no value for non-periodic codestable implementations!
	 * 
	 * @param code
	 *            as <K>
	 * @return set of codesTables with a code equal to the parameter code
	 */
	public Set<T> getCodesTableItems(K code) {
		return getCodesTableItems(getStringCodeFromEnumCode(code));
	}

	/**
	 * Method that returns a set of codesTables with a code equal to the parameter code
	 * 
	 * NB! This method has absolutely no value for non-periodic codestable implementations!
	 * 
	 * @param code
	 *            as String
	 * @return set of codesTables with a code equal to the parameter code
	 */
	public Set<T> getCodesTableItems(String code) {
		Set<T> setOfCodesTableItems = filteredCodeCodesTableItemSetMap.get(code);
		if (!eagerFiltering) { // Filters haven't been applied, apply now
			setOfCodesTableItems = filterCodesTableItems(setOfCodesTableItems);
		}
		return setOfCodesTableItems;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public Set<T> getCodesTableItems() {
		TreeSet<T> set = new TreeSet<T>(sortedMapComparator);

		for (Set<T> codesTableItems : filteredCodeCodesTableItemSetMap.values()) {
			if (!eagerFiltering) { // Filter haven't been applied, apply now
				set.addAll(filterCodesTableItems(codesTableItems));
			} else {
				set.addAll(codesTableItems);
			}
		}
		return (set);
	}

	/**
	 * Method used to get a set of {@link AbstractCodesTableItem} valid today. <em>
	 * Method should be implemented by CodesTable implementations
	 * that operate on periodic codestable items.
	 * 
	 * This implementation will return all codestableitems.
	 * </em>
	 * 
	 * @return Set of {@link AbstractCodesTablePeriodicItem} valid today
	 */
	public Set<T> getCodesTableItemsValidToday() {
		return getCodesTableItems();
	}

	/**
	 * 
	 * Method used to get a set of {@link AbstractCodesTableItem} valid for a specified date <em>.
	 * Method should be implemented by CodesTable implementations
	 * that operate on periodic codestable items.
	 * 
	 * This implementation will return all codestableitems.
	 * </em>
	 * 
	 * @param date
	 *            unused date
	 * @return Set of {@link AbstractCodesTablePeriodicItem} valid for specified date
	 */
	public Set<T> getCodesTableItemsValidForDate(Date date) {
		return getCodesTableItems();
	}

	/** {@inheritDoc} */
	public T getCodesTableItem(K code) throws ItemNotFoundException {
		String codeString = getStringCodeFromEnumCode(code);
		return getCodesTableItem(codeString);
	}

	/** {@inheritDoc} */
	public T getCodesTableItem(String code) throws ItemNotFoundException {
		T cti = findCodesTableItem(code);

		if (cti == null) {
			throw new ItemNotFoundException("No codes table item found for code: " + code);
		}

		return cti;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPredicate(Predicate predicate) {

		predicates.add(predicate);

		// Only do filtering in addPrediate if eagerFiltering is on
		if (eagerFiltering) {
			// If there are no previous predicates added to a codestable, all of
			// the codetableitems in a codetable
			// are filtered, or else the codestableitems in the filtered map are
			// filtered.
			synchronized (filteredCodeCodesTableItemSetMap) {
				if (predicates.isEmpty()) {
					filteredCodeCodesTableItemSetMap = copySortedMap(codeCodesTableItemSetMap);
				}
				// Loop through filter map and remove items that fails eval of
				// filter rules
				for (String code : filteredCodeCodesTableItemSetMap.keySet()) {
					Set<T> setAtKeyPosition = filteredCodeCodesTableItemSetMap.get(code);
					Set<T> itemsFilteredOut = new TreeSet<T>();

					// Create a list of Items that are filtered out
					for (T item : setAtKeyPosition) {
						// If CodesTableItem doesn't evaluate according to
						// predicate, remove from filtered list
						if (!predicate.evaluate(item)) {
							itemsFilteredOut.add(item);
						}
					}

					// Loop through list of items that are filtered out
					for (T filteredOutItem : itemsFilteredOut) {
						// Remove item from filtered list
						setAtKeyPosition.remove(filteredOutItem);
					}
				}
			}
		}
	}

	/** {@inheritDoc} */
	public void resetPredicates() {
		predicates.clear();
		// This is done in addPredicate if no predicates exists,
		// doing it here anyway to avoid stale version of
		// filteredCodeCodesTableItemSetMap
		if (eagerFiltering) { // Only neccessary if filering has been done
			// eagerly
			filteredCodeCodesTableItemSetMap = copySortedMap(codeCodesTableItemSetMap);
		}
	}

	/** {@inheritDoc} */
	public V getDecode(K code) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(getStringCodeFromEnumCode(code));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the resolved decode value of the given Locale as a String.
	 */
	public V getDecode(K code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(getStringCodeFromEnumCode(code), locale);
	}

	/** {@inheritDoc} */
	public V getDecode(K code, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(getStringCodeFromEnumCode(code), date);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the resolved decode value of the given Locale as a String.
	 */
	public V getDecode(K code, Locale locale, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(getStringCodeFromEnumCode(code), locale, date);
	}

	/** {@inheritDoc} */
	public V getDecode(String code) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the resolved decode value of the given Locale as a String.
	 */
	public V getDecode(String code, Locale locale) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code, locale);
	}

	/** {@inheritDoc} */
	public V getDecode(String code, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code, date);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the resolved decode value of the given Locale as a String.
	 */
	public V getDecode(String code, Locale locale, Date date) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code, locale, date);
	}

	/** {@inheritDoc} */
	public boolean validateCode(Enum code) {
		return validateCode(getStringCodeFromEnumCode(code));
	}

	/** {@inheritDoc} */
	public boolean validateCode(String code) {
		return findCodesTableItem(code) != null;
	}

	/**
	 * Creates an iterator with the filtered entries in this codes table.
	 * 
	 * @return an iterator with the filtered entries in this codes table.
	 */
	public Iterator<T> iterator() {
		return getCodesTableItems().iterator();
	}

	/**
	 * Makes a <code>String</code> representation of the codes table.
	 * 
	 * @return a <code>String</code> representation of the codes table.
	 */
	public String toString() {
		return new ToStringBuilder(this).append("filteredCodeCodesTableItemSetMap", filteredCodeCodesTableItemSetMap)
				.toString();
	}

	/**
	 * Looks up a decode for the given code, locale and optional date.
	 * 
	 * @param code
	 *            the code used to lookup the decode.
	 * @param locale
	 *            the locale used to lookup the decode.
	 * @param date
	 *            the date used to lookup the decode. This is optional.
	 * @return the resolved decode value of the given Locale as a String Message.
	 * @throws ItemNotFoundException
	 *             if codes table item was not found for the given keys.
	 * @throws DecodeNotFoundException
	 *             if the codes table item's decode was null.
	 */
	private V decode(Object code, Locale locale, Date... date) throws ItemNotFoundException, DecodeNotFoundException {
		Locale defaultLocale = LocaleContextHolder.getLocale();
		V decodeVal = decode(code, date);

		/*
		 * Dont perform resource lookup from the bundle for the default locale, default locale decodes should only be retrieved
		 * from the persistence tier.
		 */
		if (locale.equals(defaultLocale)) {
			return decodeVal;
		}

        // Should not use getbean here. Use Dependency injection...
		MessageSource messageSource = (MessageSource)applicationContext.getBean(codesTableItemsClass.getName());
		String message = messageSource.getMessage((String) code, null, locale);

		/* Return localised message, if it exists. */
		if (message != null) {
			return (V) message;
		}

		/* Fallthrough return value: the default local value, if it exists. */
		if (decodeVal instanceof String) {
			return decodeVal;
		}

		/* Nothing suitable exists */
		throw new DecodeNotFoundException("No decode found for code :" + code);
	}

	/**
	 * Looks up a decode for the given code and optional date. This method doesn't use the input Date to retrieve decode.
	 * 
	 * @param code
	 *            the code used to lookup the decode.
	 * @param date
	 *            the date used to lookup the decode. This is optional.
	 * @return the looked up decode.
	 * @throws ItemNotFoundException
	 *             if codes table item was not found for the given keys.
	 * @throws DecodeNotFoundException
	 *             if the codes table item's decode was null.
	 * @throws InvalidArgumentException
	 *             if input parameters is invalid.
	 */
	private V decode(Object code, Date... date) 
			throws ItemNotFoundException, DecodeNotFoundException, InvalidArgumentException {

		if (code == null) {
			throw new InvalidArgumentException("code is null");
		}

		// If there are predicates added to the codestable,
		// findCodesTableItem() will only return an item if it belongs to the
		// filtered collection
		// of codestableitems, otherwise it will return null
		T codesTableItem = findCodesTableItem(code.toString(), date);

		if (codesTableItem == null) {
			throw new ItemNotFoundException("No codes table item found for code: " + code);
		}

		V decode = codesTableItem.getDecode();

		// If for some reason a code in the map maps to a null value, throw
		// exception
		if (decode == null) {
			throw new DecodeNotFoundException("No decode found for code :" + code);
		}

		return decode;
	}

	/**
	 * Filters a Set of CodesTableItems using the specified predicates.
	 * 
	 * @param codesTableItems
	 *            Set of CodesTableItem
	 * @return filtered set
	 */
	protected Set<T> filterCodesTableItems(Set<T> codesTableItems) {
		Set<T> filteredSet = new TreeSet<T>();
		if (codesTableItems != null) {
			for (T t : codesTableItems) {
				boolean okToAdd = true;
				for (Predicate predicate : predicates) {
					okToAdd = (!predicate.evaluate(t)) ? false : okToAdd;
				}
				if (okToAdd) {
					filteredSet.add(t);
				}
			}
		}
		return filteredSet;
	}

	/**
	 * Finds the codes table item.
	 * 
	 * @param code
	 *            the code used to find the codes table item.
	 * @param date
	 *            the date used to find the codes table item.
	 * @return the found codes table item or <code>null</code> if not found.
	 */
	private T findCodesTableItem(String code, Date... date) {

		Set<T> setOfMatchingCodes = filteredCodeCodesTableItemSetMap.get(code);

		if (setOfMatchingCodes == null) {
			return null;
		} else if (!eagerFiltering && !CollectionUtils.isEmpty(predicates)) {
			// If codes haven't been filtered, and predicates (filters) have
			// been added
			// Do filtering
			setOfMatchingCodes = filterCodesTableItems(setOfMatchingCodes);
		}

		if (CollectionUtils.isEmpty(setOfMatchingCodes)) {
			return null;
		} else {
			return findCodesTableItemForDate(setOfMatchingCodes, date);
		}

	}

	/**
	 * This implementation doesn't consider the date input. AbstractCodesTable-implementations that needs to match items on
	 * dates should implement this method
	 * 
	 * @param codesTableItems
	 *            set of codesTable items
	 * @param date
	 *            unused date
	 * @return T matching criteria
	 */
	protected T findCodesTableItemForDate(Set<T> codesTableItems, Date... date) {
		// Return first one in set
		for (T codesTableItem : codesTableItems) {
			return codesTableItem;
		}
		return null; // If set empty, return null
	}

	/**
	 * Takes a SortedMap with a String-Set key/value and makes a copy of it. Used to ensure that the
	 * {@link #codeCodesTableItemSetMap} and {@link #filteredCodeCodesTableItemSetMap} don't operate on the same Sets (in the
	 * value portion of the map)
	 * 
	 * @param mapToCopy
	 *            SortedMap to be copied
	 * @return new map instance, with new Set instances. (The AbstractCodesTableItem instances will be the same in both Sets)
	 * 
	 */
	@SuppressWarnings("unchecked")
	private SortedMap<String, Set<T>> copySortedMap(SortedMap<String, Set<T>> mapToCopy) {

		SortedMap<String, Set<T>> copyMap = new TreeMap<String, Set<T>>(sortedMapComparator);
		for (String key : mapToCopy.keySet()) {
			Set<T> setAtKey = copyMap.get(key);
			Set<T> copyOfSet = new TreeSet<T>(setAtKey);
			copyMap.put(key, copyOfSet);
		}
		return copyMap;
	}

	/**
	 * Will return code.getIllegalCode if code is instance of {@link IllegalCodeEnum} otherwise it will return code.name().
	 * 
	 * @param code
	 *            identifying CodesTableItem in persistence store
	 * @return code name or illegal code
	 */
	protected String getStringCodeFromEnumCode(Enum code) {
		return (code instanceof IllegalCodeEnum) ? ((IllegalCodeEnum) code).getIllegalCode() : code.name();
	}

	/**
	 * Sets the application context.
	 * 
	 * @param applicationContext
	 *            the application context
	 * @throws BeansException
	 *             if a BeansException occurs
	 */
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Returns the eagerFiltering flag.
	 * 
	 * @return the eagerFiltering flag
	 */
	public boolean isEagerFiltering() {
		return eagerFiltering;
	}

	/**
	 * Indicates whether flitering should be done each time a predicate is added or each time an item is accessed.
	 * <p>
	 * Default value is <code>false</code>.
	 * </p>
	 * 
	 * @param eagerFiltering
	 *            <code>true</code> to turn on eager filtering
	 */
	public void setEagerFiltering(boolean eagerFiltering) {
		this.eagerFiltering = eagerFiltering;
	}
}
