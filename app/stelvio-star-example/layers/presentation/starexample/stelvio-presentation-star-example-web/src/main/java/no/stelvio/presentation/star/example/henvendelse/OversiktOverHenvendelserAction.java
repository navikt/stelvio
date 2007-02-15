package no.stelvio.presentation.star.example.henvendelse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.FormAction;

import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.consumer.star.example.henvendelse.SokHenvendelse;
import no.stelvio.consumer.star.example.henvendelse.to.SokHenvendelseRequest;
import no.stelvio.domain.star.example.codestable.HenvendelseTypeCti;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistikk;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistikkCriteria;


/**
 * Action class for "Oversikt over henvendelser".
 * 
 * @author personff564022aedd
 */
public class OversiktOverHenvendelserAction extends FormAction {
	private static final Log log = LogFactory.getLog(OversiktOverHenvendelserAction.class);

	private SokHenvendelse sokHenvendelse;
	private CodesTableManager codesTableManager;

	/**
	 * @param form contains input data for backend request
	 * @return {@link HenvendelseStatistikk}
	 */
	public HenvendelseStatistikk hentStatistikk(OversiktOverHenvendelserForm form) {
		// TODO enhetId
		HenvendelseStatistikkCriteria crit = 
				new HenvendelseStatistikkCriteria(
						"enhetsid", form.getValgtTidsperiode(), form.getValgtFagomrade(), form.getValgtSok());

		log.debug("CodesTable HenvendelseTypeCti: " +
				codesTableManager.getCodesTablePeriodic(HenvendelseTypeCti.class));
		return sokHenvendelse.genererHenvendelseStatistikk(new SokHenvendelseRequest(crit)).getCriteria();
	}

	public void setSokHenvendelse(SokHenvendelse sokHenvendelse) {
		this.sokHenvendelse = sokHenvendelse;
	}

	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}
}
