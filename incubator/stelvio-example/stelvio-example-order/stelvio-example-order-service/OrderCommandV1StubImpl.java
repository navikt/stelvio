import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;

public class OrderCommandV1StubImpl {
	/**
	 * Default constructor.
	 */
	public OrderCommandV1StubImpl() {
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
	 * Method generated to support implementation of operation "createOrder" defined for WSDL port type named "OrderCommand".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter type conveys that it is a complex type.
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public DataObject createOrder(DataObject createOrderRequest) {
		return null;
	}

	/**
	 * Method generated to support implementation of operation "cancelOrder" defined for WSDL port type named "OrderCommand".
	 * 
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public void cancelOrder(String orderId) {
	}

	/**
	 * Method generated to support implementation of operation "addItemsToOrder" defined for WSDL port type named
	 * "OrderCommand".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter type conveys that it is a complex type.
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public DataObject addItemsToOrder(DataObject addItemsToOrderRequest) {
		return null;
	}

	/**
	 * Method generated to support implementation of operation "removeItemsFromOrder" defined for WSDL port type named
	 * "OrderCommand".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter type conveys that it is a complex type.
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public DataObject removeItemsFromOrder(DataObject removeItemsFromOrderRequest) {
		return null;
	}
}