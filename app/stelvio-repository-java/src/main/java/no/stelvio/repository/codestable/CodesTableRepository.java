package no.stelvio.repository.codestable;

import java.util.List;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

public interface CodesTableRepository {
	
	/**
	 * Retrieves all codestable items specified by the CodesTableItem class
	 * @param codestableItem AbstractCodesTableItem implementation that may be instantiated (non-abstract)
	 * @return list of specified CodesTableItem
	 */
	List findCodesTableItems(Class<AbstractCodesTableItem>  codestableItem);
}
