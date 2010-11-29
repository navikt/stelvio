package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * Interface defining functionality for accessing and caching persistent codes tables. The class is not implemented as a
 * Singleton, but should be externally managed as a singleton, e.g. by using Spring Framework.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id $
 */
public interface CodesTableManager {
	/**
	 * Retrieves a <code>CodesTable</code> by using <code>CodesTableFactory</code>. The
	 * <code>CodesTable</code> that the input <code>CodesTableItem</code> or <code>IdAsKeyCodesTableItem</code>
	 * belongs to, is the <code>CodesTable</code> that shall be retrieved.
	 *
	 * @param <K> an enum type variable
	 * @param <V> a type variable
	 * @param <T> Subclasses of <code>CodesTableItem</code> or <code>IdAsKeyCodesTableItem</code> that are
	 * valid input parameters, and that the <code>CodesTable</code> returned by the method holds values of.
	 * @param codesTableItemClass the <code>CodesTable</code> to retrieve, represented by a subclass of
	 * <code>CodesTableItem</code> or <code>IdAsKeyCodesTableItem</code>.
	 * @return The fetched <code>CodesTable</code>.
	 */
	<T extends AbstractCodesTableItem<K, V>, K extends Enum, V>
			CodesTable<T, K, V> getCodesTable(Class<T> codesTableItemClass);

	/**
	 * Retrieves a <code>CodesTablePeriodic</code> by using <code>CodesTableFactory</code>. The
	 * <code>CodesTablePeriodic</code> that the input <code>CodesTablePeriodicItem</code> or
	 * <code>IdAsKeyCodesTablePeriodicItem</code> belongs to, is the <code>CodesTablePeriodic</code> that shall be
	 * retrieved.
	 * @param <K> an enum type variable
	 * @param <V> a type variable
	 * @param <T> Subclasses of <code>CodesTablePeriodicItem</code> or <code>IdAsKeyCodesTablePeriodicItem</code> that
	 * are valid input parameters, and that the <code>CodesTablePeriodic</code> returned by the method holds values of.
	 * @param codesTablePeriodicItemClass the <code>CodesTablePeriodic</code> to retrieve, represented by a subclass of
	 * <code>CodesTablePeriodicItem</code> or <code>IdAsKeyCodesTablePeriodicItem</code>.
	 * @return The fetched <code>CodesTablePeriodic</code>.
	 */
	<T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V>
			CodesTablePeriodic<T, K, V> getCodesTablePeriodic(Class<T> codesTablePeriodicItemClass);
}
