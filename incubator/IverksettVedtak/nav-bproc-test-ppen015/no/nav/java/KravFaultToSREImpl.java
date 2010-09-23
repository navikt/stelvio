package no.nav.java;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.krav.Krav;
import com.ibm.websphere.sca.ServiceManager;

public class KravFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public KravFaultToSREImpl() {
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
	 * named "KravPartner".  This will return an instance of
	 * {@link Krav}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link KravAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Krav
	 */
	public Krav locateService_KravPartner() {
		return (Krav) ServiceManager.INSTANCE.locateService("KravPartner");
	}

	/**
	 * Method generated to support implemention of operation "kontrollerKrav" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject kontrollerKrav(DataObject kontrollerKravRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentKrav" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentKrav(DataObject hentKravRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "kontrollerInstitusjonsopphold" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject kontrollerInstitusjonsopphold(
			DataObject kontrollerInstitusjonsoppholdRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreKravlinjeListe" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreKravlinjeListe(DataObject lagreKravlinjeListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreKravhode" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreKravhode(DataObject lagreKravhodeRequest) {
		try {
			return locateService_KravPartner().lagreKravhode(
					lagreKravhodeRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "kontrollerUtlandsopphold" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject kontrollerUtlandsopphold(
			DataObject kontrollerUtlandsoppholdRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentKravListe" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentKravListe(DataObject hentKravListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentEPSListe" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentEPSListe(DataObject hentEPSListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "kontrollerKravOgKompletterGrunnlag" defined for WSDL port type 
	 * named "Krav".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject kontrollerKravOgKompletterGrunnlag(
			DataObject kontrollerKravOgKompletterGrunnlagRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerKrav(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerKrav(DataObject aDataObject)	
	 */
	public void onKontrollerKravResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#hentKrav(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#hentKrav(DataObject aDataObject)	
	 */
	public void onHentKravResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerInstitusjonsopphold(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerInstitusjonsopphold(DataObject aDataObject)	
	 */
	public void onKontrollerInstitusjonsoppholdResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#lagreKravlinjeListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#lagreKravlinjeListe(DataObject aDataObject)	
	 */
	public void onLagreKravlinjeListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#lagreKravhode(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#lagreKravhode(DataObject aDataObject)	
	 */
	public void onLagreKravhodeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerUtlandsopphold(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerUtlandsopphold(DataObject aDataObject)	
	 */
	public void onKontrollerUtlandsoppholdResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#hentKravListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#hentKravListe(DataObject aDataObject)	
	 */
	public void onHentKravListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#hentEPSListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#hentEPSListe(DataObject aDataObject)	
	 */
	public void onHentEPSListeResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerKravOgKompletterGrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.krav.Krav)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.krav.Krav#kontrollerKravOgKompletterGrunnlag(DataObject aDataObject)	
	 */
	public void onKontrollerKravOgKompletterGrunnlagResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}