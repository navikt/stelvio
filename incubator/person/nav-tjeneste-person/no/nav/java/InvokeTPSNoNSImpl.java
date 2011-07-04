package no.nav.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

public class InvokeTPSNoNSImpl {

	private final static String className = InvokeTPSNoNSImpl.class.getName();

	private final Logger log = Logger.getLogger(className);

	final static String currentModulName = "nav-tjeneste-person";

	/**
	 * Default constructor.
	 */
	public InvokeTPSNoNSImpl() {
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
	 * named "TPSPersonPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_TPSPersonPartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("TPSPersonPartner");
	}

	/**
	 * Method generated to support implemention of operation "A0" defined for WSDL port type 
	 * named "interface.tps".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentTPSData(DataObject tpsPersonDataReq) {
		if (tpsPersonDataReq.getDataObject("tpsServiceRutine") != null) {
			// transform to XML string for CTG
			BOXMLSerializer xmlSerializer = (BOXMLSerializer) new ServiceManager()
					.locateService("com/ibm/websphere/bo/BOXMLSerializer");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			try {
				Type type = tpsPersonDataReq.getType();
				xmlSerializer.writeDataObject(tpsPersonDataReq, type
						.getURI(), "tpsPersonData", baos);
			} catch (IOException e) {

				log.logp(Level.SEVERE, className, "hentTPSData",
						"Technical problem occured with converting request DataObject to XML: "
								+ e.getMessage());
				throw new ServiceRuntimeException(e);
			}

			String boxml = new String(baos.toByteArray());
			//create the CTG DataObject XML_IN 
			DataObject ctgObjReq = DataFactory.INSTANCE.create(
					"http://nav-tjeneste-person/no/nav/asbo", "tpsPersonDataReq");
			ctgObjReq.setString("XML_DATA_IN", boxml);
			DataObject ctgObjRes = (DataObject) locateService_TPSPersonPartner()
					.invoke("hentTPSData", ctgObjReq);

			if (ctgObjRes != null) {
				ctgObjRes = ctgObjRes.getDataObject(0);

				ByteArrayInputStream bais = null;
				try {
					bais = new ByteArrayInputStream(ctgObjRes
							.getString(0).getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					log.logp(Level.SEVERE, className, "hentTPSData",
							"Technical problem occured when reading TPS data: "
									+ e.getMessage());
					throw new ServiceRuntimeException(e);
				}
				BOXMLDocument doc = null;

				try {
					doc = xmlSerializer.readXMLDocument(bais);
				} catch (IOException e) {
					log.logp(Level.SEVERE, className, "hentTPSData",
							"Technical problem occured with converting response XML to DataObject: "
									+ e.getMessage());
					throw new ServiceRuntimeException(e);
				}

				DataObject result = doc.getDataObject();
				trimWhiteSpace(result);
				return result;
			} else {
				log.logp(Level.SEVERE, className, "hentTPSData",
						"TPS reponse object is empty!");
				return null;
			}
		} else {
			log
					.logp(Level.SEVERE, className, "hentTPSData",
							"TPS request object was empty - don't spam the TPS system.");
			return null;
		}
	}
	
	/**
	 * Trims whitespace from all properties of type String (recursively).
	 * TODO: Move this method to stelvio-commons-lib
	 * @param dataObject the data object to trim whitepace on
	 */
	@SuppressWarnings("unchecked")
	private void trimWhiteSpace(DataObject dataObject) {
		if (dataObject != null) {
			for (Property property : (List<Property>) dataObject.getType().getProperties()) {
				Object propertyValue = dataObject.get(property);
				if (propertyValue != null) {
					if (propertyValue instanceof DataObject) {
						trimWhiteSpace((DataObject) propertyValue);
					} else if (propertyValue instanceof String) {
						propertyValue = ((String) propertyValue).trim();
						dataObject.set(property, propertyValue);
					} else if (propertyValue instanceof List) {
						List propertyValueAsList = (List) propertyValue;
						for (int i = 0; i < propertyValueAsList.size(); i++) {
							Object listElement = propertyValueAsList.get(i);
							if (listElement instanceof DataObject) {
								trimWhiteSpace((DataObject) listElement);
							} else if (listElement instanceof String) {
								propertyValueAsList.set(i, ((String) listElement).trim());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.tpsNoNS#A0(DataObject tpsPersonDataReq)"
	 * of wsdl interface "interface.tpsNoNS"	
	 */
	public void onHentTPSDataResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}