package no.stelvio.common.codestable;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;

/**
 * Implementation of CodesTableFactory used to retrieve a codestable from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableFactoryImpl implements CodesTableFactory {
	
	// The business delegate
	//private CodesTableRetriever codesTableRetriever = new CodesTableRetrieverImpl();
	
	// Name of the component
	private String name = "";

	/**
	 * TODO
	 * @param codesTableRetriever
	 */
	public void setCodesTableRetriever(CodesTableRetriever codesTableRetriever) {
		//this.codesTableRetriever = codesTableRetriever;
	}

	/**
	 * Assigns the name of this component. The name is only used in error reporting.
	 * 
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * TODO: document me!
	 * @param codesTable a <code>CodesTable</code> or <CodesTablePeriodic</> 
	 * @return T <code>CodesTable</code> or <CodesTablePeriodic</>
	 * @throws CodesTableException
	 */
	public <T extends CodesTable> T retrieveCodesTable(Class<T> codesTable) {
		
		/**
		if (null == codesTableRetriever) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("No delegate has been set for retrieving codes tables."),
				name);
		}
		*/
					
		//T ct = codesTableRetriever.retrieve(codesTable);
			
		/*
		if (null == ct) {
			System.out.println("Systems exception");
			throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, codesTable.getName());
		}
	
		return ct;
		*/
		return null;
	}
}