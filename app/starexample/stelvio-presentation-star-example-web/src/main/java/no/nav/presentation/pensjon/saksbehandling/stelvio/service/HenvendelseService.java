/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import no.nav.domain.pensjon.henvendelse.Henvendelse;
import no.nav.domain.pensjon.henvendelse.NewHenvendelse;
import no.nav.domain.pensjon.person.Fodselsnummer;


/**
 * @author person4f9bc5bd17cc
 *
 */
public interface HenvendelseService {
	public List<NewHenvendelse> readHenvendelseList(Fodselsnummer fodselsnummer);
	public List<Henvendelse> addHenvendelseToList(Henvendelse henvendelserDO, List<Henvendelse> sortableList);
	public List<Henvendelse> updateHenvendelseList(Henvendelse henvendelserDO, List<Henvendelse> sortableList);
	public Henvendelse createHenvendelse(Henvendelse henvendelse);
	public void updateHenvendelse(Henvendelse henvendelse);
	public Henvendelse readHenvendelse(Long hid, List<Henvendelse> sortableList);
	public ArrayList<SelectItem> getFagomrade();
	public ArrayList<SelectItem> getHenvendelsesType();
	public ArrayList<SelectItem> getStonadstype();
	public ArrayList<SelectItem> getKanal();
	public String getTidsbruk();
	public ArrayList<SelectItem> getStatus();
}