/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-test/no/nav/lib/test/inf/Pensjonssak.wsdl]##
 * ##[lineageStampEnd]##
 */
package pen.lib.nav.no.nav.lib.pen.inf.pensjonssak;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface Pensjonssak {
	public static final String PORTTYPE_NAME = "{http://nav-lib-pen/no/nav/lib/pen/inf/Pensjonssak}Pensjonssak";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettKontrollpunkt" operation on WSDL port "ns1:Pensjonssak"
	 */
	public DataObject opprettKontrollpunkt(
			DataObject opprettKontrollpunktRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentKontrollpunktListe" operation on WSDL port "ns1:Pensjonssak"
	 */
	public DataObject hentKontrollpunktListe(
			DataObject hentKontrollpunktListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentPensjonssakListe" operation on WSDL port "ns1:Pensjonssak"
	 */
	public DataObject hentPensjonssakListe(
			DataObject hentPensjonssakListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentPensjonssak" operation on WSDL port "ns1:Pensjonssak"
	 */
	public DataObject hentPensjonssak(DataObject hentPensjonssakRequest)
			throws ServiceBusinessException;

}
