package no.nav.java;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak;
import com.ibm.websphere.sca.ServiceManager;

/**
 * Klassen skal fange business faults som ikke skal håndteres av prosess og
 * pakke disse inn i en runtime fault. Dette for at feil ikke skal nå prosess, men havne på FEM.
 * Merk at dette forutsetter at det finnes et asynk steg mellom BPEL og denne klassen.
 * @author person2f8774420f39
 *
 */public class PensjonssakFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public PensjonssakFaultToSREImpl() {
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
	 * This method is used to locate the service for the reference
	 * named "PensjonssakPartner".  This will return an instance of
	 * {@link Pensjonssak}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link PensjonssakAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Pensjonssak
	 */
	public Pensjonssak locateService_PensjonssakPartner() {
		return (Pensjonssak) ServiceManager.INSTANCE
				.locateService("PensjonssakPartner");
	}

	/**
	 * Metoden mapper business fault om til Runtime fault. Dette fordi gjeldende business faults ikke skal
	 * håndteres av prosess, men havne på FEM (Failed Event Manager)
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettKontrollpunkt(
			DataObject opprettKontrollpunktRequest) {
		try{
			return locateService_PensjonssakPartner().opprettKontrollpunkt(opprettKontrollpunktRequest);
		}catch(ServiceBusinessException sbe){
			throw new ServiceRuntimeException(sbe);
			
		}
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

			return locateService_PensjonssakPartner().hentPensjonssak(hentPensjonssakRequest);

	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#opprettKontrollpunkt(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#opprettKontrollpunkt(DataObject aDataObject)	
	 */
	public void onOpprettKontrollpunktResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#hentKontrollpunktListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#hentKontrollpunktListe(DataObject aDataObject)	
	 */
	public void onHentKontrollpunktListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#hentPensjonssakListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#hentPensjonssakListe(DataObject aDataObject)	
	 */
	public void onHentPensjonssakListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#hentPensjonssak(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.pensjonssak.Pensjonssak#hentPensjonssak(DataObject aDataObject)	
	 */
	public void onHentPensjonssakResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		
	}

}