package no.stelvio.common.codestable;

import java.util.ArrayList;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;

/**
 * Implementation of CodesTableInitializer for initialization of the codestables 
 * and loading them into the cache for the codestables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableInitializerImpl implements CodesTableInitializer {

	private ArrayList<? extends Class<CodesTable>> codesTableClasses;
	private CodesTableManager codesTableManager;
	
	/**
	 * TODO
	 * @param codesTableManager
	 */
	public void setCodesTableManager(CodesTableManager codesTableManager){
		this.codesTableManager = codesTableManager;
	}
	
	/**
	 * Uses CodesTableManager to load the codestables from the database into the
	 * cache.
	 * @throws CodesTableException 
	 */
	public void init() {
				
		if(codesTableClasses.isEmpty()){
			throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND);
		}
		
		try{
			for(Class<CodesTable> ct : codesTableClasses){
				/*
				CodesTable ctable = codesTableManager.getCodesTable(ct);
				
				if(null == ctable){
					throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, ct);
				}
				*/
			}
		}
		catch(Exception ex){
			throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, ex);
		}
	}
}