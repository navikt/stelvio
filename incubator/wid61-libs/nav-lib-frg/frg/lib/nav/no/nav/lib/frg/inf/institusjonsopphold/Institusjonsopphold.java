/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-frg/no/nav/lib/frg/inf/Institusjonsopphold.wsdl]##
 * ##[lineageStampEnd]##
 */
package frg.lib.nav.no.nav.lib.frg.inf.institusjonsopphold;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface Institusjonsopphold {
	public static final String PORTTYPE_NAME = "{http://nav-lib-frg/no/nav/lib/frg/inf/Institusjonsopphold}Institusjonsopphold";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentInstitusjonsoppholdListe" operation on WSDL port "Institusjonsopphold"
	 */
	public DataObject hentInstitusjonsoppholdListe(
			DataObject hentInstitusjonsoppholdListeRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "lagreInstitusjonsopphold" operation on WSDL port "Institusjonsopphold"
	 */
	public DataObject lagreInstitusjonsopphold(
			DataObject lagreInstitusjonsoppholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "opprettInstitusjonsopphold" operation on WSDL port "Institusjonsopphold"
	 */
	public DataObject opprettInstitusjonsopphold(
			DataObject opprettInstitusjonsoppholdRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "slettIntstitusjonsopphold" operation on WSDL port "Institusjonsopphold"
	 */
	public String slettIntstitusjonsopphold(
			DataObject slettIntstitusjonsoppholdRequest)
			throws ServiceBusinessException;

}
