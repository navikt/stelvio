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
	 * @throws CodesTableException thrown if a <code>CodesTable</code> or a 
	 * <code>CodesTablePeriodic</code> that is defined in Springs application context 
	 * used to retrieve the tables from the database - isn't a class of 
	 * either <code>CodesTableItem</code> or <code>CodesTableItemPeriodic</code>. 
	 * This exception is also thrown if the desired <code>CodesTable</code>/
	 * <code>CodesTablePeriodic</code> cannot be retrieved from the database.
	 */
	public void init() throws CodesTableException;
}