package no.stelvio.common.codestable;

/**
 * Interface defining functionality for initialization of <code>CodeTables</code> and
 * loading them into the cache.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTableInitializer {

	/**
	 * Uses CodesTableManager to load all of the <code>CodesTable</code>s and 
	 * <code>CodesTablePeriodic</code>s from the database into the
	 * cache.
	 * 
	 * @throws CodesTableNotFoundException thrown if the desired <code>CodesTable</code>/
	 * <code>CodesTablePeriodic</code> cannot be retrieved from the database.
	 * 
	 * @throws CodesTableConfigurationException thrown if there haven't been defined 
	 * any <code>CodesTableItem</code>'s or <code>CodesTableItemPeriodic</code> in 
	 * the configuration, used to load the application's needed <code>CodesTable</code>'s 
	 * and <code>CodesTablePeriodic</code>. 
	 */
	void init() throws CodesTableConfigurationException, CodesTableNotFoundException;
}