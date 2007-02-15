package no.stelvio.service.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.star.example.henvendelse.HenvendelseStatistikkCriteria;

/**
 * Defines criteria for generating and returning {@link no.stelvio.star.example.henvendelse.HenvendelseStatistikk}.
 * @author personff564022aedd
 */
public class HenvendelseStatistikkRequest extends ServiceRequest {
	private HenvendelseStatistikkCriteria henvendelseStatistikkCriteria;

	/**
	 * @param henvendelseStatistikkCriteria critera to be added in the new instance
	 */
	public HenvendelseStatistikkRequest(HenvendelseStatistikkCriteria henvendelseStatistikkCriteria) {
		this.henvendelseStatistikkCriteria = henvendelseStatistikkCriteria;
	}

	/**
	 * {@inheritDoc HenvendelseStatistikkRequest#getCriteria()}
	 */
	public HenvendelseStatistikkCriteria getCriteria() {
		return henvendelseStatistikkCriteria;
	}
}
