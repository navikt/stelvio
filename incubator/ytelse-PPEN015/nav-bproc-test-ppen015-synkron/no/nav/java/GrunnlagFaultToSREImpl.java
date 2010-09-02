package no.nav.java;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag;
import com.ibm.websphere.sca.ServiceManager;

public class GrunnlagFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public GrunnlagFaultToSREImpl() {
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
	 * named "GrunnlagPartner".  This will return an instance of
	 * {@link Grunnlag}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link GrunnlagAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Grunnlag
	 */
	public Grunnlag locateService_GrunnlagPartner() {
		return (Grunnlag) ServiceManager.INSTANCE
				.locateService("GrunnlagPartner");
	}

	/**
	 * Method generated to support implemention of operation "hentPersongrunnlagListe" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentPersongrunnlagListe(
			DataObject hentPersongrunnlagListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagrePersongrunnlagListe" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagrePersongrunnlagListe(
			DataObject lagrePersongrunnlagRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentMinstepensjonSats" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentMinstepensjonSats(
			DataObject hentMinstepensjonsSatsRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentGrunnbelopListe" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentGrunnbelopListe(DataObject hentGrunnbelopListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "kopierInntektsgrunnlag" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void kopierInntektsgrunnlag(DataObject kopierInntektsgrunnlagRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "kopierOpptjeningsgrunnlag" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void kopierOpptjeningsgrunnlag(
			DataObject kopierOpptjeningsgrunnlagRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "kopierFamilieforholdForBruker" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void kopierFamilieforholdForBruker(
			DataObject kopierFamilieforholdForBrukerRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "kopierTrygdetidsgrunnlag" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String kopierTrygdetidsgrunnlag(
			DataObject kopierTrygdetidsgrunnlagRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentUforegrunnlag" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentUforegrunnlag(DataObject hentUforegrunnlagRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentOmsorgOverfGrunnlag" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOmsorgOverfGrunnlag(
			DataObject hentOmsorgOverfGrunnlagRequest) {
		try {
			return locateService_GrunnlagPartner().hentOmsorgOverfGrunnlag(
					hentOmsorgOverfGrunnlagRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "hentOmsorgGodskrGrunnlag" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOmsorgGodskrGrunnlag(
			DataObject hentOmsorgGodskrGrunnlagRequest) {
		try {
			return locateService_GrunnlagPartner().hentOmsorgGodskrGrunnlag(
					hentOmsorgGodskrGrunnlagRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "hentTrygdetidsgrunnlagListe" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentTrygdetidsgrunnlagListe(
			DataObject hentTrygdetidsgrunnlagListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreTrygdetidsgrunnlagListe" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreTrygdetidsgrunnlagListe(
			DataObject lagreTrygdetidsgrunnlagListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "kopierGrunnlagForKrav" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void kopierGrunnlagForKrav(DataObject kopierGrunnlagForKravRequest) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation "lagreVurdertSivilstandListeOgVurderEndring" defined for WSDL port type 
	 * named "Grunnlag".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreVurdertSivilstandListeOgVurderEndring(
			DataObject lagreVurdertSivilstandListeOgVurderEndringRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentPersongrunnlagListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentPersongrunnlagListe(DataObject aDataObject)	
	 */
	public void onHentPersongrunnlagListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#lagrePersongrunnlagListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#lagrePersongrunnlagListe(DataObject aDataObject)	
	 */
	public void onLagrePersongrunnlagListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentMinstepensjonSats(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentMinstepensjonSats(DataObject aDataObject)	
	 */
	public void onHentMinstepensjonSatsResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentGrunnbelopListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentGrunnbelopListe(DataObject aDataObject)	
	 */
	public void onHentGrunnbelopListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierInntektsgrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierInntektsgrunnlag(DataObject aDataObject)	
	 */
	public void onKopierInntektsgrunnlagResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierOpptjeningsgrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierOpptjeningsgrunnlag(DataObject aDataObject)	
	 */
	public void onKopierOpptjeningsgrunnlagResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierFamilieforholdForBruker(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierFamilieforholdForBruker(DataObject aDataObject)	
	 */
	public void onKopierFamilieforholdForBrukerResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierTrygdetidsgrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierTrygdetidsgrunnlag(DataObject aDataObject)	
	 */
	public void onKopierTrygdetidsgrunnlagResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentUforegrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentUforegrunnlag(DataObject aDataObject)	
	 */
	public void onHentUforegrunnlagResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentOmsorgOverfGrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentOmsorgOverfGrunnlag(DataObject aDataObject)	
	 */
	public void onHentOmsorgOverfGrunnlagResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentOmsorgGodskrGrunnlag(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentOmsorgGodskrGrunnlag(DataObject aDataObject)	
	 */
	public void onHentOmsorgGodskrGrunnlagResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentTrygdetidsgrunnlagListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#hentTrygdetidsgrunnlagListe(DataObject aDataObject)	
	 */
	public void onHentTrygdetidsgrunnlagListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#lagreTrygdetidsgrunnlagListe(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#lagreTrygdetidsgrunnlagListe(DataObject aDataObject)	
	 */
	public void onLagreTrygdetidsgrunnlagListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierGrunnlagForKrav(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#kopierGrunnlagForKrav(DataObject aDataObject)	
	 */
	public void onKopierGrunnlagForKravResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#lagreVurdertSivilstandListeOgVurderEndring(DataObject aDataObject))
	 * of java interface (@link pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag)	
	 * @see pen.lib.nav.no.nav.lib.pen.inf.grunnlag.Grunnlag#lagreVurdertSivilstandListeOgVurderEndring(DataObject aDataObject)	
	 */
	public void onLagreVurdertSivilstandListeOgVurderEndringResponse(
			Ticket __ticket, DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}