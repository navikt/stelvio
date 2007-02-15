package no.stelvio.common.codestable.factory.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableConfigurationException;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.NotCodesTableException;

/**
 * Implementation of CodesTableInitializer for initialization of the codestables and loading them into the cache for
 * the codestables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableInitializer implements InitializingBean {
	// The CodesTableManager
	private CodesTableManager codesTableManager;
	
	// A list of codestableclasses - defined in the application context
	private List<Class<CodesTableItem>> codesTableClasses = new ArrayList<Class<CodesTableItem>>();
	
	// A list of codestableclasses - defined in the application context
	private List<Class<CodesTableItemPeriodic>> codesTablePeriodicClasses = new ArrayList<Class<CodesTableItemPeriodic>>();
	
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
		
		//CodesTableItem's
		for(Class<CodesTableItem> ct : codesTableClasses){			
			validateCodesTableItemClass(ct);
								
			CodesTable cTable = codesTableManager.getCodesTable(ct);

			if(null == cTable){
				throw new CodesTableNotFoundException(ct);
			}
		}

		//CodesTableItemPeriodic's
		for(Class<CodesTableItemPeriodic> ctp : codesTablePeriodicClasses){
			validateCodesTableItemPeriodicClass(ctp);

			CodesTablePeriodic cTablePeriodic = codesTableManager.getCodesTablePeriodic(ctp);
				
			if(null == cTablePeriodic){
				throw new CodesTableNotFoundException(ctp);
			}
		}
	}
	
	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItem</>.
	 * 
	 * @param codesTableItemClass the class to load a codes table for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of <code>CodesTable</code>.
	 */
	private void validateCodesTableItemClass(final Class<CodesTableItem> codesTableItemClass){
	
		if(null!= codesTableItemClass && !CodesTableItem.class.isAssignableFrom(codesTableItemClass)){
			throw new NotCodesTableException(codesTableItemClass);
		}
	}	
	
	/**
	 * Checks that the class to load a codes table for is a subclass of <code>CodesTableItemPeriodic</>.
	 * 
	 * @param codesTableItemPeriodicClass the class to load a codes table for.
	 * @throws NotCodesTableException if the class to load a codes table for is not a subclass of <code>CodesTablePeriodic</code>.
	 */
	private void validateCodesTableItemPeriodicClass(final Class<CodesTableItemPeriodic> codesTableItemPeriodicClass){
		if(null!= codesTableItemPeriodicClass && !CodesTableItemPeriodic.class.isAssignableFrom(codesTableItemPeriodicClass)){
			throw new NotCodesTableException(codesTableItemPeriodicClass);
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
	public void setCodesTableClasses(List<Class<CodesTableItem>> codesTableClasses){
		this.codesTableClasses = codesTableClasses;
	}
	
	/**
	 * Sets the list of codestableperiodicclasses.
	 * 
	 * @param codesTablePeriodicClasses the <code>CodesTablePeriodicClasses</code> that shall be retrieved from the database.
	 */
	public void setCodesTablePeriodicClasses(List<Class<CodesTableItemPeriodic>> codesTablePeriodicClasses){
		this.codesTablePeriodicClasses = codesTablePeriodicClasses;
	}
}