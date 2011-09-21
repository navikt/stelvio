
package no.nav.dh.cics.tps;

import com.ibm.etools.marshall.util.*;

import commonj.connector.runtime.DataBindingException;
import commonj.sdo.DataObject;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @generated
 * Generated Class: TpsPersonDataResDataBinding
 */

public class TpsPersonDataResDataBinding  implements javax.resource.cci.Streamable, com.ibm.etools.marshall.RecordBytes, commonj.connector.runtime.RecordDataBinding
{
	/**
	 *  @generated 
	 */
	private static final long serialVersionUID = -4479665242126080068L;
	/**
	 * @generated
	 */
	private byte[] buffer_ = null;
	/**
	 * @generated
	 */
	private static int bufferSize_;
	/**
	 * @generated
	 */
	private static byte[] initializedBuffer_;
	
	/**
	 * @generated
	 */
	private static com.ibm.websphere.bo.BOFactory BOfactory_ = null;

	/**
	 * @default norwegian z/OS charset
	 */
	private static final String CICS_DEFAULT_LANGUAGE_ID = "IBM-277";

	/**
	* Logger - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	*/
	private final static String className = TpsPersonDataResDataBinding.class.getName();
	private final Logger log = Logger.getLogger(className);

	
	static 
    {
        bufferSize_ = 30000;
        initializedBuffer_ = new byte[bufferSize_];
        String xMLDATAOUTInitialValue = " ";
        MarshallStringUtils.marshallFixedLengthStringIntoBuffer(xMLDATAOUTInitialValue, initializedBuffer_, 0, CICS_DEFAULT_LANGUAGE_ID, 30000, 0, " ");
    }

	
	/**
	 * constructor
	 * @generated
	 */
	public TpsPersonDataResDataBinding()
	{
		bufferSize_ = 30000;
		initializedBuffer_ = new byte[bufferSize_];
		String xMLDATAOUTInitialValue = " ";
		MarshallStringUtils.marshallFixedLengthStringIntoBuffer (xMLDATAOUTInitialValue, initializedBuffer_, 0, CICS_DEFAULT_LANGUAGE_ID, 30000, MarshallStringUtils.STRING_JUSTIFICATION_LEFT, " ");
		buffer_ = new byte[bufferSize_];
		System.arraycopy (initializedBuffer_, 0, buffer_, 0, bufferSize_);
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Streamable#read(java.io.InputStream)
	 */
    public void initialize()
    {
        buffer_ = new byte[bufferSize_];
        System.arraycopy(initializedBuffer_, 0, buffer_, 0, bufferSize_);
    }
	
	/**
	 * @generated
	 * @see javax.resource.cci.Streamable#read(java.io.InputStream)
	 */
	public void read (java.io.InputStream inputStream) throws java.io.IOException
	{
		byte[] input = new byte[inputStream.available()];
		inputStream.read(input);
		buffer_ = input;
        log.logp(Level.FINE, className, "read()", (new StringBuilder("CICS TPS BufferLength: ")).append(buffer_.length).toString());
        log.logp(Level.FINE, className, "read()", (new StringBuilder("CICS TPS BufferContent(IBM-277): ")).append(MarshallStringUtils.unmarshallFixedLengthStringFromBuffer(buffer_, 0, CICS_DEFAULT_LANGUAGE_ID, buffer_.length)).toString());
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Streamable#write(java.io.OutputStream)
	 */
	public void write (java.io.OutputStream outputStream) throws java.io.IOException
	{
		outputStream.write (buffer_);
        log.logp(Level.FINE, className, "write()", (new StringBuilder("CICS BufferLength: ")).append(buffer_.length).toString());
        log.logp(Level.FINE, className, "write()", (new StringBuilder("CICS BufferContent(IBM-277): ")).append(MarshallStringUtils.unmarshallFixedLengthStringFromBuffer(buffer_, 0, CICS_DEFAULT_LANGUAGE_ID, buffer_.length)).toString());
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#getRecordName()
	 */
	public String getRecordName()
	{
		return (this.getClass().getName());
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#setRecordName(String)
	 */
	public void setRecordName (String recordName)
	{
		return;
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#setRecordShortDescription(String)
	 */
	public void setRecordShortDescription (String shortDescription)
	{
		return;
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#getRecordShortDescription()
	 */
	public String getRecordShortDescription()
	{
		return (this.getClass().getName());
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#clone()
	 */
	public Object clone() throws CloneNotSupportedException
	{
		return (super.clone());
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#equals
	 */
	public boolean equals (Object object)
	{
		return (super.equals(object));
	}

	/**
	 * @generated
	 * @see javax.resource.cci.Record#hashCode
	 */
	public int hashCode()
	{
		return (super.hashCode());
	}

	/**
	 * @generated
	 * @see com.ibm.etools.marshall.RecordBytes#getBytes
	 */
	public byte[] getBytes()
	{
		return (buffer_);
	}

	/**
	 * @generated
	 * @see com.ibm.etools.marshall.RecordBytes#setBytes
	 */
	public void setBytes (byte[] bytes)
	{
		buffer_ = bytes;
	}
	
	/**
	 * @generated
	 * @see com.ibm.etools.marshall.RecordBytes#getSize
	 */
	public int getSize()
	{
		return (30000);

	}

	/**
	 * @generated
	 */
	public boolean match (Object obj)
	{
		if (obj == null)
			return (false);
		if (obj.getClass().isArray())
		{
			byte[] currBytes = buffer_;
			try
			{
				byte[] objByteArray = (byte[])obj;
				if (objByteArray.length != buffer_.length)
					return (false);
				buffer_ = objByteArray;
			}
			catch (ClassCastException exc)
			{
				return (false);
			}
			finally
			{
				buffer_ = currBytes;
			}
		}
		else
			return (false);
		
		return (true);

	}

	/**
	 * @generated
	 */
	public void populate (Object obj)
	{
		if (obj.getClass().isArray())
		{
			try {
				buffer_ = (byte[]) obj;
			} catch (ClassCastException exc) { }
		}
	}

	/**
	 * @generated
	 * @see java.lang.Object#toString
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append ("\n");
		ConversionUtils.dumpBytes (sb, buffer_);
		return (sb.toString());
	}
	
	/**
	 * @generated
	 */
	public String getXMLDATAOUT()
	{
		String xMLDATAOUT = null;
		xMLDATAOUT = MarshallStringUtils.unmarshallFixedLengthStringFromBuffer (buffer_, 0, CICS_DEFAULT_LANGUAGE_ID, 30000);
		// LS, trim the unecessary spaces...
		xMLDATAOUT = xMLDATAOUT.trim();
		return (xMLDATAOUT);
	}

	/**
	 * @generated
	 */
	public void setXMLDATAOUT (String xMLDATAOUT)
	{
		if (xMLDATAOUT != null) {
			if (xMLDATAOUT.length() > 30000)
				throw new IllegalArgumentException(MarshallResource.instance().getString(MarshallResource.IWAA0124E, xMLDATAOUT, "30000", "XML__DATA_OUT"));
			MarshallStringUtils.marshallFixedLengthStringIntoBuffer (xMLDATAOUT, buffer_, 0, CICS_DEFAULT_LANGUAGE_ID, 30000, MarshallStringUtils.STRING_JUSTIFICATION_LEFT, " ");
		}
	}

	/**
	 * @generated
	 */
	public DataObject getDataObject() throws DataBindingException
	{
		if (BOfactory_ == null)
		{
			synchronized (initializedBuffer_)
			{
				if (BOfactory_ == null)
				{
					com.ibm.websphere.sca.ServiceManager serviceManager = new com.ibm.websphere.sca.ServiceManager();
					BOfactory_ = (com.ibm.websphere.bo.BOFactory)serviceManager.locateService("com/ibm/websphere/bo/BOFactory");
				}
			}
		}
		DataObject dataObject = BOfactory_.create("http://nav-tjeneste-person/no/nav/asbo", "tpsPersonDataRes");

		try { dataObject.setString ("XML_DATA_OUT", getXMLDATAOUT()); } catch (Exception exc) {}

		log.logp(Level.FINE, className, "getDataObject()", "DataObject: " + dataObject.toString());
		
		return (dataObject);
	}

	/**
	 * @generated
	 */
	public void setDataObject (DataObject dataObject) throws DataBindingException
	{
		if (dataObject.isSet ("XML_DATA_OUT"))
			setXMLDATAOUT (dataObject.getString ("XML_DATA_OUT"));
		log.logp(Level.FINE, className, "setDataObject()", "DataObject: " + dataObject.toString());
	}
}