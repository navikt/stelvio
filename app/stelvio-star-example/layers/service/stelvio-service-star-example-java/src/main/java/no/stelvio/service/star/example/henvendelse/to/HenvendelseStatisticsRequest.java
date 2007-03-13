package no.stelvio.service.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatisticsCriteria;

/**
 * Defines criteria for generating and returning {@link no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics}.
 * 
 * @author personff564022aedd
 */
public class HenvendelseStatisticsRequest extends ServiceRequest {
	private static final long serialVersionUID = 4890318586138308581L;
	private HenvendelseStatisticsCriteria henvendelseStatisticsCriteria;

	/**
	 * @param henvendelseStatisticsCriteria critera to be added in the new instance
	 */
	public HenvendelseStatisticsRequest(HenvendelseStatisticsCriteria henvendelseStatisticsCriteria) {
		this.henvendelseStatisticsCriteria = henvendelseStatisticsCriteria;
	}

	/**
	 * {@inheritDoc HenvendelseStatisticsRequest#getCriteria()}
	 */
	public HenvendelseStatisticsCriteria getCriteria() {
		return henvendelseStatisticsCriteria;
	}
}
