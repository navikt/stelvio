package no.nav.java;

import java.util.ArrayList;
import java.util.List;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;

public class invokeResponseTimeTestImpl {
	/**
	 * Default constructor.
	 */
	public invokeResponseTimeTestImpl() {
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
	 * named "ResponseTimePartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _ResponseTimePartner = null;

	public Service locateService_ResponseTimeSCAPartner() {
		if (_ResponseTimePartner == null) {
			_ResponseTimePartner = (Service) ServiceManager.INSTANCE
					.locateService("ResponseTimeSCAPartner");
		}
		return _ResponseTimePartner;
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "ResponseTimePartner1".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _ResponseTimePartner1 = null;

	public Service locateService_ResponseTimeWSPartner() {
		if (_ResponseTimePartner1 == null) {
			_ResponseTimePartner1 = (Service) ServiceManager.INSTANCE
					.locateService("ResponseTimeWSPartner");
		}
		return _ResponseTimePartner1;
	}

	/**
	 * Method generated to support implementation of operation "measureResponseTimes" defined for WSDL port type 
	 * named "ResponseTimeTest".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String measureResponseTimes(DataObject measureResponseTimesInput) {
		int numberOfCalls = measureResponseTimesInput.getInt("numberOfCalls");
		
		boolean testWithList = measureResponseTimesInput.getBoolean("testWithList");
		
		// create message corresponding to the desired message size
		String messageString = createString(measureResponseTimesInput.getInt("messageSize"));
		
		String operationName;
		DataObject inputObject = null;
		
		if(testWithList){
			inputObject = DataFactory.INSTANCE.create("http://nav-lib-responsetime/no/nav/data", "Listewrapper");
			inputObject.setList("stringList", createStringList(numberOfCalls, messageString));
			operationName = "measureResponsetimeList";
		}
		else {
			inputObject = DataFactory.INSTANCE.create("http://nav-lib-responsetime/no/nav/data", "Input");
			inputObject.setString("string", messageString);
			operationName = "measureResponsetime";
		}
		
				
		//invoke SCA
		String scaResponse;
		Service scaService = locateService_ResponseTimeSCAPartner();
		long start, stop, responseTime, avgTimeSCA, avgTimeWS;
		long totalTime = 0;
		for(int i=0;i<numberOfCalls;i++){
			start = System.nanoTime();
			scaService.invoke(operationName, inputObject);
			stop = System.nanoTime();
			
			responseTime = stop-start;
			//System.out.println("responstid sca: " + responseTime + " ns.");
			totalTime+=responseTime;
		}
		
		avgTimeSCA = totalTime/numberOfCalls;
		
		//invoke WS
		String wsResponse;
		Service wsService = locateService_ResponseTimeWSPartner();
		totalTime = 0;
		for(int i=0;i<numberOfCalls;i++){
			start = System.nanoTime();
			wsService.invoke(operationName, inputObject);
			stop = System.nanoTime();
			
			responseTime = stop-start;
			//System.out.println("responstid ws: " + responseTime + " ns.");
			totalTime+=responseTime;
		}
		
		avgTimeWS = totalTime/numberOfCalls;
		
		
		return "gjennomsnittlig responsetid SCA: " + avgTimeSCA/1000 + " mikrosek, WS: " + avgTimeWS/1000 + " mikrosek.";
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "ResponseTime#measureResponsetime(String measureResponsetimeInput)"
	 * of wsdl interface "ResponseTime"	
	 */
	public void onMeasureResponsetimeResponse(Ticket __ticket,
			String returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}
	
	private String createString(int length){
		char[] chars = new char[length];
		for (int i=0;i<length;i++){
			chars[i] = 'x';
		}
		
		return new String(chars);
	}
	
	private List createStringList(int length, String messageString){
		ArrayList stringList = new ArrayList();
		for (int i=0; i<length; i++){
			stringList.add(i, messageString);
		}
		return stringList;
	}

}