package no.stelvio.domain.star.example.henvendelse.historikk;

import java.util.List;

import no.stelvio.domain.star.example.henvendelse.Henvendelse;

/**
 * @author personf8e9850ed756, Accenture
 */
public class HenvendelseHistory {
	private List<Henvendelse> henvendelser;

	public HenvendelseHistory(List<Henvendelse> henvendelser) {
		this.henvendelser = henvendelser;
	}
}
