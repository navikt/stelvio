import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;

public class OrderQueryV1StubImpl {
	/**
	 * Default constructor.
	 */
	public OrderQueryV1StubImpl() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this implementation class. This method should be used when
	 * passing this service to a partner reference or if you want to invoke this component service asynchronously.
	 * 
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * Method generated to support implementation of operation "getOrder" defined for WSDL port type named "OrderQuery".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter type conveys that it is a complex type.
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public DataObject getOrder(String orderId) {
		return OrderFactory.createOrder();
	}
}