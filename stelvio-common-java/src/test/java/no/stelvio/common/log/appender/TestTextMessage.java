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

	private String text = null;

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
	public void setText(String message) throws JMSException {
		text = message;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.TextMessage#getText()
	 */
	public String getText() throws JMSException {
		return text;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSMessageID()
	 */
	public String getJMSMessageID() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSMessageID(java.lang.String)
	 */
	public void setJMSMessageID(String arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSTimestamp()
	 */
	public long getJMSTimestamp() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSTimestamp(long)
	 */
	public void setJMSTimestamp(long arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
	 */
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
	 */
	public void setJMSCorrelationIDAsBytes(byte[] arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
	 */
	public void setJMSCorrelationID(String arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSCorrelationID()
	 */
	public String getJMSCorrelationID() throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSReplyTo()
	 */
	public Destination getJMSReplyTo() throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
	 */
	public void setJMSReplyTo(Destination arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSDestination()
	 */
	public Destination getJMSDestination() throws JMSException {
		return null;
	}

	/**  
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
	 */
	public void setJMSDestination(Destination arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSDeliveryMode()
	 */
	public int getJMSDeliveryMode() throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSDeliveryMode(int)
	 */
	public void setJMSDeliveryMode(int arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSRedelivered()
	 */
	public boolean getJMSRedelivered() throws JMSException {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSRedelivered(boolean)
	 */
	public void setJMSRedelivered(boolean arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSType()
	 */
	public String getJMSType() throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSType(java.lang.String)
	 */
	public void setJMSType(String arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSExpiration()
	 */
	public long getJMSExpiration() throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSExpiration(long)
	 */
	public void setJMSExpiration(long arg0) throws JMSException {
	}

    @Override
    public long getJMSDeliveryTime() throws JMSException {
        return 0;
    }

    @Override
    public void setJMSDeliveryTime(long l) throws JMSException {

    }

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getJMSPriority()
	 */
	public int getJMSPriority() throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setJMSPriority(int)
	 */
	public void setJMSPriority(int arg0) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#clearProperties()
	 */
	public void clearProperties() throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#propertyExists(java.lang.String)
	 */
	public boolean propertyExists(String arg0) throws JMSException {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getBooleanProperty(java.lang.String)
	 */
	public boolean getBooleanProperty(String arg0) throws JMSException {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getByteProperty(java.lang.String)
	 */
	public byte getByteProperty(String arg0) throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getShortProperty(java.lang.String)
	 */
	public short getShortProperty(String arg0) throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getIntProperty(java.lang.String)
	 */
	public int getIntProperty(String arg0) throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getLongProperty(java.lang.String)
	 */
	public long getLongProperty(String arg0) throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getFloatProperty(java.lang.String)
	 */
	public float getFloatProperty(String arg0) throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getDoubleProperty(java.lang.String)
	 */
	public double getDoubleProperty(String arg0) throws JMSException {
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getStringProperty(java.lang.String)
	 */
	public String getStringProperty(String arg0) throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getObjectProperty(java.lang.String)
	 */
	public Object getObjectProperty(String arg0) throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#getPropertyNames()
	 */
	public Enumeration getPropertyNames() throws JMSException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
	 */
	public void setBooleanProperty(String arg0, boolean arg1) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
	 */
	public void setByteProperty(String arg0, byte arg1) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setShortProperty(java.lang.String, short)
	 */
	public void setShortProperty(String arg0, short arg1) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setIntProperty(java.lang.String, int)
	 */
	public void setIntProperty(String arg0, int arg1) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setLongProperty(java.lang.String, long)
	 */
	public void setLongProperty(String arg0, long arg1) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
	 */
	public void setFloatProperty(String arg0, float arg1) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
	 */
	public void setDoubleProperty(String arg0, double arg1) throws JMSException {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.jms.Message#setStringProperty(java.lang.String, java.lang.String)
	 */
	public void setStringProperty(String arg0, String arg1) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#setObjectProperty(java.lang.String, java.lang.Object)
	 */
	public void setObjectProperty(String arg0, Object arg1) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#acknowledge()
	 */
	public void acknowledge() throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * @see javax.jms.Message#clearBody()
	 */
	public void clearBody() throws JMSException {
    }

    @Override
    public <T> T getBody(Class<T> aClass) throws JMSException {
        return null;
    }

    @Override
    public boolean isBodyAssignableTo(Class aClass) throws JMSException {
        return false;
    }
}