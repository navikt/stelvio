package no.stelvio.consumer.star.example.henvendelse.to;

import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk;
import no.stelvio.common.transferobject.ServiceResponse;

/**
 * @author personf8e9850ed756, Accenture
 */
public class SokHenvendelseResponse extends ServiceResponse {
	private HenvendelseStatistikk henvendelseStatistikk;

	/**
	 * @param henvendelseStatistikkCriteria critera to be added in the new instance
	 */
	public SokHenvendelseResponse(HenvendelseStatistikk henvendelseStatistikkCriteria) {
		this.henvendelseStatistikk = henvendelseStatistikkCriteria;
	}

	/**
	 * {@inheritDoc SokHenvendelseRequest#getCriteria()}
	 */
	public HenvendelseStatistikk getCriteria() {
		return henvendelseStatistikk;
	}
}
