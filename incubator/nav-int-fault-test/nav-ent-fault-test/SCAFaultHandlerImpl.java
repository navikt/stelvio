import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;

public class SCAFaultHandlerImpl {
	/**
	 * Default constructor.
	 */
	public SCAFaultHandlerImpl() {
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
	 * Method generated to support implemention of operation "twoWay" defined
	 * for WSDL port type named "ProducerInterface".
	 * 
	 * Please refer to the WSDL Definition for more information on the type of
	 * input, output and fault(s).
	 */
	public String twoWay(String twoWayRequest) {
		System.out.println("Into SCAFaultHandler:twoWay");
		if (twoWayRequest.equalsIgnoreCase("sre")) {
			throw new ServiceRuntimeException("SRE thrown in SCAFaultHandler:twoWay");
		} else if (twoWayRequest.equalsIgnoreCase("sbe")) {
			DataObject sbe = DataFactory.INSTANCE.create("http://myLibrary", "FaultTwoWayFault");
			sbe.setString("faultMessage", "Declared SBE thrown in SCAFaultHandler:twoWay");
			throw new ServiceBusinessException(sbe);
		} else if ((twoWayRequest).equalsIgnoreCase("sbeND")) {
			DataObject sbe = DataFactory.INSTANCE.create("http://myLibrary", "FaultNotDeclaredFault");
			sbe.setString("faultMessage", "Non declared SBE thrown in SCAFaultHandler:twoWay");
			throw new ServiceBusinessException(sbe);
		} else {
			return "Returning from SCAFaultHandler:twoWay";
		}
	}

	/**
	 * Method generated to support implemention of operation "oneWay" defined
	 * for WSDL port type named "ProducerInterface".
	 * 
	 * Please refer to the WSDL Definition for more information on the type of
	 * input, output and fault(s).
	 */
	public void oneWay(String oneWayRequest) {
		System.out.println("Into SCAFaultHandler:oneWay");
		if ((oneWayRequest).equalsIgnoreCase("sre")) {
			throw new ServiceRuntimeException("SRE thrown in SCAFaultHandler:oneWay");
		} else if ((oneWayRequest).equalsIgnoreCase("sbeND")) {
			DataObject sbe = DataFactory.INSTANCE.create("http://myLibrary", "FaultNotDeclaredFault");
			sbe.setString("faultMessage", "Non declared SBE thrown in SCAFaultHandler:oneWay");
			throw new ServiceBusinessException(sbe);
		}
	}
}