/**
 * ##[lineageStampBegin]##
 * ##[generatedBy:/stelvio-example-echo-proxy-ejb-jaxrpc/ejbs/LargeGraphmapper.wsdl]##
 * ##[lineageStampEnd]##
 */
package ejbs;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;

/**
 * @generated (com.ibm.wbit.java.core)
 * An interface used for enabling a static programming model for the service. 
 * The methods provide synchronous invocations of the service operations.
 */
public interface LargeGraph1 {
	public static final String PORTTYPE_NAME = "{http://ejbs}LargeGraph";

	/**
	 * @generated (com.ibm.wbit.java.core)
	 * The "echo" operation on WSDL port "ns1:LargeGraph"
	 */
	public DataObject echo(String input) throws ServiceBusinessException;

}
