package no.stelvio.common.codestable.factory;

import java.util.List;

import no.stelvio.common.codestable.CodesTableNotFoundException;

/**
 * Business interface for EJB that can't handle Generics in interface.
 * Components that can handle Generics in interface should use {@link CodesTableItemsFactory}
 *
 */
public interface NoGenericsCodesTableItemsFactory {
	/**
	 * Retrieves an instance of CodesTable.
	 *
	 * @param codesTableItemClass a codesTableItemClass object
	 * @return a list of <code>CodesTableItem</code>s.
	 * @throws CodesTableNotFoundException if codestable wasn't found in underlying persistence store
	 */
	List createCodesTableItems(Class codesTableItemClass) throws CodesTableNotFoundException;

	/**
	 * Retrieves an instance of CodesTablePeriodic.
	 *
	 * @param codesTablePeriodicItemClass a codesTablePeriodicItemClass object
	 * @return a list of <code>CodesTablePeriodicItem</code>s.
	 * @throws CodesTableNotFoundException if codestable wasn't found in underlying persistence store
	 */
	List createCodesTablePeriodicItems(Class codesTablePeriodicItemClass) throws CodesTableNotFoundException;
}
