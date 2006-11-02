package no.stelvio.common.codestable;

import java.util.List;

/**
 * Interface defining functionality for retrieving a codestable from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTableFactory{
	
	public <T extends CodesTableItem> List<T> retrieveCodesTable(Class<T> codesTable);
}
