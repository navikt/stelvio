package no.nav.java;

import java.util.ArrayList;

import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.iverksett.vedtak.IverksettVedtak;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.sca.ServiceManager;

public class SamordningStubImpl {
	/**
	 * Default constructor.
	 */
	public SamordningStubImpl() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this
	 * implementation class. This method should be used when passing this
	 * service to a partner reference or if you want to invoke this component
	 * service asynchronously.
	 * 
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference named
	 * "IverksettVedtakPartner". This will return an instance of
	 * {@link IverksettVedtak}. If you would like to use this service
	 * asynchronously then you will need to cast the result to
	 * {@link IverksettVedtakAsync}.
	 * 
	 * @generated (com.ibm.wbit.java)
	 * 
	 * @return IverksettVedtak
	 */
	public IverksettVedtak locateService_IverksettVedtakPartner() {
		return (IverksettVedtak) ServiceManager.INSTANCE
				.locateService("IverksettVedtakPartner");
	}

	/**
	 * Method generated to support implemention of operation
	 * "opprettTPSamordning" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject opprettTPSamordning(DataObject opprettTPSamordningRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentSamordningsdata" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentSamordningsdata(DataObject hentSamordningsdataRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "slettTPSamordning"
	 * defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void slettTPSamordning(DataObject slettTPSamordningRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "opprettRefusjonskrav" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void opprettRefusjonskrav(DataObject opprettRefusjonskravRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentSamordningsVedtaksListe" defined for WSDL port type named
	 * "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentSamordningsVedtaksListe(
			DataObject hentSamordningsVedtaksListeRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "lagreSamordningsMelding" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void lagreSamordningsMelding(
			DataObject lagreSamordningsMeldingRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "opprettVedtakSamordning" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void opprettVedtakSamordning(
			DataObject opprettVedtakSamordningRequest) {
		
		String vedtaksId = opprettVedtakSamordningRequest.getString("vedtakId");

		BOFactory bof = (BOFactory) ServiceManager.INSTANCE
				.locateService("com/ibm/websphere/bo/BOFactory");
		DataObject mottaSamhandler = bof.create(
				"http://nav-lib-pen/no/nav/lib/pen/gbo", "GBOVedtaksId");

		mottaSamhandler.setString("vedtaksId", vedtaksId);
		
		locateService_IverksettVedtakPartner().mottaSamhandlerSvar(mottaSamhandler);
	}

	/**
	 * Method generated to support implemention of operation
	 * "varsleEndringPersonalia" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void varsleEndringPersonalia(
			DataObject varsleEndringPersonaliaRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "varsleManglendeRefusjonskrav" defined for WSDL port type named
	 * "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void varsleManglendeRefusjonskrav(
			DataObject varsleManglendeRefusjonskravRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "varsleTPSamordning" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void varsleTPSamordning(DataObject varsleTPSamordningRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "varsleVedtakSamordning" defined for WSDL port type named "Samordning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void varsleVedtakSamordning(DataObject varsleVedtakSamordningRequest) {
		// TODO Needs to be implemented.
	}

}