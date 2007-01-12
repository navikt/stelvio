package no.nav.service.pensjon;

import java.util.Collections;
import java.util.List;

import javax.ejb.CreateException;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.factory.CodesTableFactory;

import org.springframework.ejb.support.AbstractStatelessSessionBean;

/**
 * Bean implementation class for CodesTableFactory.
 */
public class DefaultCodesTableFactory implements CodesTableFactory {
	public <T extends CodesTableItem> List<T> retrieveCodesTable(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return Collections.EMPTY_LIST;
	}

	public <T extends CodesTableItemPeriodic> List<T> retrieveCodesTablePeriodic(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return Collections.EMPTY_LIST;
	}
}