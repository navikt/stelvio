package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.error.SystemException;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableManagerImpl implements CodesTableManager {

	private CodesTableFactory codesTableFactory;
	
	/**
	 * TODO
	 * @param codesTableFactory 
	 */
	public void setCodesTableFactory(CodesTableFactory codesTableFactory){
		this.codesTableFactory = codesTableFactory;
	}
		
	/**
	 * Uses CodesTableFactory to fetch the codestable from the database.
	 * 
	 * @param codesTable the <code>CodesTable</code> or <code>CodesTablePeriodic</code> to fetch from either cache or database.
	 * @return codesTable the fetched <code>CodesTable</code> or <code>CodesTablePeriodic</code>.
	 */
	//TODO: FIKSE CASTING
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem> CodesTable<T> getCodesTable(Class<T> codesTableItem) {
		CodesTable<T> codesTable = (CodesTable<T>) new CodesTableImpl();
		
		validateCodesTableClass(codesTableItem);
		
		List<T> codesTableItems = new ArrayList<T>();
		
		codesTableItems = (List<T>) codesTableFactory.retrieveCodesTable(codesTableItem);
		
		for(T ct : codesTableItems){
			codesTable.addCodesTableItem(ct);
		}
				
		return codesTable;
		
	}

	//TODO: FIKSE CASTING
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItem) {
		CodesTablePeriodic<T> codesTablePeriodic = (CodesTablePeriodic<T>) new CodesTablePeriodicImpl();
		
		validateCodesTableClass(codesTableItem);
		
		List<T> codesTableItems = new ArrayList<T>();
		
		codesTableItems = (List<T>) codesTableFactory.retrieveCodesTablePeriodic(codesTableItem);
		
		
/*		for(T ctp : codesTableItems){
			codesTablePeriodic.addCodesTableItem(ctp);
		}*/
				
		return codesTablePeriodic;
	}

	/**
	 * Checks that the class to load a codes table for is a subclass of <code>CodesTableItem</>.
	 * If not, an exception wil be thrown.
	 * 
	 * @param codesTableClass the class to load a codes table for.
	 * @throws SystemException if the class to load a codes table for is not a subclass of <code>CodesTable</code>.
	 */
	private void validateCodesTableClass(final Class codesTableClass){
		if(null!= codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)){
			throw new CodesTableException(codesTableClass + " is not a codestable");
		}
	}	
}