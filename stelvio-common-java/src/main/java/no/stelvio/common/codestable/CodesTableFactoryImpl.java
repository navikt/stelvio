package no.stelvio.common.codestable;

import java.util.List;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;

/**
 * Implementation of CodesTableFactory used to retrieve a codestable from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableFactoryImpl implements CodesTableFactory {
	
	public <T extends CodesTableItemPeriodic> List<T> retrieveCodesTablePeriodic(Class<T> codesTable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T extends CodesTableItem> List<T> retrieveCodesTable(Class<T> codesTable) {
		// TODO Auto-generated method stub
		return null;
	}
}