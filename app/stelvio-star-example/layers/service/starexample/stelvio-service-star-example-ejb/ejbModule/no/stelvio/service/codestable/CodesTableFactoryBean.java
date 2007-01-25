package no.stelvio.service.codestable;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;

/**
 * Bean implementation class for Session Bean: CodesTableFactory
 */
public class CodesTableFactoryBean extends AbstractStatelessSessionBean implements CodesTableFactory {
	private CodesTableFactory codesTableFactory;
	
	@Override
	protected void onEjbCreate() throws CreateException {
		// TODO what should the name of the bean be for stelvio components?
		codesTableFactory = (CodesTableFactory) getBeanFactory().getBean("srv.starexample.codesTableFactory", CodesTableFactory.class);
	}

	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("henvendelseService.henvendelseServiceBeanFactory");
	}
	
	public <T extends CodesTableItem> CodesTable<T> createCodesTable(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return codesTableFactory.createCodesTable(codesTableClass);
	}

	public <T extends CodesTableItemPeriodic> CodesTablePeriodic<T> createCodesTablePeriodic(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return codesTableFactory.createCodesTablePeriodic(codesTableClass);
	}
}