package no.nav.java;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
/**
 * Stub som simulerer Pensjonssak kall
 * @author person2f8774420f39
 *
 */
public class PensjonssakStubImpl {

	private final String nameSpace="http://nav-lib-test/no/nav/lib/test/gbo";
	private final String faultNameSpace = "http://nav-lib-test/no/nav/lib/test/fault";
	
	/**
	 * Default constructor.
	 */
	public PensjonssakStubImpl() {
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
	 * Simulerer opprettKontrollpunkt kall. Ved å sette triggerSbe = true i request, simuleres kasting av FaultSakIkkeFunnet.
	 */
	public DataObject opprettKontrollpunkt(
			DataObject opprettKontrollpunktRequest) {
		if (opprettKontrollpunktRequest.getBoolean("triggerSbe"))
		{
			DataObject faultSakIkkeFunnet = DataFactory.INSTANCE.create(faultNameSpace, "FaultSakIkkeFunnet");
			faultSakIkkeFunnet.setString("errorMessage", "Simulerer feil faultSakIkkeFunnet i opprettKontrollpunkt" );
			throw new ServiceBusinessException(faultSakIkkeFunnet);
		}
		
		DataObject gboKontrollpunkt = createGBOKontrollpunkt();
		return gboKontrollpunkt;
	}



	/**
	 * Brukes foreløpig ikke.
	 */
	public DataObject hentKontrollpunktListe(
			DataObject hentKontrollpunktListeRequest) {
		
		return null;
	}

	/**
	 * Brukes foreløpig ikke.
	 */
	public DataObject hentPensjonssakListe(
			DataObject hentPensjonssakListeRequest) {
		
		return null;
	}

	/**
	 * Simulerer hentPensjonssak kall. Resultat kan manipuleres ved å sette følgende data i request:
	 * 1. triggerSbe=true - simulerer kasting av business fault FaultSakIkkeFunnet
	 * 2. triggerSre=true - simulerer kasting av ServiceRuntimeException
	 */
	public DataObject hentPensjonssak(DataObject hentPensjonssakRequest) {
		if (hentPensjonssakRequest.getBoolean("triggerSbe"))
		{
			DataObject faultSakIkkeFunnet = DataFactory.INSTANCE.create(faultNameSpace, "FaultSakIkkeFunnet");
			faultSakIkkeFunnet.setString("errorMessage", "Simulerer feil faultSakIkkeFunnet i hentPensjonssak" );
			throw new ServiceBusinessException(faultSakIkkeFunnet);
		}
		if (hentPensjonssakRequest.getBoolean("triggerSre"))
		{
			throw new ServiceRuntimeException("Simulerer runtime feil i hentPensjonssak");
		}
		DataObject gboPensjonssak = createGboPensjonssak();
		return gboPensjonssak;
	}

	/**
	 * Bygger en GBOPensjonssak stub
	 * @return gboPensjonssak
	 */
	private DataObject createGboPensjonssak() {
		DataObject gboPensjonssak = DataFactory.INSTANCE.create(nameSpace,"GBOPensjonssak" );
		gboPensjonssak.setString("saksId", "789");
		return gboPensjonssak;
	}
	
	/**
	 * Bygger en GBOKontrollpunkt stub
	 * @return gboKontrollpunkt
	 */
	private DataObject createGBOKontrollpunkt() {
		DataObject gboKontrollpunkt = DataFactory.INSTANCE.create(nameSpace, "GBOKontrollpunkt");
		gboKontrollpunkt.setString("kontrollpunktId", "1234");
		gboKontrollpunkt.setString("kontrollpunktTypeKode","BAKSYS_UTILGJENGELIG");
		gboKontrollpunkt.setBoolean("kritisk", true);
		return gboKontrollpunkt;
	}

}