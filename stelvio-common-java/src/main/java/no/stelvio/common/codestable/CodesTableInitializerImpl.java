package no.stelvio.common.codestable;

import java.util.ArrayList;

/**
 * Implementation of CodesTableInitializer for initialization of the codestables 
 * and loading them into the cache for the codestables.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableInitializerImpl implements CodesTableInitializer {

	private ArrayList<? extends CodesTable> codesTables;
	
	/**
	 * Uses CodesTableManager to load the codestables from the database into the
	 * cache.
	 * @throws CodesTableException 
	 */
	public void init() {
				
		if(codesTables.isEmpty()){
			//throw new CodesTableException();
		}
		
		/*
		try{
			for(CodesTable ct : codesTables){
				//CodesTableManager.getCodesTable(ct);
				
				if( == null){
					//throw new CodesTableException(); throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, codesTableName);
				}
			}
		}
		catch(Exception ex){
			//throw new CodesTableException(); throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, sfe);
		}
		*/
	}
}