package no.stelvio.common.codestable;

/**
 * Interface defining functionality for retrieving a codestable from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTableFactory{
	
	public <T extends CodesTable> T retrieveCodesTable(Class<T> codestable) ;
	
}
