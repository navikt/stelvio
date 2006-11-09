package no.stelvio.common.codestable;

/** * 
 * Interface defining functionality for accessing and caching persistent codes tables.
 * The class is not implemented as a Singleton, but should be externally managed
 * as a singleton, e.g. by using Spring Framework.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id $
 */
public interface CodesTableManager {

	/**
	 * Retrieves a <code>CodesTable</code> from the database by using <code>CodesTableFactory</code>.
	 * 
	 * @param codesTableItem the <code>CodesTable</code> to retieve - represented by a <code>CodesTableItem</code>s class.
	 * @return The fetched <code>CodesTable</code>.
	 */
	public <T extends CodesTableItem> CodesTable<T> getCodesTable(Class<T> codesTableItem);
	
	/**
	  * Retrieves a <code>CodesTablePeriodic</code> from the database by using <code>CodesTableFactory</code>.
	 * 
	 * @param codesTableItemPeriodic the <code>CodesTablePeriodic</code> to retieve - represented by a <code>CodesTableItemPeriodic</code>s class.
	 * @return The fetched <code>CodesTablePeriodic</code>
	 */
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItemPeriodic);
}
