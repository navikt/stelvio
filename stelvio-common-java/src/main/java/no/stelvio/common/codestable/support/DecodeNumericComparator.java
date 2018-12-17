package no.stelvio.common.codestable.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.AbstractCodesTableItemComparator;

/**
 * Comparator class for comparing CodesTableItems and CodesTablePeriodicItems with numeric decode values. This comparator can
 * only be used when the decode value of each codes table item is numeric and possible to convert to an Integer.
 * 
 * This compator can typically be used to sort a collection of AbstractCodesTableItems according to their numeric decode values.
 * The comparator assumes that the decode values are convertable to Integers. If not, an exception will be thrown when trying to
 * convert the decode values.
 * 
 * @author person6045563b8dec, Accenture
 * @since 1.0.6
 * 
 */
public class DecodeNumericComparator extends AbstractCodesTableItemComparator {

	private static final Log LOGGER = LogFactory.getLog(DecodeNumericComparator.class);

	/**
	 * Compares two AbstractCodesTableItem objects according to the natural sorting of their numeric decode values. This metod
	 * is called from the compare method of the superclass for non-periodic items.
	 * 
	 * @param item1
	 *            the first AbstractCodesTableItem
	 * @param item2
	 *            the second AbstractCodesTableItem
	 * @return a negative value if the natural ordering of item1s decode value is less than the one for item2, a positive value
	 *         if the natural ordering of item1s decode value is greater than the one for item2, and zero if the natural odering
	 *         of decode value of the two items are the same
	 */
	@Override
	protected int compareItem(AbstractCodesTableItem item1, AbstractCodesTableItem item2) {
		Integer decode1Numeric;
		Integer decode2Numeric;
		try {
			decode1Numeric = new Integer(item1.getDecode().toString());
			decode2Numeric = new Integer(item2.getDecode().toString());
		} catch (NumberFormatException nfe) {
			// rethrowing the exception in order to log an error message
			LOGGER.warn("Exception occured while parsing decode of items " + item1 + " , " + item2 + "."
					+ " This comparator should only be used for codes table items with numeric decode values.");
			throw nfe;
		}
		return decode1Numeric.compareTo(decode2Numeric);
	}

	/**
	 * Compares two AbstractCodesTablePeriodicItem objects according to the natural sorting of their numeric decode values. This
	 * metod is called from the compare method of the superclass for periodic items.
	 * 
	 * @param periodicItem1
	 *            the first AbstractCodesTablePeriodicItem
	 * @param periodicItem2
	 *            the second AbstractCodesTablePeriodicItem
	 * @return a negative value if the natural ordering of item1s decode value is less than the one for item2, a positive value
	 *         if the natural ordering of item1s decode value is greater than the one for item2, and zero if the natural odering
	 *         of decode value of the two items are the same
	 */
	@Override
	protected int comparePeriodicItem(AbstractCodesTablePeriodicItem periodicItem1, 
			AbstractCodesTablePeriodicItem periodicItem2) {
		return (compareItem(periodicItem1, periodicItem2) != 0) ? compareItem(periodicItem1, periodicItem2) : periodicItem1
				.getFromDate().compareTo(periodicItem2.getFromDate());
	}

}
