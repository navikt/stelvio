package no.stelvio.common.codestable.support;

import java.util.Comparator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This Comparator may be used to order a collection of {@link AbstractCodesTableItem}s according to their decode. If to decodes
 * are equal, and the {@link AbstractCodesTableItem}s {@link AbstractCodesTablePeriodicItem} subclasses dates will be used to
 * compare the two items
 * 
 * It may typically be used sort a <code>List</code> of AbstractCodesTableItems in the presentation tier. It shouldn't be used
 * to sort OrderedSet as this will result in a Set with unique decodes, which may not be correct. (A combination of unique code
 * and periods is what one would normally want)
 * 
 * Be aware that it should never be passed to an {@link AbstractCodesTable} implementation as it's ordering is inconsistent with
 * <code>equals</code>. Meaning that <code>(o1.compareTo(o2)==0)</code> doesn't mean that <code>o1.equals(o2)</code>
 * 
 * @author person983601e0e117 (Accenture)
 * @since 1.0.5
 * 
 */
public class DecodeComparator implements Comparator<AbstractCodesTableItem> {

	/**
	 * Compares two {@link AbstractCodesTableItem}s based on a string representation of their decode. If the two items are
	 * assignable from {@link AbstractCodesTablePeriodicItem} the from date is used to compare two items with identical decode.
	 * 
	 * @param item1
	 *            the first AbstractCodesTableItem item
	 * @param item2
	 *            the second AbstractCodesTableItem item
	 * @return 0 if equal,
	 */
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
	 * Compares two {@link AbstractCodesTablePeriodicItem}s based on a string representation of their decode. If the two items
	 * have identical decode the from date is used to compare the two items
	 * 
	 * @param item1
	 *            the first AbstractCodesTablePeriodicItem item
	 * @param item2
	 *            the second AbstractCodesTablePeriodicItem item
	 * @return 0 if equal,
	 */
	private int comparePeriodicItem(AbstractCodesTablePeriodicItem item1, AbstractCodesTablePeriodicItem item2) {
		return (compareItem(item1, item2) != 0) ? compareItem(item1, item2) : item1.getFromDate()
				.compareTo(item2.getFromDate());

	}

	/**
	 * Compares two {@link AbstractCodesTableItem}s based on a string representation of their decode.
	 * 
	 * @param item1
	 *            the first AbstractCodesTableItem item
	 * @param item2
	 *            the second AbstractCodesTableItem item
	 * @return 0 if equal,
	 */

	private int compareItem(AbstractCodesTableItem item1, AbstractCodesTableItem item2) {
		return item1.getDecode().toString().compareTo(item2.getDecode().toString());
	}

	/**
	 * Checks if an object is of type DecodeComparator.
	 * 
	 * @param o
	 *            an object
	 * @return true if the object is of type DecodeComparator, else false
	 */
	public boolean equals(Object o) {
		return (o != null && o instanceof DecodeComparator);
	}

	/**
	 * Returns the hashCode. All instances of DecodeComperator should return the same hashCode.
	 * 
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.appendSuper(super.hashCode());
		builder.append(DecodeComparator.class);
		return builder.toHashCode();
	}

}
