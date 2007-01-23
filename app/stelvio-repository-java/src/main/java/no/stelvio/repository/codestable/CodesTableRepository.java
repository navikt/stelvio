package no.stelvio.repository.codestable;

import java.util.List;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.repository.codestable.support.HibernateCodesTableRepository;

/**
 * Interface for the repository used to retrieve codestable items
 * 
 * @author person983601e0e117 (Accenture)
 * 
 * @see HibernateCodesTableRepository
 *
 */
public interface CodesTableRepository {
	
	/**
	 * Retrieves all codestable items specified by the CodesTableItem class
	 * @param codestableItem AbstractCodesTableItem implementation that may be instantiated (non-abstract)
	 * @return list of specified CodesTableItem
	 */
	<T extends AbstractCodesTableItem> List findCodesTableItems(Class<T>  codestableItem);
}
