  
package no.nav.provider.pensjon.sak;	

/**
 * @generated
 */
public interface SakServiceBiImplAsync {

  /**
   * @generated
   */		
  public void hentSakListeAsync(no.nav.dto.pensjon.sak.to.HentSakListeRequestDto dtoRequest, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);

  /**
   * @generated
   */		
  public void hentKontrollpunktListeAsync(no.nav.dto.pensjon.sak.to.HentKontrollpunktListeRequestDto hentKontrollpunktListeRequestDto, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);

  /**
   * @generated
   */		
  public void lagreKontrollpunktAsync(no.nav.dto.pensjon.sak.to.LagreKontrollpunktRequestDto lagreKontrollpunktRequestDto, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);

  /**
   * @generated
   */		
  public void hentSakAsync(no.nav.dto.pensjon.sak.to.HentSakRequestDto hentSakRequestDto, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);

  /**
   * @generated
   */		
  public void hentUforehistorikkAsync(no.nav.dto.pensjon.sak.to.HentUforehistorikkRequestDto dtoRequest, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);

  /**
   * @generated
   */		
  public void hentBatchBrevdataAsync(no.nav.dto.pensjon.sak.to.HentBatchBrevdataRequestDto dtoRequest, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);

  /**
   * @generated
   */		
  public void lagreSakAsync(no.nav.dto.pensjon.sak.to.LagreSakRequestDto lagreSakRequestDto, no.nav.provider.pensjon.sak.SakServiceBiCallback __callback, com.ibm.websphere.sca.Ticket __ticket);
}
