package no.stelvio.common.codestable.factory.support;

import no.stelvio.common.codestable.*;
import no.stelvio.common.codestable.factory.CodesTableInitializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CodesTableInitializer for initialization of the codestables 
 * and loading them into the cache for the codestables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableInitializer implements CodesTableInitializer {
	
	//The CodesTableManager
	private CodesTableManager codesTableManager;
	
	//A list of codestableclasses - defined in the application context
	private List<Class<CodesTableItem>> codesTableClasses = new ArrayList<Class<CodesTableItem>>();
	
	//A list of codestableclasses - defined in the application context
	private List<Class<CodesTableItemPeriodic>> codesTablePeriodicClasses = new ArrayList<Class<CodesTableItemPeriodic>>();
	
	/**
	 * {@inheritDoc}
	 */
	public void init() {
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