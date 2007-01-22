package no.stelvio.service.codestable;

import java.util.List;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.repository.codestable.CodesTableRepository;

/**
 * Bean implementation class for CodesTableFactory.
 */
public class RepositoryCodesTableFactory implements CodesTableFactory {
	private CodesTableRepository codesTableRepository;
	
	public <T extends CodesTableItem> List<T> createCodesTable(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return codesTableRepository.fetchCodesTable(codesTableClass); 
	}

	public <T extends CodesTableItemPeriodic> List<T> createCodesTablePeriodic(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return codesTableRepository.fetchCodesTable(codesTableClass); 
	}
	
	public void setCodesTableRepository(CodesTableRepository codesTableRepository) {
		this.codesTableRepository = codesTableRepository;
	}
}