package no.stelvio.common.systemavailability.config;

import java.io.IOException;
import java.util.*;

import commonj.sdo.DataObject;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.sdo.DataFactory;

import no.stelvio.common.bus.util.ErrorHelperUtil;
import no.stelvio.common.systemavailability.*;

public class SystemAvailabilityConfigImpl {
	
	private static final String SYSTEM_AVAILABILITY_CONFIG_NAMESPACE = "http://system-availability-config";
	
	private static final String MODULE_NAME = "system-availability-config";
	private static final String FAULT_NAMESPACE = "http://system-availability-config/no/stelvio/fault";
	private static final String FAULT_NAME = "FaultUpdateStubbingfilesFailed";
	
	/**
	 * Default constructor.
	 */
	public SystemAvailabilityConfigImpl() {
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
	 * Method generated to support implemention of operation "enableStubs" defined for WSDL port type 
	 * named "Stubbing".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject enableStubs(DataObject enableStubsRequest) {
		String response = "Failure";
		if (enableStubsRequest != null) {
			String systemName = enableStubsRequest.getString("system");
			List operations = enableStubsRequest.getList("operations");
			boolean enabled = enableStubsRequest.getBoolean("enabled");
			if (systemName != null && operations != null) {
				SystemAvailabilityStorage systemAvailabilityStorage = new SystemAvailabilityStorage();
				AvailabilityRecord availabilityRecord = systemAvailabilityStorage.getAvailabilityRecord(systemName);
			
				for (Object operation : operations) {
					String operationName = (String) operation;
					OperationAvailabilityRecord operationAvailabilityRecord = availabilityRecord.findOrCreateOperation(operationName);
					if (operationAvailabilityRecord != null) {
						operationAvailabilityRecord.stubbed = enabled;
					}
					systemAvailabilityStorage.storeAvailabilityRecord(availabilityRecord);
				}
			}
			response = "Success";
		}
		DataObject enableStubsResponse = DataFactory.INSTANCE.create(SYSTEM_AVAILABILITY_CONFIG_NAMESPACE, "EnableStubsResponse");
		enableStubsResponse.setString("response", response);
		return enableStubsResponse;
	}

	/**
	 * Method generated to support implementation of operation "enableStubsAllOperations" defined for WSDL port type 
	 * named "Stubbing".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public Boolean enableStubsAllOperations(Boolean stubbing) {
		SystemAvailabilityStorage storage = new SystemAvailabilityStorage();
		if (stubbing != null && stubbing.booleanValue() == true) {
			storage.turnOnAllStubs();
			return true;
		}
		else if (stubbing != null && stubbing.booleanValue() == false) {
			storage.turnOffAllStubs();
			return true;
		}
		return false;
	}

	/**
	 * Method generated to support implementation of operation "enableRecordingAllOperations" defined for WSDL port type 
	 * named "Stubbing".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public Boolean enableRecordingAllOperations(Boolean recording) {
		SystemAvailabilityStorage storage = new SystemAvailabilityStorage();
		if (recording != null && recording.booleanValue() == true) {
			storage.turnOnAllRecords();
			return true;
		}
		else if (recording != null && recording.booleanValue() == false) {
			storage.turnOffAllRecords();
			return true;
		}
		return false;
	}
}