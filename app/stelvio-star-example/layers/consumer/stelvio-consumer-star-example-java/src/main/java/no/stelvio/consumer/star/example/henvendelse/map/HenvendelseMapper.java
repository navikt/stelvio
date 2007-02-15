package no.stelvio.consumer.star.example.henvendelse.map;

import no.stelvio.domain.star.example.henvendelse.Henvendelse;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class HenvendelseMapper {
	public Henvendelse toDomainObject(ASBOPenHenvendelse henvendelse) {
		return new Henvendelse("fodselsnummer");
	}
}
