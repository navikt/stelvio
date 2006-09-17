package no.stelvio.integration.framework.jca.cics.records;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class provides a simple example of using the Streamable interface.
 * This custom record can be used with the ECI Resource adapter.
 * Based on IBM Redbook example.
 *
 * @author  person5b7fd84b3197, Accenture
 * @version $Id: CICSGenericRecord.java 2828 2006-03-05 10:17:18Z skb2930 $
 */
public class CICSGenericRecord implements javax.resource.cci.Streamable, javax.resource.cci.Record {
	private static final Log log = LogFactory.getLog(CICSGenericRecord.class);

	//-------------------------------------------------------------------------
    // Internal properties
    //-------------------------------------------------------------------------
    private String recordName;
    private String desc;
    private String contents = "";
    private String enc = "IBM037";

	/**
	 * Creates a new instance of CICSGenericRecord.
	 * @param encoding String
	 */
	public CICSGenericRecord(String encoding) {
		enc = encoding;
	}

	/**
	 * Creates a new instance of CICSGenericRecord.
	 * This constructor defaults the encoding to IBM037
	 */
	public CICSGenericRecord() {
	}

    //-------------------------------------------------------------------------
    //  the following methods are required for the Record interface
    //-------------------------------------------------------------------------
    /**
     * Get the name of the Record.
     * 
     * @return a String representing the Record Name
     */
    public java.lang.String getRecordName() {
        return recordName;
    }

    /**
     * Set the name of the Record.
     * 
     * @param newName The Name of the Record
     */
    public void setRecordName(String newName) {
        recordName = newName;
    }


    /**
     * Set the record short destription.
     *
     * @param newDesc The Description for this record
     */
    public void setRecordShortDescription(String newDesc) {
        desc = newDesc;
    }

    /**
     * Get the short description for this Record.
     * 
     * @return a String representing the Description
     */
    public java.lang.String getRecordShortDescription() {
        return desc;
    }

    /**
     * Return a hashcode for this object.
     *
     * @return hashcode
     */
    public int hashCode() {
        if (contents != null) {
            return contents.hashCode();
        } else {
            return super.hashCode();
        }
    }

    /**
     * Determines if objects are equal. Objects are equal if they
     * have the same reference or the text contained is identical.
     * @param comp The object to compare.
     * @return flag indicating true or false
     */
    public boolean equals(Object comp) {
        if (!(comp instanceof CICSGenericRecord)) {
            return false;
        }

        if (comp == this) {
            return true;
        }

        CICSGenericRecord realComp = (CICSGenericRecord) comp;

        return realComp.getText().equals(getText());
    }

    /**
     * Use the superclass clone method for this class.
     * @return Object - The clone
     * @throws CloneNotSupportedException - Unable to clone
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    //-------------------------------------------------------------------------
    // These methods are required for the streamable interface
    //-------------------------------------------------------------------------
    /**
     * This method is invoked by the ECI Resource Adapter when it is
     * transmitting data to the Record. This means we are receiving a commarea
     * back from the ECI Resource Adapter and must have been passed as an 
     * output record.
     * 
     * @param is the inputStream where we read the information from.
     */
    public void read(InputStream is) {
	    try {
		    int total = is.available();
		    byte[] bytes = new byte[total];
		    is.read(bytes);

		    //-----------------------------------------------------------------
		    // convert the bytes to a string based on the selected encoding
		    //-----------------------------------------------------------------
		    contents = new String(bytes, enc);
	    } catch (UnsupportedEncodingException e) {
		    if (log.isWarnEnabled()) {
			    log.warn("Encoding not supported, change to another", e);
		    }
	    } catch (IOException e) {
		    if (log.isWarnEnabled()) {
			    log.warn("Could not read bytes", e);
		    }
	    }

    }

	/**
     * This method is invoked by the ECI Resource Adapter when it wants to
     * read the record. This means we have been passed as an input record.
     * 
     * @param os the output stream to write the information to.
     */
    public void write(OutputStream os) {
        try {

            //-----------------------------------------------------------------
            // output the string as bytes in the selected encoding
            //-----------------------------------------------------------------
            os.write(contents.getBytes(enc));
        } catch (UnsupportedEncodingException e) {
	        if (log.isWarnEnabled()) {
		        log.warn("Encoding not supported, change to another", e);
	        }
        } catch (IOException e) {
	        if (log.isWarnEnabled()) {
		        log.warn("Could not read bytes", e);
	        }
        }
	}

    /**
     * Return the text of this java record.
     *
     * @return The text
     */
    public String getText() {
        return contents;
    }


    /**
     * Set the text for this java record.
     * 
     * @param newStr The new text
     */
    public void setText(String newStr) {
        contents = newStr;
    }

    /**
     * Return the current java encoding used for this record.
     *
     * @return the java encoding
     */
    public String getEncoding() {
        return enc;
    }

    /**
     * Set the java encoding to be used for this record.
     * <p>
     * Note: No checks are made at this time to see if the encoding is a valid
     * java encoding. If you wish you can modify the code to include this.
     *
     * @param newEnc the new java encoding
     */
    public void setEncoding(String newEnc) {
        enc = newEnc;
    }
}
