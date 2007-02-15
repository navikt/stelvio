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

public class SaksbehandlerServiceBean extends AbstractStatelessSessionBean implements SaksbehandlerServiceBi {
	private SaksbehandlerServiceBi saksbehandlerService;

	/** {@inheritDoc} */
	public SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws PersonNotFoundException {
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
