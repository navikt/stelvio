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
	 * The <code>CodesTable</code> that the input <code>CodesTableItem</code> belongs to, 
	 * is the <code>CodesTable</code> that shall be retrieved. 
	 * 
	 * @param <T> <code>CodesTableItem</code>'s or subclasses of <code>CodesTableItem</code> that are valid input 
	 * parameters, and that the <code>CodesTable</code> returned by the method holds values of. 
	 * @param codesTableItem the <code>CodesTable</code> to retieve - represented by a <code>CodesTableItem</code>s class.
	 * @return The fetched <code>CodesTable</code>.
	 */
	<T extends CodesTableItem> CodesTable<T> getCodesTable(Class<T> codesTableItem);
	
	/**
	  * Retrieves a <code>CodesTablePeriodic</code> from the database by using <code>CodesTableFactory</code>. 
	  * The <code>CodesTablePeriodic</code> that the input <code>CodesTableItemPeriodic</code> belongs to, 
	  * is the <code>CodesTablePeriodic</code> that shall be retrieved. 
	  * 
	  * @param <T> <code>CodesTableItemPeriodic</code>'s or subclasses of <code>CodesTableItemPeriodic</code> that are valid input 
	  * parameters, and that the <code>CodesTablePeriodic</code> returned by the method holds values of. 
	  * @param codesTableItemPeriodic the <code>CodesTablePeriodic</code> to retieve - represented by a <code>CodesTableItemPeriodic</code>s class.
	  * @return The fetched <code>CodesTablePeriodic</code>.
	  */
	<T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItemPeriodic);
}
