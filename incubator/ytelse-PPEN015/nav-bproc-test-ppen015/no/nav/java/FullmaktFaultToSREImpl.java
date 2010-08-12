package no.nav.java;

import peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class FullmaktFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public FullmaktFaultToSREImpl() {
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
	 * named "FullmaktPartner".  This will return an instance of
	 * {@link Fullmakt}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.FullmaktAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Fullmakt
	 */
	public Fullmakt locateService_FullmaktPartner() {
		return (Fullmakt) ServiceManager.INSTANCE
				.locateService("FullmaktPartner");
	}

	/**
	 * Method generated to support implemention of operation "hentFullmaktListe" defined for WSDL port type 
	 * named "Fullmakt".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentFullmaktListe(DataObject hentFullmaktListeRequest) {
		try {
			return locateService_FullmaktPartner().hentFullmaktListe(
					hentFullmaktListeRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "lagreFullmakt" defined for WSDL port type 
	 * named "Fullmakt".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreFullmakt(DataObject lagreFullmaktRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "opprettFullmakt" defined for WSDL port type 
	 * named "Fullmakt".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettFullmakt(DataObject opprettFullmaktRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "slettFullmakt" defined for WSDL port type 
	 * named "Fullmakt".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject slettFullmakt(DataObject slettFullmaktRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreFullmaktSistBrukt" defined for WSDL port type 
	 * named "Fullmakt".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreFullmaktSistBrukt(
			DataObject lagreFullmaktSistBruktRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "finnFullmaktmottagere" defined for WSDL port type 
	 * named "Fullmakt".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject finnFullmaktmottagere(
			DataObject finnFullmaktmottagereRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#hentFullmaktListe(DataObject aDataObject))
	 * of java interface (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt)	
	 * @see peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#hentFullmaktListe(DataObject aDataObject)	
	 */
	public void onHentFullmaktListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#lagreFullmakt(DataObject aDataObject))
	 * of java interface (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt)	
	 * @see peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#lagreFullmakt(DataObject aDataObject)	
	 */
	public void onLagreFullmaktResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#opprettFullmakt(DataObject aDataObject))
	 * of java interface (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt)	
	 * @see peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#opprettFullmakt(DataObject aDataObject)	
	 */
	public void onOpprettFullmaktResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#slettFullmakt(DataObject aDataObject))
	 * of java interface (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt)	
	 * @see peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#slettFullmakt(DataObject aDataObject)	
	 */
	public void onSlettFullmaktResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#lagreFullmaktSistBrukt(DataObject aDataObject))
	 * of java interface (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt)	
	 * @see peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#lagreFullmaktSistBrukt(DataObject aDataObject)	
	 */
	public void onLagreFullmaktSistBruktResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#finnFullmaktmottagere(DataObject aDataObject))
	 * of java interface (@link peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt)	
	 * @see peh.lib.nav.no.nav.lib.peh.inf.fullmakt.Fullmakt#finnFullmaktmottagere(DataObject aDataObject)	
	 */
	public void onFinnFullmaktmottagereResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}