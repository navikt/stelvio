package no.stelvio.common.codestable;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.codestable.CodesTableFactory;
import no.stelvio.common.error.SystemException;

/**
 * Implementation of CodesTableManager used for accessing and caching persistent codes tables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableManagerImpl implements CodesTableManager {

	//TODO: er denne nødvendig? Definert i desinget
	//private ArrayList<? extends CodesTable> codesTables;
	
	private CodesTableFactory codesTableFactory;
	
	/**
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
	public <T extends CodesTable> T getCodesTable(Class<T> codesTable) {
		validateCodesTableClass(codesTable);
		
		return codesTableFactory.retrieveCodesTable(codesTable);
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
			throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, codesTableClass + " is not a codestable");
		}
	}	
}