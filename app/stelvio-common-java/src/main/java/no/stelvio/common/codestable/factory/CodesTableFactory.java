package no.stelvio.common.codestable.factory;

import org.springmodules.cache.annotations.Cacheable;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;

/**
 * Interface defining functionality for creating codes table items. Must be implemented by the project using the codes
 * table API.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @deprecated Use {@link no.stelvio.common.codestable.factory.CodesTableItemsFactory} instead.
 */
@Deprecated
public interface CodesTableFactory{
	static final String CACHE_MODEL_ID = "no.stelvio.common.codestable.deprecated";
	
	/**
	 * Creates a <code>CodesTable</code> containing a list of <code>CodesTableItem</code>s.
	 * 
	 * @param codesTableItemClass the subclass of type <code>CodesTableItem</code> that will be part of the <code>CodesTable</code> 
	 * the items shall be retrieved from.
	 * @return <code>CodesTable</code> containing a list of <code>CodesTableItem</code>s type of <T> 
	 * @throws CodesTableNotFoundException thrown when the codes table which have the list of
	 * <code>CodesTableItem</code>s is not found.
	 */
	@Cacheable(modelId = CACHE_MODEL_ID)
	<T extends CodesTableItem<K, V>, K extends Enum, V>
		CodesTable<T, K, V> createCodesTable(Class<T> codesTableItemClass) throws CodesTableNotFoundException;
	
	/**
	 * Retrieves a <code>CodesTablePeriodic</code> containing a list of <code>CodesTablePeriodicItem</code>s.
	 * 
	 * @param codesTablePeriodicItemClass the subclass of type <code>CodesTablePeriodicItem</code> that will be part of the <code>CodesTablePeriodic</code>
	 * the items shall be retrieved from.
	 * @return <code>CodesTablePeriodic</code> containing a list of <code>CodesTablePeriodicItem</code>s type of <T> 
	 * @throws CodesTableNotFoundException thrown when the codes table which have the list of
	 * <code>CodesTableItem</code>s is not found.
	 */
	@Cacheable(modelId = CACHE_MODEL_ID)
	<T extends CodesTablePeriodicItem<K, V>, K extends Enum, V>
		CodesTablePeriodic<T, K, V> createCodesTablePeriodic(Class<T> codesTablePeriodicItemClass)
			throws CodesTableNotFoundException;
}