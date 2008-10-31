/*
 * Created on Oct 19, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.j2ca.adldap;

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
public class ADLDAPAdapterRecord extends UnstructuredRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//	for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterRecord.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterRecord"; 
	
	/**
	 * @generated
	 */
	private static com.ibm.websphere.bo.BOFactory BOfactory_ = null;


	/**
	 * @BO attributer
	 */

	private String sAMAccountName;
	private String displayName;
	private String givenName;
	private String sn;
	private String mail;

	/**
	 * @BO helper attributer not within the BO
	 */
	private String ADobjectClass = "objectClass=user";

	
	/**
	 * to get the request BO 
	 */
	public ADLDAPAdapterRecord(Record record) 
	{
		DataObject dataobject= ((DataObjectRecord)record).getDataObject();
		setDataObject(dataobject);
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterRecord()", "ADLDAPAdapter requestObject: " + dataobject.toString());
	}
	
	/**
	 * to set the response BO 
	 */
	public ADLDAPAdapterRecord() 
	{
		// which isn't done here --> ADLDAPAdapterInteraction
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
		return ADLDAPAdapterConstants.BO_NAME;
	}

	/**
	 * @return Returns the boNamespace.
	 */
	public String getBoNamespace() {
		return ADLDAPAdapterConstants.BO_NAMESPACE;
	}
	
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/**
	 * @return Returns the givenName.
	 */
	public String getGivenName() {
		return givenName;
	}
	/**
	 * @param givenName The givenName to set.
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	/**
	 * @return Returns the mail.
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail The mail to set.
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @return Returns the sAMAccountName.
	 */
	public String getSAMAccountName() {
		return sAMAccountName;
	}
	/**
	 * @param accountName The sAMAccountName to set.
	 */
	public void setSAMAccountName(String accountName) {
		sAMAccountName = accountName;
	}
	/**
	 * @return Returns the sn.
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn The sn to set.
	 */
	public void setSn(String sn) {
		this.sn = sn;
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

		DataObject dataObject = BOfactory_.create(ADLDAPAdapterConstants.BO_NAMESPACE, ADLDAPAdapterConstants.BO_NAME);
		dataObject.setString (ADLDAPAdapterConstants.BO_SAMACCOUNTNAME, getSAMAccountName());
		dataObject.setString (ADLDAPAdapterConstants.BO_DISPLAYNAME, getDisplayName());
		dataObject.setString (ADLDAPAdapterConstants.BO_GIVENNAME, getGivenName());
		dataObject.setString (ADLDAPAdapterConstants.BO_SN, getSn());
		dataObject.setString (ADLDAPAdapterConstants.BO_MAIL, getMail());
	
		return (dataObject);
	}
	
	/**
	 * @generated
	 */
	public void setDataObject (DataObject dataObject)
	{
		setSAMAccountName(dataObject.getString(ADLDAPAdapterConstants.BO_SAMACCOUNTNAME));
		setDisplayName(dataObject.getString(ADLDAPAdapterConstants.BO_DISPLAYNAME));
		setGivenName(dataObject.getString(ADLDAPAdapterConstants.BO_GIVENNAME));
		setSn(dataObject.getString(ADLDAPAdapterConstants.BO_SN));
		setMail(dataObject.getString(ADLDAPAdapterConstants.BO_MAIL));
	}	

	/**
	 * @return Returns the aDobjectClass.
	 */
	public String getADobjectClass() {
		return ADobjectClass;
	}
}
