package no.nav.presentation.pensjon.saksbehandling.action;


import no.nav.domain.pensjon.codestable.HenvendelseTypeCti;
import no.nav.presentation.pensjon.saksbehandling.form.StartForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.SaksbehandlerDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.SaksbehandlerService;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;


public class StartAction extends MultiAction {
	private SaksbehandlerService saksbehandlerService;
	private CodesTableManager codesTableManager;
	private static final Log log = LogFactory.getLog(StartAction.class);
	
	SaksbehandlerDO saksbehandler;
	
	public Event executeSearch(RequestContext context)
	{
		log.debug( "--------------executeSearch-----------" );
		log.debug("test: " + codesTableManager.getCodesTablePeriodic(HenvendelseTypeCti.class));
		StartForm form =
            (StartForm)context.getFlowScope().get("startForm");
		log.debug( "form.getSaksbehandlernr(): " + form.getSaksbehandlernr() );
		log.debug( "saksbehandlerService = " + saksbehandlerService );
		try {
			saksbehandler = saksbehandlerService.readSaksbehandler(form.getSaksbehandlernr());
		} catch (PersonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug( "saksbehandler = " + saksbehandler );
		//context.getFlowScope().put( "saksbehandlerDO", saksbehandler);
		context.getExternalContext().getSessionMap().put("saksbehandlerDO", saksbehandler);
		
		log.debug( "-------------- end executeSearch-----------" );
		return success();
	}

	public SaksbehandlerService getSaksbehandlerService() {
		return saksbehandlerService;
	}

	public void setSaksbehandlerService(SaksbehandlerService saksbehandlerService) {
		this.saksbehandlerService = saksbehandlerService;
	}

	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}
	
	
}
