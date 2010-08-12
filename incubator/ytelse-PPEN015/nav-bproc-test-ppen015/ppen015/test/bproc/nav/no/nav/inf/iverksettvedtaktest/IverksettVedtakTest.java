/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-bproc-test-ppen015/no/nav/inf/IverksettVedtakTest.wsdl]##
 * ##[lineageStampEnd]##
 */
package ppen015.test.bproc.nav.no.nav.inf.iverksettvedtaktest;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface IverksettVedtakTest {
	public static final String PORTTYPE_NAME = "{http://nav-bproc-test-ppen015/no/nav/inf/IverksettVedtakTest}IverksettVedtakTest";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "invokeIverksettVedtak" operation on WSDL port "IverksettVedtakTest:IverksettVedtakTest"
	 */
	public void invokeIverksettVedtak(DataObject iverksettVedtakRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "mottaSamhandlerSvar" operation on WSDL port "IverksettVedtakTest:IverksettVedtakTest"
	 */
	public void mottaSamhandlerSvar(DataObject mottaSamhandler)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "samordningFerdig" operation on WSDL port "IverksettVedtakTest:IverksettVedtakTest"
	 */
	public void samordningFerdig(DataObject samordningFerdig)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "mottaOSKvittering" operation on WSDL port "IverksettVedtakTest:IverksettVedtakTest"
	 */
	public void mottaOSKvittering(DataObject mottaOSKvitteringRequest)
			throws ServiceBusinessException;

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "positivOSKvitteringMottatt" operation on WSDL port "IverksettVedtakTest:IverksettVedtakTest"
	 */
	public void positivOSKvitteringMottatt(
			DataObject positivOSKvitteringMottattRequest)
			throws ServiceBusinessException;

}
