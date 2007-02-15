package no.stelvio.common.codestable.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.NotCodesTableException;
import no.stelvio.common.codestable.factory.CodesTableFactory;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableManager implements CodesTableManager {
	private Log log = LogFactory.getLog(DefaultCodesTableManager.class);

	/** The CodesTableFactory used for retrieval of codestables. */
	private CodesTableFactory codesTableFactory;

	/** {@inheritDoc} */
	public <T extends CodesTableItem> CodesTable<T> getCodesTable(Class<T> codesTableItem) {
		validateCodesTableClass(codesTableItem);

		if (log.isDebugEnabled()) {
			log.debug("Creating a normal codes table for " + codesTableItem);
		}

		return codesTableFactory.createCodesTable(codesTableItem);
	}

	/** {@inheritDoc} */
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItem) {
		validateCodesTablePeriodicClass(codesTableItem);

		if (log.isDebugEnabled()) {
			log.debug("Creating a periodic codes table for " + codesTableItem);
		}

		return codesTableFactory.createCodesTablePeriodic(codesTableItem);
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItem</>.
	 *
	 * @param codesTableClass the class to load a codestable for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of <code>CodesTable</code>
	 */
	private void validateCodesTableClass(final Class<? extends AbstractCodesTableItem> codesTableClass) {
		if (null != codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)) {
			throw new NotCodesTableException(codesTableClass);
		}
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItemPeriodic</>.
	 *
	 * @param codesTablePeriodicClass the class to load a codestable for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of
	 * <code>CodesTablePeriodic</code>.
	 */
	private void validateCodesTablePeriodicClass(final Class<? extends AbstractCodesTableItem> codesTablePeriodicClass) {
		if (null != codesTablePeriodicClass && !CodesTableItemPeriodic.class.isAssignableFrom(codesTablePeriodicClass)) {
			throw new NotCodesTableException(codesTablePeriodicClass);
		}
	}

	/**
	 * Sets the <code>CodesTableFactory</code> that is used to retrieve the codestables from the database.
	 *
	 * @param codesTableFactory a reference to the interface for retrieving codestables from the database.
	 */
	public void setCodesTableFactory(CodesTableFactory codesTableFactory) {
		this.codesTableFactory = codesTableFactory;
	}
}