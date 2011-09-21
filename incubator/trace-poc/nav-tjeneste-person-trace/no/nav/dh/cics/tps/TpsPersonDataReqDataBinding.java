
package no.nav.dh.cics.tps;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.etools.marshall.util.ConversionUtils;
import com.ibm.etools.marshall.util.MarshallResource;
import com.ibm.etools.marshall.util.MarshallStringUtils;
import commonj.connector.runtime.DataBindingException;
import commonj.sdo.DataObject;


/**
 * @generated
 * Generated Class: TpsPersonDataReqDataBinding
 */

public class TpsPersonDataReqDataBinding  implements javax.resource.cci.Streamable, com.ibm.etools.marshall.RecordBytes, commonj.connector.runtime.RecordDataBinding
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7903563995729085487L;
	
	// Logger - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = TpsPersonDataReqDataBinding.class.getName();
	private Logger log = Logger.getLogger(className);
	private static final String CICS_DEFAULT_LANGUAGE_ID = "IBM-277";
	
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
	 * @generated
	 */
	@SuppressWarnings("unused")
	private HashMap valFieldNameMap_;
	
	/**
	 * @generated
	 */
	static 
    {
        bufferSize_ = 30000;
        initializedBuffer_ = new byte[bufferSize_];
        String xMLDATAINInitialValue = " ";
        MarshallStringUtils.marshallFixedLengthStringIntoBuffer(xMLDATAINInitialValue, initializedBuffer_, 0, "IBM-277", 10000, 0, " ");
    }
	
	/**
	 * constructor
	 * @generated
	 */

    public TpsPersonDataReqDataBinding()
    {
        buffer_ = null;
        valFieldNameMap_ = null;
        log = Logger.getLogger(className);
        initialize();
    }

    public TpsPersonDataReqDataBinding(HashMap valFieldNameMap)
    {
        buffer_ = null;
        valFieldNameMap_ = null;
        log = Logger.getLogger(className);
        valFieldNameMap_ = valFieldNameMap;
        initialize();
    }

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
	public String getXMLDATAIN()
	{
		String xMLDATAIN = null;
		xMLDATAIN = MarshallStringUtils.unmarshallFixedLengthStringFromBuffer (buffer_, 0, CICS_DEFAULT_LANGUAGE_ID, 10000);
		return (xMLDATAIN);
	}

	/**
	 * @generated
	 */
	public void setXMLDATAIN (String xMLDATAIN)
	{
       if(xMLDATAIN != null)
        {
            if(xMLDATAIN.length() > 10000)
               throw new IllegalArgumentException(MarshallResource.instance().getString("IWAA0124E", xMLDATAIN, "10000", "XML_DATA_IN"));
            MarshallStringUtils.marshallFixedLengthStringIntoBuffer(xMLDATAIN, buffer_, 0, CICS_DEFAULT_LANGUAGE_ID, 10000, 0, " ");
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
		DataObject dataObject = BOfactory_.create("http://nav-tjeneste-person/no/nav/asbo", "tpsPersonDataReq");

		try { dataObject.setString ("XML_DATA_IN", getXMLDATAIN()); } catch (Exception exc) {}

		log.logp(Level.FINE, className, "getDataObject()", "DataObject: " + dataObject.toString());

		return (dataObject);
	}

	/**
	 * @generated
	 */
	public void setDataObject (DataObject dataObject) throws DataBindingException
	{
		if (dataObject.isSet ("xMLDATAIN"))
			setXMLDATAIN (dataObject.getString ("XML_DATA_IN"));
		log.logp(Level.FINE, className, "setDataObject()", "DataObject: " + dataObject.toString());
	}
}