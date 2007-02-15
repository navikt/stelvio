/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.service.star.example.henvendelse;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface HenvendelseServiceLocalHome extends EJBLocalHome {
	no.stelvio.service.star.example.henvendelse.HenvendelseServiceLocal create() throws CreateException;
}
