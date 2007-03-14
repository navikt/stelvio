package no.stelvio.common.codestable.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.NotCodesTableException;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableManager implements CodesTableManager {
	private Log log = LogFactory.getLog(DefaultCodesTableManager.class);

	/** The CodesTableFactory used for retrieval of codes tables. */
	private CodesTableFactory codesTableFactory;
	/** The CodesTableItemsFactory used for retrieval of codes table items. */
	private CodesTableItemsFactory codesTableItemsFactory;

	/** {@inheritDoc} */
	public <T extends CodesTableItem<K, V>, K extends Enum, V>
			CodesTable<T, K, V> getCodesTable(Class<T> codesTableItem) {
		validateCodesTableItemClass(codesTableItem);

		if (log.isDebugEnabled()) {
			log.debug("Creating a normal codes table for " + codesTableItem);
		}

		if (codesTableItemsFactory != null) {
			return new DefaultCodesTable<T, K, V>(codesTableItemsFactory.createCodesTableItems(codesTableItem));
		} else {
			return codesTableFactory.createCodesTable(codesTableItem);
		}
	}

	/** {@inheritDoc} */
	public <T extends CodesTablePeriodicItem<K, V>, K extends Enum, V>
			CodesTablePeriodic<T, K, V> getCodesTablePeriodic(Class<T> codesTablePeriodicItemClass) {
		validateCodesTableItemPeriodicClass(codesTablePeriodicItemClass);

		if (log.isDebugEnabled()) {
			log.debug("Creating a periodic codes table for " + codesTablePeriodicItemClass);
		}

		if (codesTableItemsFactory != null) {
			return new DefaultCodesTablePeriodic<T, K, V>(
					codesTableItemsFactory.createCodesTablePeriodicItems(codesTablePeriodicItemClass));
		} else {
			return codesTableFactory.createCodesTablePeriodic(codesTablePeriodicItemClass);
		}
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItem</>.
	 *
	 * @param codesTableClass the class to load a codestable for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of <code>CodesTable</code>
	 */
	private void validateCodesTableItemClass(final Class<? extends AbstractCodesTableItem<?, ?>> codesTableClass) {
		if (null != codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)) {
			throw new NotCodesTableException(codesTableClass);
		}
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTablePeriodicItem</>.
	 *
	 * @param codesTablePeriodicClass the class to load a codestable for.
	 * @throws no.stelvio.common.codestable.NotCodesTableException if the class to load a codestable for is not a subclass of
	 * <code>CodesTablePeriodic</code>.
	 */
	private void validateCodesTableItemPeriodicClass(
			final Class<? extends AbstractCodesTableItem<?, ?>> codesTablePeriodicClass) {
		if (null != codesTablePeriodicClass &&
				!CodesTablePeriodicItem.class.isAssignableFrom(codesTablePeriodicClass)) {
			throw new NotCodesTableException(codesTablePeriodicClass);
		}
	}

	/**
	 * Sets the <code>CodesTableItemsFactory</code> that is used to retrieve the codes table items from the database.
	 *
	 * @param codesTableItemsFactory a reference to the interface for retrieving codes table items from the database.
	 */
	public void setCodesTableItemsFactory(CodesTableItemsFactory codesTableItemsFactory) {
		this.codesTableItemsFactory = codesTableItemsFactory;
	}

	/**
	 * Sets the <code>CodesTableFactory</code> that is used to retrieve the codestables from the database.
	 *
	 * @param codesTableFactory a reference to the interface for retrieving codestables from the database.
	 */
	public void setCodesTableFactory(CodesTableFactory codesTableFactory) {
		log.warn("DEPRECATED - Use CodesTableItemsFactory instead - DEPRECATED");

		this.codesTableFactory = codesTableFactory;
	}
}