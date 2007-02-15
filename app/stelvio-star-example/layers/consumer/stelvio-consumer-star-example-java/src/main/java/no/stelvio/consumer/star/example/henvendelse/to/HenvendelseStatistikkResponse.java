package no.stelvio.consumer.star.example.henvendelse.to;

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
