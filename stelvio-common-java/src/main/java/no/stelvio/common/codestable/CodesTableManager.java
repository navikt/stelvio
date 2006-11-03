package no.stelvio.common.codestable;

/**
 * Interface defining functionality for accessing and caching persistent codes tables.
 * The class is not implemented as a Singleton, but should be externally managed
 * as a singleton, e.g. by using Spring Framework.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @author Ragnar Wisløff, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: CodesTableManager.java 2786 2006-02-28 13:24:08Z skb2930 $
 */
public interface CodesTableManager {

	public <T extends CodesTableItem> CodesTable<T> getCodesTable(Class<T> codesTableItem);
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> getCodesTablePeriodic(Class<T> codesTableItem);
}
