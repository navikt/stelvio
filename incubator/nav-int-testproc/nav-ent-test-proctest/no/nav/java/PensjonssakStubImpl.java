package no.nav.java;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;

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
	 * Method generated to support implemention of operation "opprettKontrollpunkt" defined for WSDL port type 
	 * named "Pensjonssak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettKontrollpunkt(
			DataObject opprettKontrollpunktRequest) {
		if (opprettKontrollpunktRequest.getBoolean("triggerSbe"))
		{
			DataObject faultSakIkkeFunnet = DataFactory.INSTANCE.create(faultNameSpace, "FaultSakIkkeFunnet");
			faultSakIkkeFunnet.setString("errorMessage", "Simulerer feil faultSakIkkeFunnet i opprettKontrollpunkt" );
			throw new ServiceBusinessException(faultSakIkkeFunnet);
		}
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentKontrollpunktListe" defined for WSDL port type 
	 * named "Pensjonssak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentKontrollpunktListe(
			DataObject hentKontrollpunktListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentPensjonssakListe" defined for WSDL port type 
	 * named "Pensjonssak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentPensjonssakListe(
			DataObject hentPensjonssakListeRequest) {
		
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentPensjonssak" defined for WSDL port type 
	 * named "Pensjonssak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
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

	private DataObject createGboPensjonssak() {
		DataObject gboPensjonssak = DataFactory.INSTANCE.create(nameSpace,"GBOPensjonssak" );
		gboPensjonssak.setString("saksId", "789");
		return gboPensjonssak;
	}

}