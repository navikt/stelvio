package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

/**
 * @deprecated Use <code>CodesTablePeriodicItem</code> instead.
 * @see CodesTablePeriodicItem
 */
public abstract class CodesTableItemPeriodic<K extends Enum, V> extends CodesTablePeriodicItem<K, V> {
	private static final long serialVersionUID = -7207635739439965031L;

	/** Constructor for <code>CodesTableItemPeriodic</code>. Only available to subclasses. */
	protected CodesTableItemPeriodic() {
	}

	/**
	 * Constructor for an item, initializing all of its attributes.
	 *
	 * @param code the code.
	 * @param decode the decode.
	 * @param fromDate the date an item is valid from.
	 * @param toDate the date an item is valid to.
	 * @param locale the locale of the item.
	 * @param isValid validity of the item.
	 * @deprecated Should not be possible to construct new instances of CodesTableItems; only through ORM
	 */
	@Deprecated
	public CodesTableItemPeriodic(K code, V decode, Date fromDate, Date toDate, Locale locale, boolean isValid) {
		super(code, decode, fromDate, toDate, locale, isValid);
	}

	/**
	 * Constructor for an item, initializing all of its attributes.
	 *
	 * @param code the code.
	 * @param decode the decode.
	 * @param fromDate the date an item is valid from.
	 * @param toDate the date an item is valid to.
	 * @param isValid validity of the item.
	 * @deprecated Should not be possible to construct new instances of CodesTableItems; only through ORM
	 */
	@Deprecated
	public CodesTableItemPeriodic(K code, V decode, Date fromDate, Date toDate, boolean isValid) {
		super(code, decode, fromDate, toDate, isValid);
	}
}