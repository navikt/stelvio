/*
 * Created on Oct 19, 2007
 *
 */
package no.nav.j2ca.adldap;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.resource.cci.Record;

import com.ibm.j2ca.base.UnstructuredRecord;
import com.ibm.j2ca.base.DataObjectRecord;
import commonj.sdo.DataObject;

/**
 * @author lsb2812
 *
 * Record To BO class
 */
public class NDUListADLDAPAdapterRecord extends UnstructuredRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//	for logging
	private static Logger log = Logger.getLogger(NDUListADLDAPAdapterRecord.class.getName());
	private static final String CLASSNAME = "NDUListADLDAPAdapterRecord"; 
	
	/**
	 * @generated
	 */
	private static com.ibm.websphere.bo.BOFactory BOfactory_ = null;


	/**
	 * @BO attributer
	 */
	private List ansattListe;
	private List ikkeFunnetListe;

	
	/**
	 * to get the request BO 
	 */
	public NDUListADLDAPAdapterRecord(Record record) 
	{
		DataObject dataobject= ((DataObjectRecord)record).getDataObject();
		setDataObject(dataobject);
		log.logp(Level.FINE, CLASSNAME, "NDUListADLDAPAdapterRecord()", "NDUListADLDAPAdapter requestObject: " + dataobject.toString());
	}
	
	/**
	 * to set the response BO 
	 */
	public NDUListADLDAPAdapterRecord() 
	{
		// which isn't done here --> NDUListADLDAPAdapterRecord
	}
	
	/**
	 * @generated
	 */
	public String getRecordName()
	{
		return (this.getClass().getName());
	}
	
	/**
	 * @return Returns the boName.
	 */
	public String getBoName() {
		return ADLDAPAdapterConstants.NDU_BO_NAME;
	}

	/**
	 * @return Returns the boNamespace.
	 */
	public String getBoNamespace() {
		return ADLDAPAdapterConstants.NDU_BO_NAMESPACE;
	}
	
	/**
	 * @return Returns the ansattListe.
	 */
	public List getAnsattListe() {
		return ansattListe;
	}
	/**
	 * @param ansattListe The ansattListe to set.
	 */
	public void setAnsattListe(List adList)
	{
		this.ansattListe = adList;
	}
	
	/**
	 * @return Returns the ikkeFunnetListe.
	 */
	public List getIkkeFunnetListe() {
		return ikkeFunnetListe;
	}
	/**
	 * @param ikkeFunnetListe The ikkeFunnetListe to set.
	 */
	public void setIkkeFunnetListe(List ikkeFunnet)
	{
		this.ikkeFunnetListe = ikkeFunnet;
	}
	
	/**
	 * @return
	 */
	public DataObject getDataObject()
	{
		if (BOfactory_ == null)
		{
			com.ibm.websphere.sca.ServiceManager serviceManager = new com.ibm.websphere.sca.ServiceManager();
			BOfactory_ = (com.ibm.websphere.bo.BOFactory)serviceManager.locateService("com/ibm/websphere/bo/BOFactory");
		}

		DataObject dataObject = BOfactory_.create(ADLDAPAdapterConstants.NDU_BO_NAMESPACE, ADLDAPAdapterConstants.NDULIST_BO_NAME);
		dataObject.setList (ADLDAPAdapterConstants.NDULIST_BO_LIST, getAnsattListe());
		dataObject.setList (ADLDAPAdapterConstants.NDULIST_BO_NOT_FOUND_LIST, getIkkeFunnetListe());
		return (dataObject);
	}
	
	/**
	 * @generated
	 */
	public void setDataObject (DataObject dataObject)
	{
		setAnsattListe(dataObject.getList(ADLDAPAdapterConstants.NDULIST_BO_LIST));
		setIkkeFunnetListe(dataObject.getList(ADLDAPAdapterConstants.NDULIST_BO_NOT_FOUND_LIST));
	}
}
