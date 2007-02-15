package no.nav.presentation.pensjon.saksbehandling.henvendelse;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

import no.stelvio.star.example.henvendelse.Fagomrade;
import no.stelvio.star.example.henvendelse.Spesifikasjon;
import no.stelvio.star.example.henvendelse.Tidsperiode;

/**
 * Form used in oversiktoverhenvendelser.xhtml.
 * 
 * @author personff564022aedd
 */
public class OversiktOverHenvendelserForm {

	private Fagomrade valgtFagomrade;
	private List<SelectItem> fagomrader;
	private Tidsperiode valgtTidsperiode;
	private List<SelectItem> tidsperioder;
	private Spesifikasjon valgtSok;
	private List<SelectItem> sokeAlternativ;
	
	public OversiktOverHenvendelserForm() {
		sokeAlternativ = new ArrayList<SelectItem>();
		sokeAlternativ.add(new SelectItem(Spesifikasjon.ANTALL, ""));
		sokeAlternativ.add(new SelectItem(Spesifikasjon.GRUNN, ""));
		sokeAlternativ.add(new SelectItem(Spesifikasjon.KANAL, ""));
		sokeAlternativ.add(new SelectItem(Spesifikasjon.TYPE, ""));
		valgtSok = Spesifikasjon.ANTALL;
		
		fagomrader = new ArrayList<SelectItem>();
		fagomrader.add(new SelectItem(Fagomrade.PENSJON, Fagomrade.PENSJON.getValue()));
		fagomrader.add(new SelectItem(Fagomrade.BIDRAG, Fagomrade.BIDRAG.getValue()));
		fagomrader.add(new SelectItem(Fagomrade.ALLE, Fagomrade.ALLE.getValue()));
		valgtFagomrade = Fagomrade.PENSJON;

		tidsperioder = new ArrayList<SelectItem>();
		tidsperioder.add(new SelectItem(Tidsperiode.SISTE_4_UKER, Tidsperiode.SISTE_4_UKER.getValue()));
		tidsperioder.add(new SelectItem(Tidsperiode.SISTE_5_DAGER, Tidsperiode.SISTE_5_DAGER.getValue()));
		valgtTidsperiode = Tidsperiode.IKKE_SATT; 
	}
	
	public Spesifikasjon getValgtSok() {
		return valgtSok;
	}

	public void setValgtSok(Spesifikasjon valgtSok) {
		this.valgtSok = valgtSok;
	}

	public List<SelectItem> getFagomrader() {
		return fagomrader;
	}

	public void setFagomrader(List<SelectItem> fagomrader) {
		this.fagomrader = fagomrader;
	}

	public List<SelectItem> getTidsperioder() {
		return tidsperioder;
	}

	public void setTidsperioder(List<SelectItem> tidsperioder) {
		this.tidsperioder = tidsperioder;
	}

	public List<SelectItem> getSokeAlternativ() {
		return sokeAlternativ;
	}

	public Fagomrade getValgtFagomrade() {
		return valgtFagomrade;
	}

	public void setValgtFagomrade(Fagomrade valgtFagomrade) {
		this.valgtFagomrade = valgtFagomrade;
	}

	public Tidsperiode getValgtTidsperiode() {
		return valgtTidsperiode;
	}

	public void setValgtTidsperiode(Tidsperiode valgtTidsperiode) {
		this.valgtTidsperiode = valgtTidsperiode;
	}	
}
