import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.sca.ServiceManager;

import commonj.sdo.DataObject;


public class OrderFactory {
	public static DataObject createOrder() {
		DataObject order = getFactoryService().create("http://www.stelvio.no/example/order/V1","Order");
		order.set("id", 1234);
		order.set("customerId", 1234);
		DataObject orderLineItem = order.createDataObject("orderLineItem");
		orderLineItem.set("id", 1234);
		orderLineItem.set("productId", 1234);
		orderLineItem.set("quantity", 1);
		return order;
	}
	
	private static BOFactory getFactoryService() {
		return (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
	}
}
