package no.stelvio.common.codestable.support;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DecodeNotFoundException;

/**
 * Implementation of CodesTablePeriodic for retrieving <code>CodesTable</code>s that has defined a period of validity.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @todo add safe copying of input/output, that is, constructor and getItems()
 * @todo should this check that the period is not overlapping for "like" rows? -> just an equal/hashcode impl that
 * throws exception when 2 rows have wrapping periods, hashcode uses code/date_from/is_approved, equals checks for
 * overlapping
 */
public class DefaultCodesTablePeriodic<T extends CodesTableItemPeriodic<K, V>, K extends Enum, V> extends AbstractCodesTable<T, K, V> implements CodesTablePeriodic<T, K, V> {
	private static final long serialVersionUID = 6455631274807017184L;

	/**
	 * Creates a <code>DefaultCodesTablePeriodic</code> with a list of <code>CodesTableItemPeriodic</code>s.
	 *
	 * @param codesTableItems list of <code>CodesTableItemPeriodic</code>s this <code>DefaultCodesTablePeriodic</code>
	 * consists of.
	 */
	public DefaultCodesTablePeriodic(List<T> codesTableItems) {
		init(codesTableItems);
	}

	/** {@inheritDoc} */
	public V getDecode(Enum code, Date date) throws DecodeNotFoundException {
		return decode(code.name(), date);
	}

	/** {@inheritDoc} */
	public V getDecode(Enum code, Locale locale, Date date) throws DecodeNotFoundException {
		return decode(code.name(), locale, date);
	}

	/** {@inheritDoc} */
	public String getDecode(Object code, Date date) throws DecodeNotFoundException {
		return (String) decode(code, date);
	}

	/** {@inheritDoc} */
	public String getDecode(Object code, Locale locale, Date date) throws DecodeNotFoundException {
		return (String) decode(code, locale, date);
	}
}
