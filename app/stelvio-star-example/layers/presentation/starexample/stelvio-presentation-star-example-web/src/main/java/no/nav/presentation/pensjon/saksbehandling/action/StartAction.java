package no.nav.presentation.pensjon.saksbehandling.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import no.nav.domain.pensjon.saksbehandling.SaksbehandlerDO;
import no.nav.presentation.pensjon.saksbehandling.form.StartForm;
import no.nav.service.pensjon.saksbehandling.SaksbehandlerService;


public class StartAction extends MultiAction {
	private SaksbehandlerService saksbehandlerService;
	private static final Log log = LogFactory.getLog(StartAction.class);
	
	public Event executeSearch(RequestContext context) {
		StartForm form = (StartForm) context.getFlowScope().get("startForm");
		log.debug("form.getSaksbehandlernr(): " + form.getSaksbehandlernr());
		log.debug("saksbehandlerService = " + saksbehandlerService);
		
		SaksbehandlerDO saksbehandler = saksbehandlerService.readSaksbehandler(form.getSaksbehandlernr());

		log.debug("saksbehandler = " + saksbehandler);
		context.getExternalContext().getSessionMap().put("saksbehandlerDO", saksbehandler);

		return success();
	}

	public void setSaksbehandlerService(SaksbehandlerService saksbehandlerService) {
		this.saksbehandlerService = saksbehandlerService;
	}
}
