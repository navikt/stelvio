package no.stelvio.service.codestable;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.common.codestable.support.DefaultCodesTable;
import no.stelvio.common.codestable.support.DefaultCodesTablePeriodic;
import no.stelvio.repository.codestable.CodesTableRepository;

/**
 * Bean implementation class for CodesTableFactory.
 */
public class RepositoryCodesTableFactory implements CodesTableFactory {
	private Log log = LogFactory.getLog(RepositoryCodesTableFactory.class);
	private CodesTableRepository codesTableRepository;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem<K, V>, K extends Enum, V> CodesTable<T, K, V> createCodesTable(Class<T> codesTableItemClass)
			throws CodesTableNotFoundException {
		log.warn("DEPRECATED - Use RepositoryCodesTableItemsFactory instead - DEPRECATED");
		final List<T> items = codesTableRepository.findCodesTableItems(codesTableItemClass);

		return new DefaultCodesTable<T, K, V>(items);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTablePeriodicItem<K, V>, K extends Enum, V> CodesTablePeriodic<T, K, V>
	                                       createCodesTablePeriodic(Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException {
		log.warn("DEPRECATED - Use RepositoryCodesTableItemsFactory instead - DEPRECATED");
		final List<T> items = codesTableRepository.findCodesTableItems(codesTableItemPeriodicClass);

		return new DefaultCodesTablePeriodic<T, K, V>(items);
	}
	
	/**
	 * Sets the repository used to retrieve codestable from persistent repository.
	 * 
	 * @param codesTableRepository repository to use in thus factory
	 */
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}