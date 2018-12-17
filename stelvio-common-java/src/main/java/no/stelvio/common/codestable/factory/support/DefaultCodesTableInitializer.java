package no.stelvio.common.codestable.factory.support;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableConfigurationException;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.CodesTablePeriodicItem;

/**
 * Initializes the specified codes tables and loads them into their cache.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableInitializer implements InitializingBean {
	private static final Log LOG = LogFactory.getLog(DefaultCodesTableInitializer.class);

	/** The CodesTableManager. */
	private CodesTableManager codesTableManager;

	/** A set of codestableclasses - defined in the application context. */
	private Set<Class<? extends CodesTableItem<? extends Enum, ?>>> codesTableItemClasses = new HashSet<>();
	/** A set of codestableperiodicclasses - defined in the application context. */
	private Set<Class<? extends CodesTablePeriodicItem<? extends Enum, ?>>> codesTablePeriodicItemClasses = new HashSet<>();

	/**
	 * Uses <code>CodesTableManager</code> to load all of the <code>CodesTable</code>s and <code>CodesTablePeriodic</code>s from
	 * the database into the cache.
	 * 
	 * @throws CodesTableNotFoundException
	 *             if the desired <code>CodesTable</code>/<code>CodesTablePeriodic</code> cannot be retrieved from the database.
	 * @throws CodesTableConfigurationException
	 *             if no <code>CodesTableItem</code>'s or <code>CodesTablePeriodicItem</code>'s has been defined in the
	 *             configuration.
	 */
	@Override
	public void afterPropertiesSet() throws CodesTableNotFoundException, CodesTableConfigurationException {
		if (codesTableItemClasses.isEmpty() && codesTablePeriodicItemClasses.isEmpty()) {
			throw new CodesTableConfigurationException("No CodesTables or CodesTablePeriodics to load have been set");
		}

		preloadingCodesTables();
		preloadingCodesTablePeriodics();
	}

	/**
	 * Preloads the CodesTables.
	 */
	private void preloadingCodesTables() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Preloading " + codesTableItemClasses.size() + " CodesTables");
		}

		for (Class<? extends CodesTableItem> ct : codesTableItemClasses) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Preloading CodesTable: " + ct);
			}
			CodesTable cTable = codesTableManager.getCodesTable(ct);

			if (null == cTable) {
				throw new CodesTableNotFoundException("Codestable does not exist: " + ct);
			}
		}
	}

	/**
	 * Preloads the CodesTablePeriodics.
	 */
	private void preloadingCodesTablePeriodics() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Preloading " + codesTablePeriodicItemClasses.size() + " CodesTablePeriodics");
		}

		for (Class<? extends CodesTablePeriodicItem> ctp : codesTablePeriodicItemClasses) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Preloading CodesTablePeriodic: " + ctp);
			}
			CodesTablePeriodic cTablePeriodic = codesTableManager.getCodesTablePeriodic(ctp);

			if (null == cTablePeriodic) {
				throw new CodesTableNotFoundException("Codestable does not exist: " + ctp);
			}
		}
	}

	/**
	 * Sets the CodesTableManager.
	 * 
	 * @param codesTableManager
	 *            the CodesTableManager.
	 */
	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}

	/**
	 * Sets the set of codestableclasses.
	 * 
	 * @param codesTableItemClasses
	 *            the <code>CodesTableClasses</code> that shall be retrieved from the database.
	 */
	public void setCodesTableItemClasses(Set<Class<? extends CodesTableItem<? extends Enum, ?>>> codesTableItemClasses) {
		this.codesTableItemClasses = codesTableItemClasses;
	}

	/**
	 * Sets the set of codestableperiodicclasses.
	 * 
	 * @param codesTablePeriodicItemClasses
	 *            the <code>CodesTablePeriodicClasses</code> that shall be retrieved from the database.
	 */
	public void setCodesTablePeriodicItemClasses(
			Set<Class<? extends CodesTablePeriodicItem<? extends Enum, ?>>> codesTablePeriodicItemClasses) {
		this.codesTablePeriodicItemClasses = codesTablePeriodicItemClasses;
	}
}