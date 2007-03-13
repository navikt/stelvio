package no.stelvio.common.codestable.factory;

import org.springmodules.cache.annotations.Cacheable;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;

/**
 * Interface defining functionality for retrieving a <code>CodesTable</code> from the database.
 * Must be implemented by the EJB that has responsibility for fetching the codestables
 * from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTableFactory{
	
	static final String CACHE_MODEL_ID = "no.stelvio.common.codestable";
	
	/**
	 * Retrieves a <code>CodesTable</code> containing a list of <code>CodesTableItem</code>s.
	 * 
	 * @param <T> A concrete implementation of <code>CodesTableItem</code> 
	 * 
	 * @param codesTableItemClass the subclass of type <code>CodesTableItem</code> that will be part of the <code>CodesTable</code> 
	 * the items shall be retrieved from.
	 * @return <code>CodesTable</code> containing a list of <code>CodesTableItem</code>s type of <T> 
	 * @throws no.stelvio.common.codestable.CodesTableNotFoundException - exception thrown when a <code>CodesTable</code> couldn't be retrieved.
	 */
	@Cacheable(modelId = CACHE_MODEL_ID)
	<T extends CodesTableItem<K, V>, K extends Enum, V>
		CodesTable<T, K, V> createCodesTable(Class<T> codesTableItemClass) throws CodesTableNotFoundException;
	
	/**
	 * Retrieves a <code>CodesTablePeriodic</code> containing a list of <code>CodesTableItemPeriodic</code>s.
	 * 
	 * @param <T> A concrete implementation of <code>CodesTableItem</code> 
	 * 
	 * @param codesTableItemPeriodicClass the subclass of type <code>CodesTableItemPeriodic</code> that will be part of the <code>CodesTablePeriodic</code> 
	 * the items shall be retrieved from.
	 * @return <code>CodesTablePeriodic</code> containing a list of <code>CodesTablePeriodicItem</code>s type of <T> 
	 * @throws no.stelvio.common.codestable.CodesTableNotFoundException - exception thrown when a <code>CodesTablePeriodic</code> couldn't be retrieved.
	 */
	@Cacheable(modelId = CACHE_MODEL_ID)
	<T extends CodesTableItemPeriodic<K, V>, K extends Enum, V>
		CodesTablePeriodic<T, K, V> createCodesTablePeriodic(Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException;
}