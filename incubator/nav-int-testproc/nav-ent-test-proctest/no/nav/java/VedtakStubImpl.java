package no.nav.java;

import java.util.ArrayList;
import java.util.List;

import commonj.sdo.DataObject;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
/**
 * Stub som simulerer Vedtak kall.
 * @author person2f8774420f39
 *
 */
public class VedtakStubImpl {
	
	
	private final String nameSpace="http://nav-lib-test/no/nav/lib/test/gbo";
	private final String faultNameSpace = "http://nav-lib-test/no/nav/lib/test/fault";
	/**
	 * Default constructor.
	 */
	public VedtakStubImpl() {
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
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * Simulerer hentVedtakListe kall. Resultatet kan manipuleres ved å sette returnTomVedtakListe i 
	 * hentVedtakListeRequest.
	 * 1. retunrTomVedtakListe = false - Et objekt av type GBOVedtakListe som inneholder vedtak returneres.
	 * 2. returnTomVedtakListe = true - Et tomt objekt av type GBOVedtakListe returneres.
	 * 
	 */
	public DataObject hentVedtakListe(DataObject hentVedtakListeRequest) {				
		DataObject gboVedtakListe = DataFactory.INSTANCE.create(nameSpace, "GBOVedtakListe");
		if (hentVedtakListeRequest.getBoolean("returnTomVedtakListe"))
		{
			return gboVedtakListe;
		}else
		{ 
			List<DataObject> vedtakListe = new ArrayList<DataObject>();
			DataObject gboVedtak = createGboVedtak(true);
			vedtakListe.add(gboVedtak);		
			gboVedtakListe.setList("vedtakListe", vedtakListe);
			return gboVedtakListe;
		}		
		
	}

	/**
	 * Simulerer kall hentVedtak.
	 * Man kan manipulere resultat ved å bruke følgende inputparametre i request:
	 * 1. setDatoBefore - Dersom denne er true, settes dato til en dato før 10.10.2008 for å trigge DatoFor10102008 case.
	 * 2. triggerSbe - Dersom denne er true, vil koden kaste ServiceBusinessException som er definert for kallet (faultVedtakIkkeFunnet).
	 * 3. triggerSre - Dersom denne er true, vil koden kaste en ServiceRuntimeException.
	 * @param hentVedtakRequest
	 */
	public DataObject hentVedtak(DataObject hentVedtakRequest) {
		if(hentVedtakRequest.getBoolean("triggerSbe"))
		{
			DataObject faultVedtakIkkeFunnet = DataFactory.INSTANCE.create(faultNameSpace, "FaultVedtakIkkeFunnet");
			faultVedtakIkkeFunnet.setString("errorMessage", "Simulerer feil faultVedtakIkkeFunnet");
			throw new ServiceBusinessException(faultVedtakIkkeFunnet);
		}
		if(hentVedtakRequest.getBoolean("triggerSre"))
		{
			throw new ServiceRuntimeException("Simulererer runtime feil i hentVedtak");
		}
		DataObject gboVedtak = createGboVedtak(hentVedtakRequest.getBoolean("setDatoBefore"));
		return gboVedtak;
	}

	/**
	 * Bygger en stub av GBOVedtak. 
	 * @param setDatoBefore - hvis true, settes dato til før 10102008, ellers til en dato etter dette.
	 * @return
	 */
	private DataObject createGboVedtak(boolean setDatoBefore) {
		DataObject gboVedtak = DataFactory.INSTANCE.create(nameSpace, "GBOVedtak");
		gboVedtak.setString("vedtakId", "567");
		gboVedtak.setString("saksId", "1234");
		if(setDatoBefore)
		{
			gboVedtak.setString("virkningFom", "2007-11-18T23:00:00Z");
		}else{
			gboVedtak.setString("virkningFom", "2008-11-18T23:00:00Z");
		}
		
		return gboVedtak;
	}

	/**
	 * Brukes foreløpig ikke.
	 */
	public String lagreVedtakStatus(DataObject lagreVedtakStatusRequest) {
		
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreVedtak" defined for WSDL port type 
	 * named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String lagreVedtak(DataObject lagreVedtakRequest) {
		
		return "OK";
	}

}