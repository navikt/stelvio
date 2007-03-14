/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.provider.star.example.henvendelse;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface HenvendelseServiceLocalHome extends EJBLocalHome {
	HenvendelseServiceLocal create() throws CreateException;
}
