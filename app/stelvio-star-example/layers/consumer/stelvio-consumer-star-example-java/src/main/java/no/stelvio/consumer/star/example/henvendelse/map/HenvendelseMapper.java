package no.stelvio.consumer.star.example.henvendelse.map;

import no.stelvio.domain.star.example.henvendelse.Henvendelse;

/**
 * Maps from an ASBO version of henvendelse to a local version.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class HenvendelseMapper {
	public Henvendelse toDomainObject(ASBOPenHenvendelse henvendelse) {
		return new Henvendelse("fodselsnummer");
	}
}
