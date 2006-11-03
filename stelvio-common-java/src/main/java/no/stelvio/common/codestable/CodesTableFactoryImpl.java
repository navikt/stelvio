package no.stelvio.common.codestable;

import java.util.List;

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