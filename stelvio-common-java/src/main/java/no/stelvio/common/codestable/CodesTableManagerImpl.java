package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.List;

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
	
	private CodesTable codesTable;
	
	/**
	 * TODO
	 * @param codesTableFactory 
	 */
	public void setCodesTableFactory(CodesTableFactory codesTableFactory){
		this.codesTableFactory = codesTableFactory;
	}
	
	/**
	 * TODO
	 * @param codesTableFactory 
	 */
	public void setCodesTable(CodesTable codesTable){
		this.codesTable = codesTable;
	}
	
	/**
	 * Uses CodesTableFactory to fetch the codestable from the database.
	 * 
	 * @param codesTable the <code>CodesTable</code> or <code>CodesTablePeriodic</code> to fetch from either cache or database.
	 * @return codesTable the fetched <code>CodesTable</code> or <code>CodesTablePeriodic</code>.
	 */
	public <T extends CodesTable> T getCodesTable(Class<? extends CodesTableItem> codesTable) {
		validateCodesTableClass(codesTable);
		
		ArrayList<CodesTableItem> codesTableItems = new ArrayList<CodesTableItem>();
		
		codesTableItems = (ArrayList<CodesTableItem>) codesTableFactory.retrieveCodesTable(codesTable);
		
		for(CodesTableItem ct : codesTableItems){
			codesTable.addCodesTableItem(ct);
		}
		
		return codesTable;
		
		for(Class<CodesTable> ct : codesTableClasses){
			CodesTable ctable = codesTableManager.getCodesTable(ct);
			
			if(null == ctable){
				throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, ct);
			}
		}
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