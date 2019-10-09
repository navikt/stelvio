package no.stelvio.common.log.appender;

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * TextMessage implementation for unit testing.
 * 
 * @author person356941106810, Accenture
 * @version $Id: TestTextMessage.java 2187 2005-04-06 07:05:45Z psa2920 $
 */
public class TestTextMessage implements TextMessage {

	private String text;

	/**
	 * Constructs a TestTextMessage using the provided message.
	 * 
	 * @param message the message.
	 */
	public TestTextMessage(String message) {
		text = message;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.TextMessage#setText(java.lang.String)
	 */
	public void setText(String message) {
		text = message;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.TextMessage#getText()
	 */
	public String getText() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSMessageID()
	 */
	public String getJMSMessageID() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSMessageID(java.lang.String)
	 */
	public void setJMSMessageID(String arg0) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSTimestamp()
	 */
	public long getJMSTimestamp() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSTimestamp(long)
	 */
	public void setJMSTimestamp(long arg0) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
	 */
	public byte[] getJMSCorrelationIDAsBytes() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
	 */
	public void setJMSCorrelationIDAsBytes(byte[] arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
	 */
	public void setJMSCorrelationID(String arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSCorrelationID()
	 */
	public String getJMSCorrelationID() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSReplyTo()
	 */
	public Destination getJMSReplyTo() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
	 */
	public void setJMSReplyTo(Destination arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSDestination()
	 */
	public Destination getJMSDestination() {
		return null;
	}

	/**  
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
	 */
	public void setJMSDestination(Destination arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSDeliveryMode()
	 */
	public int getJMSDeliveryMode() {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSDeliveryMode(int)
	 */
	public void setJMSDeliveryMode(int arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSRedelivered()
	 */
	public boolean getJMSRedelivered() {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSRedelivered(boolean)
	 */
	public void setJMSRedelivered(boolean arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSType()
	 */
	public String getJMSType() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSType(java.lang.String)
	 */
	public void setJMSType(String arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSExpiration()
	 */
	public long getJMSExpiration() {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSExpiration(long)
	 */
	public void setJMSExpiration(long arg0) {
	}

    @Override
    public long getJMSDeliveryTime() {
        return 0;
    }

    @Override
    public void setJMSDeliveryTime(long l) {

    }

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSPriority()
	 */
	public int getJMSPriority() {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSPriority(int)
	 */
	public void setJMSPriority(int arg0) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#clearProperties()
	 */
	public void clearProperties() {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#propertyExists(java.lang.String)
	 */
	public boolean propertyExists(String arg0) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getBooleanProperty(java.lang.String)
	 */
	public boolean getBooleanProperty(String arg0) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getByteProperty(java.lang.String)
	 */
	public byte getByteProperty(String arg0) {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getShortProperty(java.lang.String)
	 */
	public short getShortProperty(String arg0) {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getIntProperty(java.lang.String)
	 */
	public int getIntProperty(String arg0) {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getLongProperty(java.lang.String)
	 */
	public long getLongProperty(String arg0) {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getFloatProperty(java.lang.String)
	 */
	public float getFloatProperty(String arg0) {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getDoubleProperty(java.lang.String)
	 */
	public double getDoubleProperty(String arg0) {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getStringProperty(java.lang.String)
	 */
	public String getStringProperty(String arg0) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getObjectProperty(java.lang.String)
	 */
	public Object getObjectProperty(String arg0) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getPropertyNames()
	 */
	public Enumeration getPropertyNames() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
	 */
	public void setBooleanProperty(String arg0, boolean arg1) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
	 */
	public void setByteProperty(String arg0, byte arg1) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setShortProperty(java.lang.String, short)
	 */
	public void setShortProperty(String arg0, short arg1) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setIntProperty(java.lang.String, int)
	 */
	public void setIntProperty(String arg0, int arg1) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setLongProperty(java.lang.String, long)
	 */
	public void setLongProperty(String arg0, long arg1) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
	 */
	public void setFloatProperty(String arg0, float arg1) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
	 */
	public void setDoubleProperty(String arg0, double arg1) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setStringProperty(java.lang.String, java.lang.String)
	 */
	public void setStringProperty(String arg0, String arg1) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setObjectProperty(java.lang.String, java.lang.Object)
	 */
	public void setObjectProperty(String arg0, Object arg1) {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#acknowledge()
	 */
	public void acknowledge() {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#clearBody()
	 */
	public void clearBody() {
    }

    @Override
    public <T> T getBody(Class<T> aClass) {
        return null;
    }

    @Override
    public boolean isBodyAssignableTo(Class aClass) {
        return false;
    }
}
