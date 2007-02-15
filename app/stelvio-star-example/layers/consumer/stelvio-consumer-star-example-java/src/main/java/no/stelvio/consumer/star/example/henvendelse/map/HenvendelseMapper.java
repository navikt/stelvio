package no.stelvio.consumer.star.example.henvendelse.map;

import no.nav.domain.pensjon.henvendelse.Henvendelse;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class HenvendelseMapper {
	public Henvendelse toDomainObject(ASBOPenHenvendelse henvendelse) {
		return new Henvendelse("fodselsnummer");
	}
}
