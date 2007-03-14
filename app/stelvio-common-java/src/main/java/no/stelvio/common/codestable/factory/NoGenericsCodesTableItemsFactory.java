package no.stelvio.common.codestable.factory;

import java.util.List;

import no.stelvio.common.codestable.CodesTableNotFoundException;

/**
 * Business interface for EJB that can't handle Generics in interface.
 * Components that can handle Generics in interface should use {@link CodesTableItemsFactory}
 *
 * @author person983601e0e117(Accenture)
 */
public interface NoGenericsCodesTableItemsFactory {
	/**
	 * Retrieves an instance of CodesTable.
	 *
	 * @param codesTableItemClass
	 * @return a list of <code>CodesTableItem</code>s.
	 * @throws CodesTableNotFoundException if codestable wasn't found in underlying persistence store
	 */
	List createCodesTableItems(Class codesTableItemClass) throws CodesTableNotFoundException;

	/**
	 * Retrieves an instance of CodesTablePeriodic.
	 *
	 * @param codesTablePeriodicItemClass
	 * @return a list of <code>CodesTablePeriodicItem</code>s.
	 * @throws CodesTableNotFoundException if codestable wasn't found in underlying persistence store
	 */
	List createCodesTablePeriodicItems(Class codesTablePeriodicItemClass) throws CodesTableNotFoundException;
}
