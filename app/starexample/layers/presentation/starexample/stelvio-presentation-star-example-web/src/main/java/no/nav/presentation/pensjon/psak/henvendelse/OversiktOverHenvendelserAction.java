package no.nav.presentation.pensjon.psak.henvendelse;

import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk;
import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;
import no.nav.service.pensjon.henvendelse.HenvendelseService;
import no.nav.service.pensjon.henvendelse.to.impl.HenvendelseStatistikkRequestImpl;

import org.springframework.webflow.action.FormAction;


/**
 * Action class for "Oversikt over henvendelser".
 * 
 * @author personff564022aedd
 */
public class OversiktOverHenvendelserAction extends FormAction {

	private HenvendelseService henvendelseService;

	public void setHenvendelseService(HenvendelseService henvendelseService) {
		this.henvendelseService = henvendelseService;
	}

	// TODO: brukes denne?
	public OversiktOverHenvendelserForm setupForm() {
		return new OversiktOverHenvendelserForm();
	}
	
	/**
	 * @param form contains input data for backend request
	 * @param enhetId id of unit saksbehandler is assigned to
	 * @return {@link HenvendelseStatistikk}
	 */
	public HenvendelseStatistikk hentStatistikk(OversiktOverHenvendelserForm form) {
		//TODO enhetId
		HenvendelseStatistikkCriteria crit =null; 
//			new HenvendelseStatistikkCriteria(
//				"", 
//				form.getValgtTidsperiode(),
//				form.getValgtFagomrade(),
//				form.getValgtSok());
//		
		return (HenvendelseStatistikk)henvendelseService.genererHenvendelseStatistikk(new HenvendelseStatistikkRequestImpl(crit)).getEntity();
	}

}
