package no.stelvio.common.codestable.support;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import no.stelvio.common.codestable.CodesTableEmptyException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DuplicateItemsException;
import no.stelvio.common.util.DateUtil;

/**
 * Implementation of CodesTablePeriodic for retrieving <code>CodesTable</code>s that has defined a period of validity.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @author person983601e0e117, Accenture
 * @author person19fa65691a36, Accenture
 * @version $Id$
 * 
 * @param <T>
 *            <code>AbstractCodesTablePeriodicItem</code>'s or subclasses of <code>AbstractCodesTablePeriodicItem</code> that
 *            the <code>DefaultCodesTablePeriodic</code> can hold values of.
 * @param <K>
 *            an enum type variable
 * @param <V>
 *            an type variable
 */
public class DefaultCodesTablePeriodic<T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> extends
		AbstractCodesTable<T, K, V> implements CodesTablePeriodic<T, K, V> {
	private static final long serialVersionUID = 6455631274807017184L;

	/**
	 * Creates a <code>DefaultCodesTablePeriodic</code> with a collection of <code>CodesTablePeriodicItem</code>s.
	 * 
	 * @param codesTableItems
	 *            collection of <code>CodesTablePeriodicItem</code>s this <code>DefaultCodesTablePeriodic</code> consists of.
	 * @param codesTableItemsClass
	 *            the class of the items to hold.
	 * @throws CodesTableEmptyException
	 *             if the collection is null or empty.
	 * @throws DuplicateItemsException
	 *             if the collection has duplicate entries.
	 */
	public DefaultCodesTablePeriodic(Collection<T> codesTableItems, 
			Class<? extends AbstractCodesTableItem> codesTableItemsClass)
			throws CodesTableEmptyException, DuplicateItemsException {
		init(codesTableItems, codesTableItemsClass);
	}

	/**
	 * Returns the code table item associated with the given code, in the date period in which the given date belongs to.
	 * 
	 * The parameter date (used to determine the period) time is set to 12:00:00. The fromDate time is set to 00:00:00 by
	 * default, and the toDate is set to 23:59:59.
	 * 
	 * @param code
	 *            the code, as an <code>Enum</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>.
	 * @param date
	 *            the date determining which period to retrieve the <code>CodesTablePeriodicItem</code>.
	 * @return the <code>CodesTablePeriodicItem</code> that corresponds to the given code.
	 */
	public T getCodesTableItem(K code, Date date) {
		return getCodesTableItem(getStringCodeFromEnumCode(code), date);
	}

	/**
	 * Returns the code table item associated with the given code, in the date period in which the given date belongs to.
	 * 
	 * The parameter date (used to determine the period) time is set to 12:00:00. The fromDate time is set to 00:00:00 by
	 * default, and the toDate is set to 23:59:59.
	 * 
	 * @param code
	 *            the code, as a <code>String</code>, used to lookup the corresponding <code>CodesTablePeriodicItem</code>.
	 * @param date
	 *            the date determining which period to retrieve the <code>CodesTablePeriodicItem</code>.
	 * @return the <code>CodesTablePeriodicItem</code> that corresponds to the given code.
	 */
	public T getCodesTableItem(String code, Date date) {

		Set<T> itemsForCode = getCodesTableItems(code);
		for (T t : itemsForCode) {
			if (DateUtil.isDateInPeriod(tranformValidDateToBeUsedInComparison(date), t.getFromDate(),
					transformToDateToBeUsedInComparison(t.getToDate()))) {
				return t;
			}
		}
		return null;
	}

	/**
	 * @inheritDoc This method checks whether the CodesTableItem being added has the same Code as a CodesTableItem that is
	 *             already in the CodesTable. If an Item with the same code exsists, the code will go on to check whether the to
	 *             Items have overlapping to and from dates.
	 * 
	 *             A <code>null</code> value in to date is considered as date not set, and a code that is still valid. There may
	 *             only be ONE Item for each code with null as to date.
	 * 
	 * @param codeCodesTableItemMap
	 *            set of mapped codesTabItems
	 * @param item
	 *            the new item
	 * @return true if there is no overlap, false otherwise
	 */
	@Override
	protected boolean isAddable(SortedMap<String, Set<T>> codeCodesTableItemMap, T item) {

		Set<T> mappedItems = codeCodesTableItemMap.get(item.getCodeAsString());

		if (mappedItems == null) { // First item being added with that code
			return true;
		}

		// Travers Set of already added CodesTableItems with code matching item's code
		for (T mappedItem : mappedItems) {

			/* Check if there is a overlap in the time interval for similar items */
			// mappedItem = codeCodesTableItemMap.get(item.getCodeAsString());
			if (mappedItem != null /* && item != null */) {

				Date dFrom = item.getFromDate();

				// Treat toDates as tomorrow, to avoid NullPointers further down. <null> means no to date (valid today)
				Date dTo = (item.getToDate() == null) ? DateUtil.ETERNITY : item.getToDate();
				Date mappedTo = (mappedItem.getToDate() == null) ? DateUtil.ETERNITY : mappedItem.getToDate();

				if (checkIntervalOverlap(dFrom, dTo, mappedItem.getFromDate(), mappedTo)) {
					return false;
				}
			}
		}

		/* Duplicate items are allowed in periodic tables as long as they dont overlap */
		// /* Perform other checks as defined in the super class */
		// if (super.isAddable(codeCodesTableItemMap, item) == false)
		// return(false);
		return (true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T findCodesTableItemForDate(Set<T> codesTableItems, Date... date) {

		// Only use date to find Item, if it's supplied
		if (date.length != 0) {
			for (T codesTableItem : codesTableItems) {
				Date toDate = transformToDateToBeUsedInComparison(codesTableItem.getToDate());

				if (checkIntervalOverlap(date[0], date[0], codesTableItem.getFromDate(), toDate)) {
					return codesTableItem;
				}
			}
			return null;
		} else {
			return super.findCodesTableItemForDate(codesTableItems);
		}
	}

	/**
	 * Method used to get a set of {@link AbstractCodesTableItem} valid today.
	 * 
	 * @return Set of {@link AbstractCodesTablePeriodicItem} valid today
	 */
	@Override
	public Set<T> getCodesTableItemsValidToday() {
		return getCodesTableItemsValidForDate(createDate(System.currentTimeMillis()));
	}

	/**
	 * Method used to get a set of {@link AbstractCodesTableItem} for a specified date.
	 * 
	 * @param date
	 *            the date that the {@link AbstractCodesTablePeriodicItem} should be valid for
	 * @return Set of {@link AbstractCodesTablePeriodicItem} valid for supplied date
	 */
	@Override
	public Set<T> getCodesTableItemsValidForDate(Date date) {
		Date transformedDate = tranformValidDateToBeUsedInComparison(date);

		Set<T> allCodesTableItems = getCodesTableItems();
		Set<T> returnSet = new TreeSet<T>();
		for (T t : allCodesTableItems) {
			Date fromDate = t.getFromDate();
			Date toDate = transformToDateToBeUsedInComparison(t.getToDate());

			// If items period overlap the specified date, add to return set
			if (t.isValid() && checkIntervalOverlap(transformedDate, transformedDate, fromDate, toDate)) {
				returnSet.add(t);
			}
		}
		return returnSet;
	}

	/**
	 * Method that transforms <code>null</code> to 31-12-9999 sets the time of day to 12:00:00 for the input Date.
	 * 
	 * @param date
	 *            to be adjusted
	 * @return date where time of day is 12:00:00
	 */
	private Date tranformValidDateToBeUsedInComparison(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date == null) {
			cal.setTimeInMillis(DateUtil.ETERNITY.getTime());
		} else {
			cal.setTimeInMillis(date.getTime());
		}
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * Method that transforms <code>null</code> to 31-12-9999 sets the time of day to 23:59:59 for the input Date.
	 * 
	 * @param date
	 *            to be adjusted
	 * @return date where time of day is 23:59:59
	 */
	private Date transformToDateToBeUsedInComparison(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date == null) {
			cal.setTimeInMillis(DateUtil.ETERNITY.getTime());
		} else {
			cal.setTimeInMillis(date.getTime());
		}
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * Creates a date for the specified time in millis Sets hours to 0 (24-hour clock), minutes, seconds and milliseconds to 0.
	 * 
	 * @param millis
	 *            milliseconds since January 1 1970, 00:00:00 GMT
	 * @return date
	 */
	private Date createDate(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * Returns true if there is an overlap between the first set of dates and the second set of dates.
	 * 
	 * @param int1StartDate
	 *            start date of the first interval
	 * @param int1EndDate
	 *            end date of the first interval
	 * @param int2StartDate
	 *            start date of the second interval
	 * @param int2EndDate
	 *            end date of the second interval
	 * @return true if there an overlap, false otherwise
	 */
	private boolean checkIntervalOverlap(Date int1StartDate, Date int1EndDate, Date int2StartDate, Date int2EndDate) {
		return !(int2EndDate.getTime() <= int1StartDate.getTime() || int2StartDate.getTime() >= int1EndDate.getTime());
	}

}
