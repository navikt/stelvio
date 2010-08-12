package no.nav.java;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.Opptjening;
import com.ibm.websphere.sca.ServiceManager;

public class OpptjeningFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public OpptjeningFaultToSREImpl() {
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
	 * named "OpptjeningPartner".  This will return an instance of
	 * {@link Opptjening}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link pen.lib.nav.no.nav.lib.pen.inf.OpptjeningAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Opptjening
	 */
	public Opptjening locateService_OpptjeningPartner() {
		return (Opptjening) ServiceManager.INSTANCE
				.locateService("OpptjeningPartner");
	}

	/**
	 * Method generated to support implemention of operation "lagreOpptjening" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreOpptjening(DataObject lagreOpptjeningRequest) {
		try {
			return locateService_OpptjeningPartner().lagreOpptjening(
					lagreOpptjeningRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "hentOpptjeningsgrunnlagListe" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOpptjeningsgrunnlagListe(
			DataObject hentOpptjeningsgrunnlagListeRequest) {
		return locateService_OpptjeningPartner().hentOpptjeningsgrunnlagListe(
				hentOpptjeningsgrunnlagListeRequest);
	}

	/**
	 * Method generated to support implemention of operation "lagreInntekt" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreInntekt(DataObject lagreInntektRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "opprettInntekt" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettInntekt(DataObject opprettInntektRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "slettInntekt" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void slettInntekt(DataObject slettInntektRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "slettOpptjening" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void slettOpptjening(DataObject slettOpptjeningRequest) {
		try {
			locateService_OpptjeningPartner().slettOpptjening(
					slettOpptjeningRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "opprettOpptjening" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettOpptjening(DataObject opprettOpptjeningRequest) {
		try {
			return locateService_OpptjeningPartner().opprettOpptjening(
					opprettOpptjeningRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "hentBeholdning" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentBeholdning(DataObject hentBeholdningRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentBeholdningListe" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentBeholdningListe(DataObject hentBeholdningListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "overforOmsorgsopptjeningListe" defined for WSDL port type 
	 * named "Opptjening".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject overforOmsorgsopptjeningListe(
			DataObject overforOmsorgsopptjeningListeRequest) {
		try {
			return locateService_OpptjeningPartner().overforOmsorgsopptjeningListe(
					overforOmsorgsopptjeningListeRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening#lagreOpptjening(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.Opptjening#lagreOpptjening(DataObject aDataObject)	
	 */
	public void onLagreOpptjeningResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening#hentOpptjeningsgrunnlagListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.Opptjening#hentOpptjeningsgrunnlagListe(DataObject aDataObject)	
	 */
	public void onHentOpptjeningsgrunnlagListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening#lagreInntekt(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.Opptjening#lagreInntekt(DataObject aDataObject)	
	 */
	public void onLagreInntektResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening#opprettInntekt(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.Opptjening#opprettInntekt(DataObject aDataObject)	
	 */
	public void onOpprettInntektResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening#opprettOpptjening(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.Opptjening)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.Opptjening#opprettOpptjening(DataObject aDataObject)	
	 */
	public void onOpprettOpptjeningResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}