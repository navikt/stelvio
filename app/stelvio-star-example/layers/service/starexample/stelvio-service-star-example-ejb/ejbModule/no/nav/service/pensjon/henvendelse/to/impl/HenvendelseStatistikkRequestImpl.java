package no.nav.service.pensjon.henvendelse.to.impl;

import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;
import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkRequest;

/**
 * Defines criteria for generating and returning {@link no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk}.
 * @author personff564022aedd
 */
public class HenvendelseStatistikkRequestImpl implements HenvendelseStatistikkRequest {

	private HenvendelseStatistikkCriteria henvendelseStatistikkCriteria;
	
	/**
	 * @param henvendelseStatistikkCriteria critera to be added in the new instance
	 */
	public HenvendelseStatistikkRequestImpl(HenvendelseStatistikkCriteria henvendelseStatistikkCriteria) {
		this.henvendelseStatistikkCriteria = henvendelseStatistikkCriteria;
	}
	
	/**
	 * {@inheritDoc HenvendelseStatistikkRequest#getCriteria()}
	 */
	public HenvendelseStatistikkCriteria getCriteria() {
		return henvendelseStatistikkCriteria;
	}
}
