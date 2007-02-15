package no.stelvio.consumer.star.example.henvendelse.to;

import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;
import no.stelvio.common.transferobject.ServiceRequest;

/**
 * Defines criteria for generating and returning {@link no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk}.
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
