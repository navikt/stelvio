import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class PerfTestImpl {
	/**
	 * Default constructor.
	 */
	public PerfTestImpl() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this implementation
	 * class.  This method should be used when passing this service to a partner reference
	 * or if you want to invoke this component service asynchronously.    
	 *
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "LargeGraphPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _LargeGraphPartner = null;

	public Service locateService_LargeGraphPartner() {
		if (_LargeGraphPartner == null) {
			_LargeGraphPartner = (Service) ServiceManager.INSTANCE
					.locateService("LargeGraphPartner");
		}
		return _LargeGraphPartner;
	}

	/**
	 * Method generated to support implementation of operation "test" defined for WSDL port type 
	 * named "Test".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void test(Integer numInvokes) {
		long startTime = System.currentTimeMillis();
		System.out
				.println("Started " + numInvokes + " invokes at " + startTime);

		for (int i = 0; i < numInvokes.intValue(); i++) {
			locateService_LargeGraphPartner().invoke("echo", "Quack");
		}

		long stopTime = System.currentTimeMillis();
		long duration = stopTime - startTime;
		double rate = 1000 * numInvokes / duration;
		System.out.println("Finished at " + stopTime + ", processing took "
				+ duration + " ms, rate is " + rate + " tps");
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "Echo#echo(String arg0)"
	 * of wsdl interface "Echo"	
	 */
	public void onEchoResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}