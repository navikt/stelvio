package no.stelvio.service.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceResponse;
import no.stelvio.star.example.henvendelse.HenvendelseStatistikk;

/**
 * @author personf8e9850ed756, Accenture
 */
public class HenvendelseStatistikkResponse extends ServiceResponse {
	private HenvendelseStatistikk henvendelseStatistikk;

	/**
	 * @param henvendelseStatistikkCriteria critera to be added in the new instance
	 */
	public HenvendelseStatistikkResponse(HenvendelseStatistikk henvendelseStatistikkCriteria) {
		this.henvendelseStatistikk = henvendelseStatistikkCriteria;
	}

	/**
	 * {@inheritDoc HenvendelseStatistikkRequest#getCriteria()}
	 */
	public HenvendelseStatistikk getCriteria() {
		return henvendelseStatistikk;
	}
}
