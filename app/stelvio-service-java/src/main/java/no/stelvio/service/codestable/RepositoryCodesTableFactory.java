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
	
	@SuppressWarnings("unchecked")
	public <T extends CodesTableItem> CodesTable<T> createCodesTable(Class<T> codesTableClass) throws CodesTableNotFoundException {
		List<T> items = codesTableRepository.findCodesTableItems(codesTableClass);
		
		return new DefaultCodesTable<T>(items); 
	}

	@SuppressWarnings("unchecked")
	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(Class<T> codesTableClass) throws CodesTableNotFoundException {
		List<T> items = codesTableRepository.findCodesTableItems(codesTableClass);

		return new DefaultCodesTablePeriodic<T>(items); 
	}
	
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}