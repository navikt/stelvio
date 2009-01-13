/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-test/no/nav/lib/test/inf/Vedtak.wsdl]##
 * ##[lineageStampEnd]##
 */
package pen.lib.nav.no.nav.lib.pen.inf.vedtak;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface Vedtak {
	public static final String PORTTYPE_NAME = "{http://nav-lib-pen/no/nav/lib/pen/inf/Vedtak}Vedtak";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentVedtakListe" operation on WSDL port "ns1:Vedtak"
	 */
	public DataObject hentVedtakListe(DataObject hentVedtakListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentVedtak" operation on WSDL port "ns1:Vedtak"
	 */
	public DataObject hentVedtak(DataObject hentVedtakRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreVedtakStatus" operation on WSDL port "ns1:Vedtak"
	 */
	public String lagreVedtakStatus(DataObject lagreVedtakStatusRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreVedtak" operation on WSDL port "ns1:Vedtak"
	 */
	public String lagreVedtak(DataObject lagreVedtakRequest)
			throws ServiceBusinessException;

}
