package no.nav.service.pensjon.henvendelse.to;

import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;

/**
 * Defines criteria for generating and returning {@link no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk}.
 * 
 * @author personff564022aedd
 */
public interface HenvendelseStatistikkRequest {

	/**
	 * @return {@link HenvendelseStatistikkCriteria}
	 */
	HenvendelseStatistikkCriteria getCriteria();
}
