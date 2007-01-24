package no.stelvio.service.codestable;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.repository.codestable.CodesTableRepository;

/**
 * Bean implementation class for CodesTableFactory.
 */
public class RepositoryCodesTableFactory implements CodesTableFactory {
	private CodesTableRepository codesTableRepository;
	
	public <T extends CodesTableItem> CodesTable<T> createCodesTable(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return (CodesTable<T>) codesTableRepository.findCodesTableItems(codesTableClass);
	}

	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return (CodesTablePeriodic<T>) codesTableRepository.findCodesTableItems(codesTableClass); 
	}
	
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}