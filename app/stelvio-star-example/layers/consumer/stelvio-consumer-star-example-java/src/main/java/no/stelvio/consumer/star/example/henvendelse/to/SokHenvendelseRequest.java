package no.stelvio.consumer.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistikkCriteria;

/**
 * Defines criteria for generating and returning {@link no.stelvio.domain.star.example.henvendelse.HenvendelseStatistikk}.
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
