/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.service.star.example.error;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface ErrorDefinitionRetrieverLocalHome extends EJBLocalHome {
	ErrorDefinitionRetrieverLocal create() throws CreateException;
}
