package no.stelvio.consumer.star.example.henvendelse.map.historikk;

import java.util.ArrayList;

import no.stelvio.domain.star.example.henvendelse.Henvendelse;
import no.stelvio.domain.star.example.henvendelse.historikk.HenvendelseHistory;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class HenvendelseHistoricsMapper {
	public HenvendelseHistory toDomainObject(ASBOPenHenvendelseHistorikk henvendelseHistorikk) {
		return new HenvendelseHistory(new ArrayList<Henvendelse>());
	}
}
