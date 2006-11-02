package no.stelvio.common.codestable;

import java.util.List;

/**
 * Interface for retrieval of a <code>CodesTable</code>.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public interface CodesTableRetriever {
	public <T extends CodesTableItem> List<T> retrieve(Class<T> codesTable);
}
