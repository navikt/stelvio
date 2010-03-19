package no.stelvio.example.person.stub;

import commonj.sdo.DataObject;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;

public class PersonServiceV2StubImpl {
	/**
	 * Default constructor.
	 */
	public PersonServiceV2StubImpl() {
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
	 * Method generated to support implementation of operation "getPerson" defined for WSDL port type named "PersonService".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter type conveys that it is a complex type.
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public DataObject getPerson(DataObject getPersonRequest) {
		BOFactory factoryService = getFactoryService();

		String id = getPersonRequest.getString("id");
		try {
			Long.parseLong(id);
			DataObject getPersonResponse = factoryService.create("http://www.stelvio.no/example/person/provider/V1",
					"GetPersonResponse");
			DataObject person = getPersonResponse.createDataObject("person");
			person.set("id", id);
			DataObject name = person.createDataObject("name");
			name.set("firstName", "Ola");
			name.set("lastName", "Nordmann");
			return getPersonResponse;
		} catch (NumberFormatException e) {
			DataObject personNotFoundFault = factoryService.create("http://www.stelvio.no/example/person/provider/V1",
					"PersonNotFoundFault");
			personNotFoundFault.set("id", id);
			throw new ServiceBusinessException(personNotFoundFault);
		}

	}

	/**
	 * Method generated to support implementation of operation "findPerson" defined for WSDL port type named "PersonService".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter type conveys that it is a complex type.
	 * Please refer to the WSDL Definition for more information on the type of input, output and fault(s).
	 */
	public DataObject findPerson(DataObject findPersonRequest) {
		BOFactory factoryService = getFactoryService();

		DataObject findPersonResponse = factoryService.create("http://www.stelvio.no/example/person/provider/V2",
				"FindPersonResponse");
		
		DataObject personList = findPersonResponse.createDataObject("personList");
		
		DataObject person = personList.createDataObject("person");
		person.set("id", 1);
		DataObject name = person.createDataObject("name");
		name.set("firstName", "Ola");
		name.set("lastName", "Nordmann");
		
		person = personList.createDataObject("person");
		person.set("id", 2);
		name = person.createDataObject("name");
		name.set("firstName", "Kari");
		name.set("lastName", "Nordmann");
		
		findPersonResponse.setInt("totalHits", 2);
		
		return findPersonResponse;
	}

	private BOFactory getFactoryService() {
		return (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
	}
}