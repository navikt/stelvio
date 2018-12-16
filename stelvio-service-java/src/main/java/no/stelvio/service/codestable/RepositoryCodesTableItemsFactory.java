package no.stelvio.service.codestable;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;
import no.stelvio.repository.codestable.CodesTableRepository;

/** Bean implementation class for CodesTableItemsFactory. */
public class RepositoryCodesTableItemsFactory implements CodesTableItemsFactory {

	/**
	 * Cache model id must correspond to a cache definition in ehcache.xml or similar (the ehcache config file, example
	 * cfg-pen-ehcache.xml.).
	 */
	public static final String CACHE_MODEL_ID = "no.stelvio.common.codestable.items";

	private CodesTableRepository codesTableRepository;

	@Override
	@Cacheable(CACHE_MODEL_ID)
	public <T extends AbstractCodesTableItem<K, V>, K extends Enum, V> List<T> createCodesTableItems(
			Class<T> codesTableItemClass) throws CodesTableNotFoundException {
		return codesTableRepository.findCodesTableItems(codesTableItemClass);
	}

	@Override
	@Cacheable(CACHE_MODEL_ID)
	public <T extends AbstractCodesTablePeriodicItem<K, V>, K extends Enum, V> List<T> createCodesTablePeriodicItems(
			Class<T> codesTableItemPeriodicClass) throws CodesTableNotFoundException {
		return codesTableRepository.findCodesTableItems(codesTableItemPeriodicClass);
	}

	/**
	 * Sets the repository used to retrieve codestable from persistent repository.
	 * 
	 * @param codesTableRepository
	 *            repository to use in thus factory
	 */
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}
