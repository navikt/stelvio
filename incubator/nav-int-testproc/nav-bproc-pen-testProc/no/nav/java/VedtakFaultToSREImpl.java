package no.nav.java;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak;
import com.ibm.websphere.sca.ServiceManager;

public class VedtakFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public VedtakFaultToSREImpl() {
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
	 * named "VedtakPartner".  This will return an instance of
	 * {@link Vedtak}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link VedtakAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Vedtak
	 */
	public Vedtak locateService_VedtakPartner() {
		return (Vedtak) ServiceManager.INSTANCE.locateService("VedtakPartner");
	}

	/**
	 * Method generated to support implemention of operation "hentVedtakListe" defined for WSDL port type 
	 * named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentVedtakListe(DataObject hentVedtakListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentVedtak" defined for WSDL port type 
	 * named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentVedtak(DataObject hentVedtakRequest) {
		
		try{
			return locateService_VedtakPartner().hentVedtak(hentVedtakRequest);
		}catch(ServiceBusinessException sbe){
			throw new ServiceRuntimeException(sbe);
		}
		
		
	}

	/**
	 * Method generated to support implemention of operation "lagreVedtakStatus" defined for WSDL port type 
	 * named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String lagreVedtakStatus(DataObject lagreVedtakStatusRequest) {
		//TODO Needs to be implemented.
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
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtakListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtakListe(DataObject aDataObject)	
	 */
	public void onHentVedtakListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtak(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtak(DataObject aDataObject)	
	 */
	public void onHentVedtakResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#lagreVedtakStatus(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#lagreVedtakStatus(DataObject aDataObject)	
	 */
	public void onLagreVedtakStatusResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#lagreVedtak(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#lagreVedtak(DataObject aDataObject)	
	 */
	public void onLagreVedtakResponse(Ticket __ticket, String returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}