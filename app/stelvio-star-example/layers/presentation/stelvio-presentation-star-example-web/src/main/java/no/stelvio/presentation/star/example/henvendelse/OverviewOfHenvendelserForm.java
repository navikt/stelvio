package no.stelvio.presentation.star.example.henvendelse;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

import no.stelvio.domain.star.example.henvendelse.Fagomrade;
import no.stelvio.domain.star.example.henvendelse.Spesifikasjon;
import no.stelvio.domain.star.example.henvendelse.Tidsperiode;

/**
 * Form used in overviewofhenvendelser.xhtml.
 * 
 * @author personff564022aedd
 */
public class OverviewOfHenvendelserForm {

	private Fagomrade chosenFagomrade;
	private List<SelectItem> fagomrader;
	private Tidsperiode chosenTidsperiode;
	private List<SelectItem> tidsperioder;
	private Spesifikasjon chosenSearch;
	private List<SelectItem> searchAlternative;
	
	public OverviewOfHenvendelserForm() {
		searchAlternative = new ArrayList<SelectItem>();
		searchAlternative.add(new SelectItem(Spesifikasjon.ANTALL, ""));
		searchAlternative.add(new SelectItem(Spesifikasjon.GRUNN, ""));
		searchAlternative.add(new SelectItem(Spesifikasjon.KANAL, ""));
		searchAlternative.add(new SelectItem(Spesifikasjon.TYPE, ""));
		chosenSearch = Spesifikasjon.ANTALL;
		
		fagomrader = new ArrayList<SelectItem>();
		fagomrader.add(new SelectItem(Fagomrade.PENSJON, Fagomrade.PENSJON.getValue()));
		fagomrader.add(new SelectItem(Fagomrade.BIDRAG, Fagomrade.BIDRAG.getValue()));
		fagomrader.add(new SelectItem(Fagomrade.ALLE, Fagomrade.ALLE.getValue()));
		chosenFagomrade = Fagomrade.PENSJON;

		tidsperioder = new ArrayList<SelectItem>();
		tidsperioder.add(new SelectItem(Tidsperiode.SISTE_4_UKER, Tidsperiode.SISTE_4_UKER.getValue()));
		tidsperioder.add(new SelectItem(Tidsperiode.SISTE_5_DAGER, Tidsperiode.SISTE_5_DAGER.getValue()));
		chosenTidsperiode = Tidsperiode.IKKE_SATT;
	}
	
	public Spesifikasjon getChosenSearch() {
		return chosenSearch;
	}

	public void setChosenSearch(Spesifikasjon chosenSearch) {
		this.chosenSearch = chosenSearch;
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

	public List<SelectItem> getSearchAlternative() {
		return searchAlternative;
	}

	public Fagomrade getChosenFagomrade() {
		return chosenFagomrade;
	}

	public void setChosenFagomrade(Fagomrade chosenFagomrade) {
		this.chosenFagomrade = chosenFagomrade;
	}

	public Tidsperiode getChosenTidsperiode() {
		return chosenTidsperiode;
	}

	public void setChosenTidsperiode(Tidsperiode chosenTidsperiode) {
		this.chosenTidsperiode = chosenTidsperiode;
	}	
}
