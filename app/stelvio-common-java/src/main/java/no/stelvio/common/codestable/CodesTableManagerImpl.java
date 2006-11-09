package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableManagerImpl implements CodesTableManager {

	//The CodesTableFactory used for retrieval of codestables
	private CodesTableFactory codesTableFactory;
	
	//TODO: FIKSE CASTING
	/**
	 * {@inheritDoc CacheTableManager#getCodesTable()}
	 */
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

	/**
	 * {@inheritDoc CacheTableManager#getCodesTablePeriodic()}
	 */
	//TODO: FIKSE CASTING
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItem) {
		CodesTablePeriodic<T> codesTablePeriodic = (CodesTablePeriodic<T>) new CodesTablePeriodicImpl();
		
		validateCodesTablePeriodicClass(codesTableItem);
		
		List<T> codesTableItems = new ArrayList<T>();
		
		codesTableItems = (List<T>) codesTableFactory.retrieveCodesTablePeriodic(codesTableItem);
		
		for(T ctp : codesTableItems){
			codesTablePeriodic.addCodesTableItem(ctp);
		}
				
		return codesTablePeriodic;
	}

	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItem</>.
	 * 
	 * @param codesTableClass the class to load a codestable for
	 * @throws CodesTableException if the class to load a codestable for is not a subclass of <code>CodesTable</code>
	 */
	private void validateCodesTableClass(final Class codesTableClass){
		if(null!= codesTableClass && !CodesTableItem.class.isAssignableFrom(codesTableClass)){
			throw new CodesTableException(codesTableClass + " is not a codestable");
		}
	}	
	
	/**
	 * Checks that the class to load a codestable for is a subclass of <code>CodesTableItemPeriodic</>.
	 * 
	 * @param codesTablePeriodicClass the class to load a codestable for.
	 * @throws CodesTableException if the class to load a codestable for is not a subclass of <code>CodesTablePeriodic</code>.
	 */
	private void validateCodesTablePeriodicClass(final Class codesTablePeriodicClass){
		if(null!= codesTablePeriodicClass && !CodesTableItemPeriodic.class.isAssignableFrom(codesTablePeriodicClass)){
			throw new CodesTableException (codesTablePeriodicClass + " is not a codestable");
		}
	}	
	
	/**
	 * Sets the <code>CodesTableFactory</code> that is used to retrieve 
	 * the codestables from the database.
	 * @param codesTableFactory 
	 */
	public void setCodesTableFactory(CodesTableFactory codesTableFactory){
		this.codesTableFactory = codesTableFactory;
	}
}