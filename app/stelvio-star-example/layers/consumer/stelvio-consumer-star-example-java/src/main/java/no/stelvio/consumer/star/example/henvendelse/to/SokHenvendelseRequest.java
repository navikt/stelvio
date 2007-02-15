package no.stelvio.consumer.star.example.henvendelse.to;

import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;
import no.stelvio.common.transferobject.ServiceRequest;

/**
 * Defines criteria for generating and returning {@link no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk}.
 * @author personff564022aedd
 */
public class SokHenvendelseRequest extends ServiceRequest {
	private HenvendelseStatistikkCriteria henvendelseStatistikkCriteria;

	/**
	 * @param henvendelseStatistikkCriteria critera to be added in the new instance
	 */
	public SokHenvendelseRequest(HenvendelseStatistikkCriteria henvendelseStatistikkCriteria) {
		this.henvendelseStatistikkCriteria = henvendelseStatistikkCriteria;
	}

	/**
	 * {@inheritDoc SokHenvendelseRequest#getCriteria()}
	 */
	public HenvendelseStatistikkCriteria getCriteria() {
		return henvendelseStatistikkCriteria;
	}
}
