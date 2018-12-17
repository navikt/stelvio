package no.stelvio.common.codestable;

import java.util.Comparator;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * Abstract base class for comaparators for comparing codes table items. This class implements the java.util.Comparator
 * interface and provides the compare method for comparing two AbstractCodesTableItems. The comparator can be used both for
 * non-periodic and periodic codes table items, and the methods for comparing the two types will differ.
 * 
 * Classes extending this class must implement methods for comparing periodic and non-periodic codes table items.
 * 
 * @author person6045563b8dec, Accenture
 * @since 1.0.6
 * 
 */
public abstract class AbstractCodesTableItemComparator implements Comparator<AbstractCodesTableItem> {

	/**
	 * Compares two codes table item objects.
	 * 
	 * @param item1
	 *            the first AbstractCodesTableItem
	 * @param item2
	 *            the second AbstractCodesTableItem
	 * @return a negative value if item1 is less than item2, a positive value if item1 is greater than item 2, zero if the two
	 *         items ordering is equal.
	 */
	@Override
	public int compare(AbstractCodesTableItem item1, AbstractCodesTableItem item2) {
		if (item1 instanceof AbstractCodesTablePeriodicItem && item2 instanceof AbstractCodesTablePeriodicItem) {
			AbstractCodesTablePeriodicItem periodicItem1 = (AbstractCodesTablePeriodicItem) item1;
			AbstractCodesTablePeriodicItem periodicItem2 = (AbstractCodesTablePeriodicItem) item2;
			return comparePeriodicItem(periodicItem1, periodicItem2);
		} else {
			return compareItem(item1, item2);
		}
	}

	/**
	 * Compares two non-periodic codes table items.
	 * 
	 * @param item1
	 *            the first AbstractCodesTableItem
	 * @param item2
	 *            the second AbstractCodesTableItem
	 * @return a negative value if item1 is less than item2, a positive value if item1 is greater than item 2, zero if the two
	 *         items ordering is equal.
	 */
	protected abstract int compareItem(AbstractCodesTableItem item1, AbstractCodesTableItem item2);

	/**
	 * Compares two periodic codes table items.
	 * 
	 * @param periodicItem1
	 *            the first AbstractCodesTablePeriodicItem
	 * @param periodicItem2
	 *            the second AbstractCodesTablePeriodicItem
	 * @return a negative value if item1 is less than item2, a positive value if item1 is greater than item 2, zero if the two
	 *         items ordering is equal.
	 */
	protected abstract int comparePeriodicItem(AbstractCodesTablePeriodicItem periodicItem1,
			AbstractCodesTablePeriodicItem periodicItem2);

}
