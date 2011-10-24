  
package no.nav.provider.pensjon.sak;	


/**
 * @generated
 */
public interface SakServiceBiAsync {

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentSakListeAsync(no.nav.dto.pensjon.sak.to.HentSakListeRequestDto dtoRequest);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentSakListeAsyncWithCallback(no.nav.dto.pensjon.sak.to.HentSakListeRequestDto dtoRequest);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.HentSakListeResponseDto hentSakListeResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentKontrollpunktListeAsync(no.nav.dto.pensjon.sak.to.HentKontrollpunktListeRequestDto hentKontrollpunktListeRequestDto);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentKontrollpunktListeAsyncWithCallback(no.nav.dto.pensjon.sak.to.HentKontrollpunktListeRequestDto hentKontrollpunktListeRequestDto);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.HentKontrollpunktListeResponseDto hentKontrollpunktListeResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket lagreKontrollpunktAsync(no.nav.dto.pensjon.sak.to.LagreKontrollpunktRequestDto lagreKontrollpunktRequestDto);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket lagreKontrollpunktAsyncWithCallback(no.nav.dto.pensjon.sak.to.LagreKontrollpunktRequestDto lagreKontrollpunktRequestDto);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.LagreKontrollpunktResponseDto lagreKontrollpunktResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentSakAsync(no.nav.dto.pensjon.sak.to.HentSakRequestDto hentSakRequestDto);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentSakAsyncWithCallback(no.nav.dto.pensjon.sak.to.HentSakRequestDto hentSakRequestDto);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.HentSakResponseDto hentSakResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout) throws no.nav.dto.pensjon.exception.PEN038SakIkkeFunnetDtoException;

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentUforehistorikkAsync(no.nav.dto.pensjon.sak.to.HentUforehistorikkRequestDto dtoRequest);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentUforehistorikkAsyncWithCallback(no.nav.dto.pensjon.sak.to.HentUforehistorikkRequestDto dtoRequest);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.HentUforehistorikkResponseDto hentUforehistorikkResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentBatchBrevdataAsync(no.nav.dto.pensjon.sak.to.HentBatchBrevdataRequestDto dtoRequest);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket hentBatchBrevdataAsyncWithCallback(no.nav.dto.pensjon.sak.to.HentBatchBrevdataRequestDto dtoRequest);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.HentBatchBrevdataResponseDto hentBatchBrevdataResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket lagreSakAsync(no.nav.dto.pensjon.sak.to.LagreSakRequestDto lagreSakRequestDto);

  /**
   * @generated
   */		
  public com.ibm.websphere.sca.Ticket lagreSakAsyncWithCallback(no.nav.dto.pensjon.sak.to.LagreSakRequestDto lagreSakRequestDto);

  /**
   * @generated
   */		
  public no.nav.dto.pensjon.sak.to.LagreSakResponseDto lagreSakResponse(com.ibm.websphere.sca.Ticket __ticket, long __timeout);
}
