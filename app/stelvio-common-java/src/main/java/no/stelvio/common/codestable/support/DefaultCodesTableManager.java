package no.stelvio.common.codestable.support;

import no.stelvio.common.codestable.*;
import no.stelvio.common.codestable.factory.CodesTableFactory;

import java.util.List;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableManager implements CodesTableManager {

	//The CodesTableFactory used for retrieval of codestables
	private CodesTableFactory codesTableFactory;
	
	/**
	 * {@inheritDoc}
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem> CodesTable<T> getCodesTable(Class<T> codesTableItem) {
        validateCodesTableClass(codesTableItem);

        CodesTable<T> codesTable = (CodesTable<T>) new DefaultCodesTable();
        List<T> codesTableItems = codesTableFactory.createCodesTable(codesTableItem);

        for(T ct : codesTableItems){
			codesTable.addCodesTableItem(ct);
		}
		
		return codesTable;
	}

	/**
	 * {@inheritDoc}
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItem) {
		validateCodesTablePeriodicClass(codesTableItem);
		
		CodesTablePeriodic<T> codesTablePeriodic = (CodesTablePeriodic<T>) new DefaultCodesTablePeriodic();
        List<T> codesTableItems = codesTableFactory.createCodesTablePeriodic(codesTableItem);

        for(T ctp : codesTableItems){
			codesTablePeriodic.addCodesTableItem(ctp);
		}
				
		return codesTablePeriodic;
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItem</>.
	 * 
	 * @param codesTableClass the class to load a codestable for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of <code>CodesTable</code>
	 */
	private void validateCodesTableClass(final Class codesTableClass){
		if(null!= codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)){
			throw new NotCodesTableException(codesTableClass);
		}
	}	
	
	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItemPeriodic</>.
	 * 
	 * @param codesTablePeriodicClass the class to load a codestable for.
	 * @throws NotCodesTableException if the class to load a codestable for is not a subclass of <code>CodesTablePeriodic</code>.
	 */
	private void validateCodesTablePeriodicClass(final Class codesTablePeriodicClass){
		if(null!= codesTablePeriodicClass && !CodesTableItemPeriodic.class.isAssignableFrom(codesTablePeriodicClass)){
			throw new NotCodesTableException (codesTablePeriodicClass);
		}
	}	
	
	/**
	 * Sets the <code>CodesTableFactory</code> that is used to retrieve 
	 * the codestables from the database.
	 * @param codesTableFactory a reference to the interface for retrieving codestables from the database.
	 */
	public void setCodesTableFactory(CodesTableFactory codesTableFactory){
		this.codesTableFactory = codesTableFactory;
	}
}