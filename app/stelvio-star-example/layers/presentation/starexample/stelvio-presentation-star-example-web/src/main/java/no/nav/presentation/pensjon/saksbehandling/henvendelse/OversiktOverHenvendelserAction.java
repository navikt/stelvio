package no.nav.presentation.pensjon.saksbehandling.henvendelse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.FormAction;

import no.nav.domain.pensjon.codestable.HenvendelseTypeCti;
import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk;
import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;
import no.nav.service.pensjon.henvendelse.HenvendelseService;
import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkRequest;
import no.stelvio.common.codestable.CodesTableManager;


/**
 * Action class for "Oversikt over henvendelser".
 * 
 * @author personff564022aedd
 */
public class OversiktOverHenvendelserAction extends FormAction {
	private static final Log log = LogFactory.getLog(OversiktOverHenvendelserAction.class);

	private HenvendelseService henvendelseService;
	private CodesTableManager codesTableManager;

	// TODO: brukes denne eller formObject i context-fila?
	public OversiktOverHenvendelserForm setupForm() {
		return new OversiktOverHenvendelserForm();
	}

	/**
	 * @param form contains input data for backend request
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
		log.debug("CodesTable HenvendelseTypeCti: " +
				codesTableManager.getCodesTablePeriodic(HenvendelseTypeCti.class));
		return (HenvendelseStatistikk)henvendelseService.genererHenvendelseStatistikk(new HenvendelseStatistikkRequest(crit)).getCriteria();
	}

	public void setHenvendelseService(HenvendelseService henvendelseService) {
		this.henvendelseService = henvendelseService;
	}

	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}
}
