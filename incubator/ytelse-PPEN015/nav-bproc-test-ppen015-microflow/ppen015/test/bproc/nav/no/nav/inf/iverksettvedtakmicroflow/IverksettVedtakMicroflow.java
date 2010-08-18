/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/nav-bproc-test-ppen015-microflow/no/nav/inf/IverksettVedtakMicroflow.wsdl]##
 * ##[lineageStampEnd]##
 */
package ppen015.test.bproc.nav.no.nav.inf.iverksettvedtakmicroflow;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface IverksettVedtakMicroflow {
	public static final String PORTTYPE_NAME = "{http://nav-bproc-test-ppen015/no/nav/inf/IverksettVedtakMicroflow}IverksettVedtakMicroflow";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "invokeIverksettVedtak" operation on WSDL port "IverksettVedtakMicroflow"
	 */
	public void invokeIverksettVedtak(DataObject iverksettVedtakRequest)
			throws ServiceBusinessException;

}
