package no.nav.java;

import sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class SamordningFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public SamordningFaultToSREImpl() {
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
	 * named "SamordningPartner".  This will return an instance of
	 * {@link Samordning}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link sto.lib.nav.no.nav.lib.sto.inf.samordning.SamordningAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Samordning
	 */
	public Samordning locateService_SamordningPartner() {
		return (Samordning) ServiceManager.INSTANCE
				.locateService("SamordningPartner");
	}

	/**
	 * Method generated to support implemention of operation "opprettTPSamordning" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opprettTPSamordning(DataObject opprettTPSamordningRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentSamordningsdata" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentSamordningsdata(DataObject hentSamordningsdataRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "slettTPSamordning" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void slettTPSamordning(DataObject slettTPSamordningRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "opprettRefusjonskrav" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void opprettRefusjonskrav(DataObject opprettRefusjonskravRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "hentSamordningsVedtaksListe" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentSamordningsVedtaksListe(
			DataObject hentSamordningsVedtaksListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreSamordningsMelding" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void lagreSamordningsMelding(
			DataObject lagreSamordningsMeldingRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "opprettVedtakSamordning" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void opprettVedtakSamordning(
			DataObject opprettVedtakSamordningRequest) {
		try{
			locateService_SamordningPartner().opprettVedtakSamordning(opprettVedtakSamordningRequest);
		}catch (ServiceBusinessException sbe){
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "varsleEndringPersonalia" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void varsleEndringPersonalia(
			DataObject varsleEndringPersonaliaRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "varsleManglendeRefusjonskrav" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void varsleManglendeRefusjonskrav(
			DataObject varsleManglendeRefusjonskravRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "varsleTPSamordning" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void varsleTPSamordning(DataObject varsleTPSamordningRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "varsleVedtakSamordning" defined for WSDL port type 
	 * named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void varsleVedtakSamordning(DataObject varsleVedtakSamordningRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning#opprettTPSamordning(DataObject aDataObject))
	 * of java interface (@link sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning)	
	 * @see sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning#opprettTPSamordning(DataObject aDataObject)	
	 */
	public void onOpprettTPSamordningResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning#hentSamordningsdata(DataObject aDataObject))
	 * of java interface (@link sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning)	
	 * @see sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning#hentSamordningsdata(DataObject aDataObject)	
	 */
	public void onHentSamordningsdataResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning#hentSamordningsVedtaksListe(DataObject aDataObject))
	 * of java interface (@link sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning)	
	 * @see sto.lib.nav.no.nav.lib.sto.inf.samordning.Samordning#hentSamordningsVedtaksListe(DataObject aDataObject)	
	 */
	public void onHentSamordningsVedtaksListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}