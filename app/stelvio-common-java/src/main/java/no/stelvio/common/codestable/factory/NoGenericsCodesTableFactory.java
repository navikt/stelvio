package no.stelvio.common.codestable.factory;

import no.stelvio.common.codestable.CodesTableNotFoundException;

/**
 * Business interface for EJB that can't handle Generics in interface. 
 * Components that can handle Generics in interface should use {@link CodesTableFactory}
 * @author person983601e0e117(Accenture)
 * 
 *
 */
public interface NoGenericsCodesTableFactory {

	/**
	 * Retrieves an instance of CodesTable
	 * @param codesTableItemClass
	 * @return object reference to a CodesTable
	 * @throws CodesTableNotFoundException if codestable wasn't found in underlying persistence store
	 */
	Object createCodesTable(Class codesTableItemClass)
	throws CodesTableNotFoundException;

	/**
	 * Retrieves an instance of CodesTablePeriodic
	 * @param codesTableItemClass
	 * @return object reference to a CodesTablePeriodic
	 * @throws CodesTableNotFoundException if codestable wasn't found in underlying persistence store
	 */
	public Object createCodesTablePeriodic(Class codesTableItemPeriodicClass)
	throws CodesTableNotFoundException;	
	
	
}
