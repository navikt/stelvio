package no.stelvio.service.codestable;

import java.util.List;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.repository.codestable.CodesTableRepository;

/**
 * Bean implementation class for CodesTableItemsFactory.
 */
public class RepositoryCodesTableItemsFactory implements CodesTableItemsFactory {
	private CodesTableRepository codesTableRepository;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem<K, V>, K extends Enum, V>
		List<T> createCodesTableItems(Class<T> codesTableItemClass)
			throws CodesTableNotFoundException {
		return codesTableRepository.findCodesTableItems(codesTableItemClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTablePeriodicItem<K, V>, K extends Enum, V>
		List<T> createCodesTablePeriodicItems(Class<T> codesTableItemPeriodicClass)
			throws CodesTableNotFoundException {
		return codesTableRepository.findCodesTableItems(codesTableItemPeriodicClass);
	}

	/**
	 * Sets the repository used to retrieve codestable from persistent repository
	 * @param codesTableRepository repository to use in thus factory
	 */
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}
