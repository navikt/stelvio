package no.nav.presentation.pensjon.saksbehandling.action;


import no.nav.presentation.pensjon.saksbehandling.form.StartForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.SaksbehandlerDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.SaksbehandlerService;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;

import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;


public class StartAction extends MultiAction {
	private SaksbehandlerService saksbehandlerService;
	
	SaksbehandlerDO saksbehandler;
	
	public Event executeSearch(RequestContext context)
	{
		System.out.println( "--------------executeSearch-----------" );
		
		StartForm form =
            (StartForm)context.getFlowScope().get("startForm");
		System.out.println( "form.getSaksbehandlernr(): " + form.getSaksbehandlernr() );
		System.out.println( "saksbehandlerService = " + saksbehandlerService );
		try {
			saksbehandler = saksbehandlerService.readSaksbehandler(form.getSaksbehandlernr());
		} catch (PersonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println( "saksbehandler = " + saksbehandler );
		//context.getFlowScope().put( "saksbehandlerDO", saksbehandler);
		context.getExternalContext().getSessionMap().put("saksbehandlerDO", saksbehandler);
		
		System.out.println( "-------------- end executeSearch-----------" );
		return success();
	}

	public SaksbehandlerService getSaksbehandlerService() {
		return saksbehandlerService;
	}

	public void setSaksbehandlerService(SaksbehandlerService saksbehandlerService) {
		this.saksbehandlerService = saksbehandlerService;
	}


	
	
	
	
}
