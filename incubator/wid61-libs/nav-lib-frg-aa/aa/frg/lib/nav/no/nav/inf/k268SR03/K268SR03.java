/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-lib-frg-aa/K268SR03.wsdl]##
 * ##[lineageStampEnd]##
 */
package aa.frg.lib.nav.no.nav.inf.k268SR03;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface K268SR03 {
	public static final String PORTTYPE_NAME = "{http://nav-lib-frg-aa/no/nav/lib/frg/aa/inf}K268SR03";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "hentArbeidsforhold" operation on WSDL port "__1:K268SR03"
	 */
	public DataObject hentArbeidsforhold(DataObject hentArbeidsforholdInput)
			throws ServiceBusinessException;

}
