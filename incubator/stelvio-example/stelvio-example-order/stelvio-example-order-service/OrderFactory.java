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
		orderLineItem.setInt("quantity", 1);
		DataObject additionalInfo = orderLineItem.createDataObject("additionalInfo");
		DataObject discount = additionalInfo.createDataObject("discount");
		// TODO: Is this the best way to set simpleContent of a complex type. It seems like a 'magic value'
		discount.setFloat("value", 0.25f);
		// TODO: For some reason this value needs to be set - even if it has a fixed value in schema.
		discount.set("sinceVersion", "1.1");
		
		
		//Validate order
		BusinessObjectValidator.assertValidBusinessObject(order);
		
		return order;
	}
	
	private static BOFactory getFactoryService() {
		return (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
	}
}
