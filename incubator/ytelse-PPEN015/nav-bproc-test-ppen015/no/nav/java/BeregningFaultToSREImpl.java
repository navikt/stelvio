package no.nav.java;

import pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class BeregningFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public BeregningFaultToSREImpl() {
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
	 * named "BeregningPartner".  This will return an instance of
	 * {@link Beregning}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link BeregningAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Beregning
	 */
	public Beregning locateService_BeregningPartner() {
		return (Beregning) ServiceManager.INSTANCE
				.locateService("BeregningPartner");
	}

	/**
	 * Method generated to support implemention of operation "beregnYtelse"
	 * defined for WSDL port type named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject beregnYtelse(DataObject beregnYtelseRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentBeregningSammendragBruker" defined for WSDL port type named
	 * "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentBeregningSammendragBruker(
			DataObject hentBeregningSammendragBrukerRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentBeregningSammendragAvdod" defined for WSDL port type named
	 * "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentBeregningSammendragAvdod(
			DataObject hentBeregningSammendragAvdodRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "simulerPensjonsberegning" defined for WSDL port type named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject simulerPensjonsberegning(
			DataObject simulerPensjonsberegningRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "beregnPensjonspoeng" defined for WSDL port type named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject beregnPensjonspoeng(DataObject beregnPensjonspoengRequest) {
		try {
			return locateService_BeregningPartner().beregnPensjonspoeng(
					beregnPensjonspoengRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "beregnVedtak"
	 * defined for WSDL port type named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject beregnVedtak(DataObject beregnVedtakRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentBeregningsinformasjon" defined for WSDL port type named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentBeregningsinformasjon(
			DataObject hentBeregningsinformasjonRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "forberedBeregning" defined for WSDL port type 
	 * named "Beregning".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject forberedBeregning(DataObject forberedBeregningRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "beregnAlderspensjonGammelOpptjening" defined for WSDL port type 
	 * named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject beregnAlderspensjonGammelOpptjening(
			DataObject beregnAlderspensjonGammelOpptjeningRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "beregnSammenstotstilfeller" defined for WSDL port type 
	 * named "Beregning".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject beregnSammenstotstilfeller(
			DataObject beregnSammenstotstilfellerRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnYtelse(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnYtelse(DataObject
	 *      aDataObject)
	 */
	public void onBeregnYtelseResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#hentBeregningSammendragBruker(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#hentBeregningSammendragBruker(DataObject
	 *      aDataObject)
	 */
	public void onHentBeregningSammendragBrukerResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#hentBeregningSammendragAvdod(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#hentBeregningSammendragAvdod(DataObject
	 *      aDataObject)
	 */
	public void onHentBeregningSammendragAvdodResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#simulerPensjonsberegning(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#simulerPensjonsberegning(DataObject
	 *      aDataObject)
	 */
	public void onSimulerPensjonsberegningResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnPensjonspoeng(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnPensjonspoeng(DataObject
	 *      aDataObject)
	 */
	public void onBeregnPensjonspoengResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnVedtak(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnVedtak(DataObject
	 *      aDataObject)
	 */
	public void onBeregnVedtakResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#hentBeregningsinformasjon(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#hentBeregningsinformasjon(DataObject
	 *      aDataObject)
	 */
	public void onHentBeregningsinformasjonResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#forberedBeregning(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#forberedBeregning(DataObject aDataObject)	
	 */
	public void onForberedBeregningResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnAlderspensjonGammelOpptjening(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnAlderspensjonGammelOpptjening(DataObject aDataObject)	
	 */
	public void onBeregnAlderspensjonGammelOpptjeningResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnSammenstotstilfeller(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.beregning.Beregning#beregnSammenstotstilfeller(DataObject aDataObject)	
	 */
	public void onBeregnSammenstotstilfellerResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}