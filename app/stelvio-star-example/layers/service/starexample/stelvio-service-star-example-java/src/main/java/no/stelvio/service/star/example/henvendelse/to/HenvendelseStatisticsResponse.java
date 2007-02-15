package no.stelvio.service.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceResponse;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics;

/**
 * @author personf8e9850ed756, Accenture
 */
public class HenvendelseStatisticsResponse extends ServiceResponse {
	private HenvendelseStatistics henvendelseStatistics;

	/**
	 * @param henvendelseStatisticsCriteria critera to be added in the new instance
	 */
	public HenvendelseStatisticsResponse(HenvendelseStatistics henvendelseStatisticsCriteria) {
		this.henvendelseStatistics = henvendelseStatisticsCriteria;
	}

	/**
	 * {@inheritDoc HenvendelseStatisticsRequest#getCriteria()}
	 */
	public HenvendelseStatistics getCriteria() {
		return henvendelseStatistics;
	}
}
