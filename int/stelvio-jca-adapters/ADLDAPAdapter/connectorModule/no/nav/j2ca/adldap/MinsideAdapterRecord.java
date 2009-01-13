package no.nav.j2ca.adldap;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.resource.cci.Record;

import com.ibm.j2ca.base.DataObjectRecord;
import com.ibm.j2ca.base.UnstructuredRecord;
import commonj.sdo.DataObject;

/**
 * This class encapsulate a LDAP record for entries in the 'Minside' directory.
 * The class does also override <code>DataObject</code> methods from the 
 * superclass <code>UnstructuredRecord</code>
 * 
 * @author Andreas Roe 
 */
public class MinsideAdapterRecord extends UnstructuredRecord {

	/** serialVersionUID */
	private static final long serialVersionUID = 3964740168558483778L;

	/** Private logger */
	private static Logger log = Logger.getLogger(MinsideAdapterRecord.class.getName());
	
	/** Private String for reuse in log statements */
	private final String CLASSNAME = this.getClass().getSimpleName();
	
	/** Class level factory */
	private static com.ibm.websphere.bo.BOFactory BOfactory_ = null;
	
	/** Class level variable used for fnr (social security number) */
	private String fnr;
	
	/** Class level variable used for name */
	private String navn;
	
	/** Class level variable used for address */
	private String adresse;
	
	/** Class level variable used for language */
	private String sprak;

	public MinsideAdapterRecord() {
		
	}
	
	public MinsideAdapterRecord(Record record) {
		DataObject dataobject= ((DataObjectRecord)record).getDataObject();
		setDataObject(dataobject);
		log.logp(Level.FINE, CLASSNAME, "MinsideAdapterRecord()", "Creating new instance (" + dataobject.toString() + ")");
	}
	
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	/**
	 * @return the fnr
	 */
	public String getFnr() {
		return fnr;
	}

	/**
	 * @param fnr the fnr to set
	 */
	public void setFnr(String fnr) {
		this.fnr = fnr;
	}

	/**
	 * @return the navn
	 */
	public String getNavn() {
		return navn;
	}

	/**
	 * @param navn the navn to set
	 */
	public void setNavn(String navn) {
		this.navn = navn;
	}

	/**
	 * @return the sprak
	 */
	public String getSprak() {
		return sprak;
	}

	/**
	 * @param sprak the sprak to set
	 */
	public void setSprak(String sprak) {
		this.sprak = sprak;
	}

	public DataObject getDataObject() {
		if (BOfactory_ == null)	{
			com.ibm.websphere.sca.ServiceManager serviceManager = new com.ibm.websphere.sca.ServiceManager();
			BOfactory_ = (com.ibm.websphere.bo.BOFactory)serviceManager.locateService("com/ibm/websphere/bo/BOFactory");
		}
		
		DataObject dataObject = BOfactory_.create(ADLDAPAdapterConstants.MINSIDE_BO_NAMESPACE, ADLDAPAdapterConstants.MINSIDE_BO_NAME);
		
		if (null != fnr && !"".equals(fnr)) {
			dataObject.setString (ADLDAPAdapterConstants.MINSIDE_BO_FNR, this.getFnr());
		} 
		
		if (null != navn && !"".equals(navn)) {
			dataObject.setString (ADLDAPAdapterConstants.MINSIDE_BO_NAVN, this.getNavn());
		}
		
		if (null != adresse && !"".equals(adresse)) {
			dataObject.setString (ADLDAPAdapterConstants.MINSIDE_BO_ADRESSE, this.getAdresse());
		}
		
		if (null != sprak && !"".equals(sprak)) {
			dataObject.setString (ADLDAPAdapterConstants.MINSIDE_BO_SPRAK, this.getSprak());
		}
		
		return dataObject;
	}
	
	public void setDataObject(DataObject dataObject) {
		String fnr = dataObject.getString(ADLDAPAdapterConstants.MINSIDE_BO_FNR); 
		if (null != fnr ) this.setFnr(fnr); 
		String navn = dataObject.getString(ADLDAPAdapterConstants.MINSIDE_BO_NAVN);
		if (null != navn) this.setNavn(navn);
		String adresse = dataObject.getString(ADLDAPAdapterConstants.MINSIDE_BO_ADRESSE); 
		if (null != adresse) this.setAdresse(adresse);
		String sprak = dataObject.getString(ADLDAPAdapterConstants.MINSIDE_BO_SPRAK); 
		this.setSprak(sprak);
	}
	
	
}
