package no.stelvio.common.codestable.support;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.NotCodesTableException;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableManager implements CodesTableManager, ApplicationContextAware {
	private Log log = LogFactory.getLog(DefaultCodesTableManager.class);

	/** The CodesTableItemsFactory used for retrieval of codes table items. */
	private CodesTableItemsFactory codesTableItemsFactory;

	/** The <code>ApplicationContext</code> this manager is run in. */
	private ApplicationContext applicationContext;

	/** {@inheritDoc} */
	public <T extends AbstractCodesTableItem<K, V>, K extends Enum, V> CodesTable<T, K, V> getCodesTable(
			Class<T> codesTableItemClass) {
		validate(codesTableItemClass, AbstractCodesTableItem.class);

		if (log.isDebugEnabled()) {
			log.debug("Creating a normal codes table for " + codesTableItemClass);
		}

		CodesTable<T, K, V> ct;

		ct = new DefaultCodesTable<T, K, V>(codesTableItemsFactory.createCodesTableItems(codesTableItemClass),
				codesTableItemClass);

		injectApplicationContextIfPossible(ct);

		return ct;
	}

	/** {@inheritDoc} */
	public <T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> 
	CodesTablePeriodic<T, K, V> getCodesTablePeriodic(Class<T> codesTablePeriodicItemClass) {
		validate(codesTablePeriodicItemClass, AbstractCodesTablePeriodicItem.class);

		if (log.isDebugEnabled()) {
			log.debug("Creating a periodic codes table for " + codesTablePeriodicItemClass);
		}

		CodesTablePeriodic<T, K, V> ctp;

		ctp = new DefaultCodesTablePeriodic<T, K, V>(codesTableItemsFactory
				.createCodesTablePeriodicItems(codesTablePeriodicItemClass), codesTablePeriodicItemClass);

		injectApplicationContextIfPossible(ctp);

		return ctp;
	}

	/**
	 * Inject this instance's <code>ApplicationContext</code> into the specified object.
	 * 
	 * @param ct
	 *            the object to inject the <code>ApplicationContext</code> into.
	 */
	private void injectApplicationContextIfPossible(final Object ct) {
		if (applicationContext != null && ct instanceof AbstractCodesTable) {
			((AbstractCodesTable) ct).setApplicationContext(applicationContext);
		}
	}

	/**
	 * Checks that the class to load a codes table for is a subclass of <code>CodesTableItem</code>.
	 * 
	 * @param codesTableClass
	 *            the class to load a codestable for.
	 * @param superClass
	 *            the class to check taht the codes table class is a sub class of.
	 */
	private void validate(Class<? extends AbstractCodesTableItem<?, ?>> codesTableClass, Class<?> superClass) {
		if (null == codesTableClass) {
			throw new NotCodesTableException("<null> is not a codes table");
		} else if (!superClass.isAssignableFrom(codesTableClass)) {
			throw new NotCodesTableException(codesTableClass + "is not a codes table");
		}
	}

	/**
	 * Sets the <code>CodesTableItemsFactory</code> that is used to retrieve the codes table items from the database.
	 * 
	 * @param codesTableItemsFactory
	 *            a reference to the interface for retrieving codes table items from the database.
	 */
	public void setCodesTableItemsFactory(CodesTableItemsFactory codesTableItemsFactory) {
		this.codesTableItemsFactory = codesTableItemsFactory;
	}

	/**
	 * Sets the <code>ApplicationContext</code> this instance run in.
	 * 
	 * @param applicationContext
	 *            the <code>ApplicationContext</code> this instance run in.
	 */
	public void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
