/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
package no.stelvio.service.star.example.henvendelse;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatistikkRequest;
import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatistikkResponse;

public class HenvendelseServiceBean extends AbstractStatelessSessionBean implements HenvendelseServiceBi {
	private HenvendelseServiceBi henvendelseService;

	public HenvendelseStatistikkResponse genererHenvendelseStatistikk(HenvendelseStatistikkRequest henvendelseStatistikkRequest) {
		return henvendelseService.genererHenvendelseStatistikk(henvendelseStatistikkRequest);
	}

	@Override
	protected void onEjbCreate() throws CreateException {
		henvendelseService = (HenvendelseServiceBi) getBeanFactory().getBean("srv.starexample.henvendelseService");
	}

	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("star.example.beanFactory");
	}
}
