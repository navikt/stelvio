/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.service.star.example.saksbehandling;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SaksbehandlerServiceLocalHome extends EJBLocalHome {
	no.stelvio.service.star.example.saksbehandling.SaksbehandlerServiceLocal create() throws CreateException;
}
