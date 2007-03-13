package no.stelvio.common.codestable.factory.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableConfigurationException;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.NotCodesTableException;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;

/**
 * Initializes the specified codes tables and loads them into their cache.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableInitializer implements InitializingBean {
	private static final Log log = LogFactory.getLog(DefaultCodesTableInitializer.class);

	/** The CodesTableManager */
	private CodesTableManager codesTableManager;

	/** A list of codestableclasses - defined in the application context */
	private List<Class<? extends CodesTableItem<? extends Enum, ?>>> codesTableClasses =
			new ArrayList<Class<? extends CodesTableItem<? extends Enum, ?>>>();
	/** A list of codestableperiodicclasses - defined in the application context */
	private List<Class<? extends CodesTableItemPeriodic<? extends Enum, ?>>> codesTablePeriodicClasses =
			new ArrayList<Class<? extends CodesTableItemPeriodic<? extends Enum, ?>>>();

	/**
	 * Uses CodesTableManager to load all of the <code>CodesTable</code>s and <code>CodesTablePeriodic</code>s from
	 * the database into the cache.
	 *
	 * @throws CodesTableNotFoundException if the desired <code>CodesTable</code>/<code>CodesTablePeriodic</code>
	 * cannot be retrieved from the database.
	 * @throws no.stelvio.common.codestable.CodesTableConfigurationException if there haven't been defined
	 * any <code>CodesTableItem</code>'s or <code>CodesTableItemPeriodic</code> in the configuration, used to load
	 * the application's needed <code>CodesTable</code>'s and <code>CodesTablePeriodic</code>.
	 */
	public void afterPropertiesSet() throws CodesTableNotFoundException, CodesTableConfigurationException {
		if(codesTableClasses.isEmpty() && codesTablePeriodicClasses.isEmpty()){
			throw new CodesTableConfigurationException("No CodesTables or CodesTablePeriodics have been set");
		}

		if (log.isInfoEnabled()) {
			log.info("Preloading " + codesTableClasses.size() + " CodesTable classes");
		}                                                                   

		//CodesTableItem's
		for(Class ct : codesTableClasses){
			validateCodesTableItemClass(ct);
								
			CodesTable cTable = codesTableManager.getCodesTable(ct);

			if(null == cTable){
				throw new CodesTableNotFoundException(ct);
			}
		}

		if (log.isInfoEnabled()) {
			log.info("Preloading " + codesTablePeriodicClasses.size() + " CodesTablePeriodic classes");
		}

		//CodesTableItemPeriodic's
		for(Class ctp : codesTablePeriodicClasses){
			validateCodesTableItemPeriodicClass(ctp);

			CodesTablePeriodic cTablePeriodic = codesTableManager.getCodesTablePeriodic(ctp);

			if(null == cTablePeriodic){
				throw new CodesTableNotFoundException(ctp);
			}
		}
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItemPeriodic</>.
	 *
	 * @param codesTablePeriodicClass the class to load a codestable for.
	 * @throws no.stelvio.common.codestable.NotCodesTableException if the class to load a codestable for is not a subclass of
	 * <code>CodesTablePeriodic</code>.
	 */
	private void validateCodesTableItemPeriodicClass(final Class<? extends AbstractCodesTableItem<?, ?>> codesTablePeriodicClass) {
		if (null != codesTablePeriodicClass && !CodesTableItemPeriodic.class.isAssignableFrom(codesTablePeriodicClass)) {
			throw new NotCodesTableException(codesTablePeriodicClass);
		}
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItem</>.
	 *
	 * @param codesTableClass the class to load a codestable for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of <code>CodesTable</code>
	 */
	private void validateCodesTableItemClass(final Class codesTableClass) {
		if (null != codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)) {
			throw new NotCodesTableException(codesTableClass);
		}
	}
	
	/**
	 * Sets the CodesTableManager.
	 * 
	 * @param codesTableManager the CodesTableManager.
	 */
	public void setCodesTableManager(CodesTableManager codesTableManager){
		this.codesTableManager = codesTableManager;
	}
	
	/**
	 * Sets the list of codestableclasses.
	 * 
	 * @param codesTableClasses the <code>CodesTableClasses</code> that shall be retrieved from the database.
	 */
	public void setCodesTableClasses(List<Class<? extends CodesTableItem<? extends Enum, ?>>> codesTableClasses){
		this.codesTableClasses = codesTableClasses;
	}
	
	/**
	 * Sets the list of codestableperiodicclasses.
	 * 
	 * @param codesTablePeriodicClasses the <code>CodesTablePeriodicClasses</code> that shall be retrieved from the database.
	 */
	public void setCodesTablePeriodicClasses(List<Class<? extends CodesTableItemPeriodic<? extends Enum, ?>>> codesTablePeriodicClasses){
		this.codesTablePeriodicClasses = codesTablePeriodicClasses;
	}
}