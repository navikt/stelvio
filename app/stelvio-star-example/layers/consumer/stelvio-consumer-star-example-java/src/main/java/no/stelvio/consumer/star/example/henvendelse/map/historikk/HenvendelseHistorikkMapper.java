package no.stelvio.consumer.star.example.henvendelse.map.historikk;

import no.stelvio.star.example.henvendelse.historikk.HenvendelseHistorikk;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class HenvendelseHistorikkMapper {
	public HenvendelseHistorikk toDomainObject(ASBOPenHenvendelseHistorikk henvendelseHistorikk) {
		return new HenvendelseHistorikk();
	}
}
