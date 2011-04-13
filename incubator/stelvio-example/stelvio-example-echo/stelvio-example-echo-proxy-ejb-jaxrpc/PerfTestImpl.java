
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import ejbs.LargeGraph1;
import com.ibm.websphere.sca.ServiceManager;

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
	 * {@link LargeGraph1}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link LargeGraph1Async}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return LargeGraph1
	 */
	public LargeGraph1 locateService_LargeGraphPartner() {
		return (LargeGraph1) ServiceManager.INSTANCE
				.locateService("LargeGraphPartner");
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link ejbs.LargeGraph1#echo(String aString))
	 * of java interface (@link ejbs.LargeGraph1)	
	 * @see ejbs.LargeGraph1#echo(String aString)	
	 */
	public void onEchoResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}