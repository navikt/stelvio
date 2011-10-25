package no.nav.java;

import no.nav.dto.pensjon.exception.PEN038SakIkkeFunnetDtoException;
import no.nav.dto.pensjon.kjerne.sak.BatchBrevdataDto;
import no.nav.dto.pensjon.sak.to.HentBatchBrevdataRequestDto;
import no.nav.dto.pensjon.sak.to.HentBatchBrevdataResponseDto;
import no.nav.dto.pensjon.sak.to.HentKontrollpunktListeRequestDto;
import no.nav.dto.pensjon.sak.to.HentKontrollpunktListeResponseDto;
import no.nav.dto.pensjon.sak.to.HentSakListeRequestDto;
import no.nav.dto.pensjon.sak.to.HentSakListeResponseDto;
import no.nav.dto.pensjon.sak.to.HentSakRequestDto;
import no.nav.dto.pensjon.sak.to.HentSakResponseDto;
import no.nav.dto.pensjon.sak.to.HentUforehistorikkRequestDto;
import no.nav.dto.pensjon.sak.to.HentUforehistorikkResponseDto;
import no.nav.dto.pensjon.sak.to.LagreKontrollpunktRequestDto;
import no.nav.dto.pensjon.sak.to.LagreKontrollpunktResponseDto;
import no.nav.dto.pensjon.sak.to.LagreSakRequestDto;
import no.nav.dto.pensjon.sak.to.LagreSakResponseDto;
import no.nav.provider.pensjon.sak.SakServiceBi;

import com.ibm.websphere.sca.ServiceManager;

public class Component1Impl2 implements SakServiceBi {
	/**
	 * Default constructor.
	 */
	public Component1Impl2() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this implementation
	 * class.  This method should be used when passing this service to a partner reference
	 * or if you want to invoke this component service asynchronously.    
	 *
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private SakServiceBi getMyService() {
		return (SakServiceBi) ServiceManager.INSTANCE.locateService("self");
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#hentSakListe(HentSakListeRequestDto dtoRequest) 
	 */
	public HentSakListeResponseDto hentSakListe(
			HentSakListeRequestDto dtoRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#hentKontrollpunktListe(HentKontrollpunktListeRequestDto hentKontrollpunktListeRequestDto) 
	 */
	public HentKontrollpunktListeResponseDto hentKontrollpunktListe(
			HentKontrollpunktListeRequestDto hentKontrollpunktListeRequestDto) {
		//TODO Needs to be implemented.
		return null;
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#lagreKontrollpunkt(LagreKontrollpunktRequestDto lagreKontrollpunktRequestDto) 
	 */
	public LagreKontrollpunktResponseDto lagreKontrollpunkt(
			LagreKontrollpunktRequestDto lagreKontrollpunktRequestDto) {
		//TODO Needs to be implemented.
		return null;
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#hentSak(HentSakRequestDto hentSakRequestDto) 
	 */
	public HentSakResponseDto hentSak(HentSakRequestDto hentSakRequestDto)
			throws PEN038SakIkkeFunnetDtoException {
		//TODO Needs to be implemented.
		return null;
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#hentUforehistorikk(HentUforehistorikkRequestDto dtoRequest) 
	 */
	public HentUforehistorikkResponseDto hentUforehistorikk(
			HentUforehistorikkRequestDto dtoRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#hentBatchBrevdata(HentBatchBrevdataRequestDto dtoRequest) 
	 */
	public HentBatchBrevdataResponseDto hentBatchBrevdata(
			HentBatchBrevdataRequestDto dtoRequest) {
		
		//Creating a stub for recording
		HentBatchBrevdataResponseDto hentBatchBrevdataResponseDto = new HentBatchBrevdataResponseDto();
		BatchBrevdataDto batchBrevdataDto = new BatchBrevdataDto();
		
		batchBrevdataDto.setBrevGruppe("brevGruppe");
		batchBrevdataDto.setBrevkode("brevkode");
		batchBrevdataDto.setDokumentType("dokumentType");
		batchBrevdataDto.setElektroniskDistribusjon(true);
		batchBrevdataDto.setFagsystem("fagsystem");
		batchBrevdataDto.setInnhold("innhold");
		batchBrevdataDto.setInternBatchbrevkode("internBatchbrevkode");
		batchBrevdataDto.setKategori("kategori");
		batchBrevdataDto.setRedigerbart(true);
		batchBrevdataDto.setSensitivt(true);
				
		hentBatchBrevdataResponseDto.setBatchBrevdata(batchBrevdataDto);
		
		return hentBatchBrevdataResponseDto;
	}

	/* (non-Javadoc)
	 * @see no.nav.provider.pensjon.sak.SakServiceBi#lagreSak(LagreSakRequestDto lagreSakRequestDto) 
	 */
	public LagreSakResponseDto lagreSak(LagreSakRequestDto lagreSakRequestDto) {
		//TODO Needs to be implemented.
		return null;
	}

}