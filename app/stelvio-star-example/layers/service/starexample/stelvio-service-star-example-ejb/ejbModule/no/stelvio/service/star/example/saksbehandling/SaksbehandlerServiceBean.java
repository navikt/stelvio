/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.service.star.example.saksbehandling;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerRequest;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerResponse;

/**
 * EJB implementation of the <code>SaksbehandlerServiceBi</code> interface which acts as a thin facade over the POJO
 * containing the business logic.
 *
 * @author personf8e9850ed756, Accenture
 */
public class SaksbehandlerServiceBean extends AbstractStatelessSessionBean implements SaksbehandlerServiceBi {
	private SaksbehandlerServiceBi saksbehandlerService;

	/** {@inheritDoc} */
	public SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws SaksbehandlerNotFoundException {
		return saksbehandlerService.hentSaksbehandler(request);
	}

	/** {@inheritDoc} */
	@Override
	protected void onEjbCreate() throws CreateException {
		saksbehandlerService = (SaksbehandlerServiceBi) getBeanFactory().getBean("srv.starexample.saksbehandlerService");
	}

	/** {@inheritDoc} */
	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("star.example.beanFactory");
	}
}
