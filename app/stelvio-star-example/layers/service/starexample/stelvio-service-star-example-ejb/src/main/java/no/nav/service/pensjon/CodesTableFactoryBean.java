package no.nav.service.pensjon;

import java.util.List;

import javax.ejb.CreateException;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.factory.CodesTableFactory;

import org.springframework.ejb.support.AbstractStatelessSessionBean;

/**
 * Bean implementation class for Session Bean: CodesTableFactory
 *
 * @ejb.bean
 *	name="CodesTableFactory"
 *	type="Stateless"
 *	local-jndi-name="ejb/no/nav/service/pensjon/CodesTableFactoryLocalHome"
 *	view-type="local"
 *	transaction-type="Container"
 *
 * @ejb.home
 *	local-class="no.nav.service.pensjon.CodesTableFactoryLocalHome"
 *
 * @ejb.interface
 *	local-class="no.nav.service.pensjon.CodesTableFactoryLocal"
 *  local-extends="no.stelvio.common.codestable.factory.CodesTableFactory, javax.ejb.EJBLocalObject"
 */
public class CodesTableFactoryBean extends AbstractStatelessSessionBean implements CodesTableFactory {
	private CodesTableFactory codesTableFactory;
	
	@Override
	protected void onEjbCreate() throws CreateException {
		// TODO what should the name of the bean be for stelvio components?
		codesTableFactory = (CodesTableFactory) getBeanFactory().getBean("frm.codesTableFactory", CodesTableFactory.class);		
	}

	public <T extends CodesTableItem> List<T> retrieveCodesTable(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return codesTableFactory.retrieveCodesTable(codesTableClass);
	}

	public <T extends CodesTableItemPeriodic> List<T> retrieveCodesTablePeriodic(Class<T> codesTableClass) throws CodesTableNotFoundException {
		return codesTableFactory.retrieveCodesTablePeriodic(codesTableClass);
	}
}