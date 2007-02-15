package no.stelvio.consumer.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceResponse;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics;

/**
 * The response from calling search henvendelse.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class SearchHenvendelseResponse extends ServiceResponse {
	private static final long serialVersionUID = 246870219293724483L;

	private HenvendelseStatistics henvendelseStatistics;

	/**
	 * Constructs a new instance.
	 * 
	 * @param henvendelseStatisticsCriteria critera to be added in the new instance
	 */
	public SearchHenvendelseResponse(HenvendelseStatistics henvendelseStatisticsCriteria) {
		this.henvendelseStatistics = henvendelseStatisticsCriteria;
	}

	/**
	 * Returns the value of criteria.
	 *
	 * @return value for criteria.
	 */
	public HenvendelseStatistics getCriteria() {
		return henvendelseStatistics;
	}
}
