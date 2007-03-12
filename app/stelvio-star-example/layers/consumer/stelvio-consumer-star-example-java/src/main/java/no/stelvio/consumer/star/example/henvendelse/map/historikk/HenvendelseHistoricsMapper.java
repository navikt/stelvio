package no.stelvio.consumer.star.example.henvendelse.map.historikk;

import java.util.ArrayList;

import no.stelvio.domain.star.example.henvendelse.Henvendelse;
import no.stelvio.domain.star.example.henvendelse.historikk.HenvendelseHistory;

/**
 * Maps from an ASBO version of henvendelse history to a local version.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class HenvendelseHistoricsMapper {
	public HenvendelseHistory toDomainObject(ASBOPenHenvendelseHistory henvendelseHistory) {
		return new HenvendelseHistory(new ArrayList<Henvendelse>());
	}
}
