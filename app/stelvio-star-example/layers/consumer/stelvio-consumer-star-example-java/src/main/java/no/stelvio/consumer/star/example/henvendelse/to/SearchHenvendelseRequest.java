package no.stelvio.consumer.star.example.henvendelse.to;

import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatisticsCriteria;

/**
 * Defines criteria for generating and returning
 * {@link no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics}.
 * 
 * @author personff564022aedd
 */
public class SearchHenvendelseRequest extends ServiceRequest {
	private HenvendelseStatisticsCriteria henvendelseStatisticsCriteria;

	/**
	 * Constructs a new instance.
	 * 
	 * @param henvendelseStatisticsCriteria critera to be added in the new instance.
	 */
	public SearchHenvendelseRequest(HenvendelseStatisticsCriteria henvendelseStatisticsCriteria) {
		this.henvendelseStatisticsCriteria = henvendelseStatisticsCriteria;
	}

	/**
	 * Returns the value of criteria.
	 *
	 * @return value for criteria.
	 */
	public HenvendelseStatisticsCriteria getCriteria() {
		return henvendelseStatisticsCriteria;
	}
}
