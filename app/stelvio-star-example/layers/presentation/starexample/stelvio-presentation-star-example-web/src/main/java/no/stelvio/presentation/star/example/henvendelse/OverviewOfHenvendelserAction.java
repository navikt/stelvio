package no.stelvio.presentation.star.example.henvendelse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.FormAction;

import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.consumer.star.example.henvendelse.SearchHenvendelseServiceBi;
import no.stelvio.consumer.star.example.henvendelse.to.SearchHenvendelseRequest;
import no.stelvio.domain.star.example.codestable.HenvendelseTypeCti;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatisticsCriteria;


/**
 * Action class for "Oversikt over henvendelser".
 * 
 * @author personff564022aedd
 */
public class OverviewOfHenvendelserAction extends FormAction {
	private static final Log log = LogFactory.getLog(OverviewOfHenvendelserAction.class);

	private SearchHenvendelseServiceBi searchHenvendelseService;
	private CodesTableManager codesTableManager;

	/**
	 * @param form contains input data for backend request
	 * @return {@link HenvendelseStatistics}
	 */
	public HenvendelseStatistics hentStatistikk(OverviewOfHenvendelserForm form) {
		// TODO enhetId
		HenvendelseStatisticsCriteria crit =
				new HenvendelseStatisticsCriteria(
						"enhetsid", form.getChosenTidsperiode(), form.getChosenFagomrade(), form.getChosenSearch());

		log.debug("CodesTable HenvendelseTypeCti: " +
				codesTableManager.getCodesTablePeriodic(HenvendelseTypeCti.class));
		return searchHenvendelseService.genererHenvendelseStatistikk(new SearchHenvendelseRequest(crit)).getCriteria();
	}

	public void setSearchHenvendelseService(SearchHenvendelseServiceBi searchHenvendelseService) {
		this.searchHenvendelseService = searchHenvendelseService;
	}

	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}
}
