package no.nav.java;

import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak;
import com.ibm.websphere.sca.ServiceManager;

public class VedtakStubImpl {
	/**
	 * Default constructor.
	 */
	public VedtakStubImpl() {
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
	 * "VedtakPartner". This will return an instance of {@link Vedtak}. If you
	 * would like to use this service asynchronously then you will need to cast
	 * the result to {@link VedtakAsync}.
	 * 
	 * @generated (com.ibm.wbit.java)
	 * 
	 * @return Vedtak
	 */
	public Vedtak locateService_VedtakPartner() {
		return (Vedtak) ServiceManager.INSTANCE.locateService("VedtakPartner");
	}

	/**
	 * Method generated to support implemention of operation "hentVedtakListe"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentVedtakListe(DataObject hentVedtakListeRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentVedtak"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentVedtak(DataObject hentVedtakRequest) {
		return locateService_VedtakPartner().hentVedtak(hentVedtakRequest);
	}

	/**
	 * Method generated to support implemention of operation "lagreVedtakStatus"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void lagreVedtakStatus(DataObject lagreVedtakStatusRequest) {
		return;
	}

	/**
	 * Method generated to support implemention of operation
	 * "lagreVilkarsvedtakListe" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject lagreVilkarsvedtakListe(
			DataObject lagreVilkarsvedtakListeRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "vurderVilkar"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject vurderVilkar(DataObject vurderVilkarRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "lagreVedtak"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void lagreVedtak(DataObject lagreVedtakRequest) {
		return;
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentPensjonssaksInfoListe" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentPensjonssaksInfoListe(
			DataObject hentPensjonssaksInfoListeRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentUttrekkTilOppdragstransaksjon" defined for WSDL port type named
	 * "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentUttrekkTilOppdragstransaksjon(
			DataObject hentUttrekkTilOppdragstransaksjonRequest) {
		return locateService_VedtakPartner().hentUttrekkTilOppdragstransaksjon(hentUttrekkTilOppdragstransaksjonRequest);
	}

	/**
	 * Method generated to support implemention of operation
	 * "hentTilbakekrevingTotal" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject hentTilbakekrevingTotal(
			DataObject hentTilbakekrevingTotalRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "kopierVilkarsprovingForKravListe" defined for WSDL port type named
	 * "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject kopierVilkarsprovingForKravListe(
			DataObject kopierVilkarsprovingForKravListeRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "behandleOSKvittering" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject behandleOSKvittering(
			DataObject behandleOSKvitteringRequest) {
		// TODO stubbes?
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "klargjorForIverksetting" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject klargjorForIverksetting(
			DataObject klargjorForIverksettingRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "validerVilkarsproving" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject validerVilkarsproving(
			DataObject validerVilkarsprovingRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "opphorYtelse"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject opphorYtelse(DataObject opphorYtelseRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "finnPengemottakerOgLagreAutoBrev" defined for WSDL port type named
	 * "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject finnPengemottakerOgLagreAutoBrev(
			DataObject finnPengemottakerOgLagreAutobrevRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "vilkarsprovYtelse2011" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject vilkarsprovYtelse2011(
			DataObject vilkarsprovYtelse2011Request) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation
	 * "behandleBerorteSaker" defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public void behandleBerorteSaker(DataObject behandleBerorteSakerRequest) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support implemention of operation
	 * "sjekkOmBrukerHarLopendeAfpVedtak" defined for WSDL port type named
	 * "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject sjekkOmBrukerHarLopendeAfpVedtak(
			DataObject sjekkOmBrukerHarLopendeAfpVedtakRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "finnVedtakListe"
	 * defined for WSDL port type named "Vedtak".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject finnVedtakListe(DataObject finnVedtakListeRequest) {
		// TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtakListe(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtakListe(DataObject
	 *      aDataObject)
	 */
	public void onHentVedtakListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtak(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentVedtak(DataObject
	 *      aDataObject)
	 */
	public void onHentVedtakResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#lagreVilkarsvedtakListe(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#lagreVilkarsvedtakListe(DataObject
	 *      aDataObject)
	 */
	public void onLagreVilkarsvedtakListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#vurderVilkar(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#vurderVilkar(DataObject
	 *      aDataObject)
	 */
	public void onVurderVilkarResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentPensjonssaksInfoListe(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentPensjonssaksInfoListe(DataObject
	 *      aDataObject)
	 */
	public void onHentPensjonssaksInfoListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentUttrekkTilOppdragstransaksjon(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentUttrekkTilOppdragstransaksjon(DataObject
	 *      aDataObject)
	 */
	public void onHentUttrekkTilOppdragstransaksjonResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentTilbakekrevingTotal(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#hentTilbakekrevingTotal(DataObject
	 *      aDataObject)
	 */
	public void onHentTilbakekrevingTotalResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#kopierVilkarsprovingForKravListe(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#kopierVilkarsprovingForKravListe(DataObject
	 *      aDataObject)
	 */
	public void onKopierVilkarsprovingForKravListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#behandleOSKvittering(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#behandleOSKvittering(DataObject
	 *      aDataObject)
	 */
	public void onBehandleOSKvitteringResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#klargjorForIverksetting(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#klargjorForIverksetting(DataObject
	 *      aDataObject)
	 */
	public void onKlargjorForIverksettingResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#validerVilkarsproving(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#validerVilkarsproving(DataObject
	 *      aDataObject)
	 */
	public void onValiderVilkarsprovingResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#opphorYtelse(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#opphorYtelse(DataObject
	 *      aDataObject)
	 */
	public void onOpphorYtelseResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#finnPengemottakerOgLagreAutoBrev(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#finnPengemottakerOgLagreAutoBrev(DataObject
	 *      aDataObject)
	 */
	public void onFinnPengemottakerOgLagreAutoBrevResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#vilkarsprovYtelse2011(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#vilkarsprovYtelse2011(DataObject
	 *      aDataObject)
	 */
	public void onVilkarsprovYtelse2011Response(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#sjekkOmBrukerHarLopendeAfpVedtak(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#sjekkOmBrukerHarLopendeAfpVedtak(DataObject
	 *      aDataObject)
	 */
	public void onSjekkOmBrukerHarLopendeAfpVedtakResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback for
	 * the operation (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#finnVedtakListe(DataObject
	 * aDataObject)) of java interface (@link
	 * pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak)
	 * 
	 * @see pen.lib.nav.no.nav.lib.pen.inf.vedtak.Vedtak#finnVedtakListe(DataObject
	 *      aDataObject)
	 */
	public void onFinnVedtakListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		// TODO Needs to be implemented.
	}

}