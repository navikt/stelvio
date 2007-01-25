package no.stelvio.service.codestable;

import java.util.List;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
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
	private CodesTableRepository codesTableRepository;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem> CodesTable<T> createCodesTable(Class<T> codesTableItemClass) throws CodesTableNotFoundException {
		final List<T> items = codesTableRepository.findCodesTableItems(codesTableItemClass);

		return new DefaultCodesTable<T>(items);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(Class<T> codesTableItemPeriodicClass) throws CodesTableNotFoundException {
		final List<T> items = codesTableRepository.findCodesTableItems(codesTableItemPeriodicClass);

		return new DefaultCodesTablePeriodic<T>(items);
	}
	
	/**
	 * Sets the repository used to retrieve codestable from persistent repository
	 * @param codesTableRepository repository to use in thus factory
	 */
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}