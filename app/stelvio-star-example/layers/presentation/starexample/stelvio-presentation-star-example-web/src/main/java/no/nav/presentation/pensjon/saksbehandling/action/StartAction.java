package no.nav.presentation.pensjon.saksbehandling.action;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import no.nav.presentation.pensjon.saksbehandling.form.StartForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.SaksbehandlerDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.SaksbehandlerService;

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
			
			System.out.println( "saksbehandler = " + saksbehandler );
			//context.getFlowScope().put( "saksbehandlerDO", saksbehandler);
			context.getExternalContext().getSessionMap().put("saksbehandlerDO", saksbehandler);
		}
		catch (PersonNotFoundException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage( "Fant ingen saksbehandler med nr " + form.getSaksbehandlernr(), "nei da igjen" );
			facesContext.addMessage(null, msg);
			System.err.println(e);
			return error();
		}
		catch (DatabaseNotFoundException e) {
			System.err.println(e);
		}
		
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
