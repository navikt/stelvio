/**
 * 
 */
package no.nav.sibushelper.helper;

/**
 * @author persona2c5e3b49756 Schnell	
 *
 */
public class QueuePointInfo {

    private String queueName;
    private Long queueDepth;
    private String queueState;
    private Long highMessageThreshold;
    private Boolean sendAllowed;
    private String id;
	
    /**
     * @param queueName
     * @param queueDepth
     * @param queueState
     * @param highMessageThreshold
     * @param sendAllowed
     * @param id
     */
    public QueuePointInfo(String queueName, Long queueDepth, String queueState, Long highMessageThreshold, Boolean sendAllowed, String id)
    {
        this.queueName = null;
        this.queueDepth = null;
        this.queueState = null;
        this.highMessageThreshold = null;
        this.sendAllowed = null;
        this.id = null;
    
        this.queueName = queueName;
        this.queueDepth = queueDepth;
        this.queueState = queueState;
        this.highMessageThreshold = highMessageThreshold;
        this.sendAllowed = sendAllowed;
        this.id = id;
    }

    /**
     * 
     */
    public QueuePointInfo()
    {
        this.queueName = null;
        this.queueDepth = 0L;
        this.queueState = null;
        this.highMessageThreshold = 0L;
        this.sendAllowed = null;
        this.id = null;
    }
    
    
    /**
     * @return
     */
    public Long getCurrentDepth()
    {
        return queueDepth;
    }

    /**
     * @return
     */
    public String getName()
    {
        return queueName;
    }

    /**
     * @return
     */
    public String getState()
    {
        return queueState;
    }

    /**
     * @return
     */
    public Long getHighMessageThreshold()
    {
        return highMessageThreshold;
    }

    /**
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return
     */
    public Boolean getSendAllowed()
    {
        return sendAllowed;
    }

    /**
	 * @param highMessageThreshold the highMessageThreshold to set
	 */
	public void setHighMessageThreshold(Long highMessageThreshold) {
		this.highMessageThreshold = highMessageThreshold;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param queueName the queueName to set
	 */
	public void setName(String queueName) {
		this.queueName = queueName;
	}

	/**
	 * @param queueDepth the queueDepth to set
	 */
	public void setCurrentDepth(Long queueDepth) {
		this.queueDepth = queueDepth;
	}

	/**
	 * @param queueState the queueState to set
	 */
	public void setState(String queueState) {
		this.queueState = queueState;
	}

	/**
	 * @param sendAllowed the sendAllowed to set
	 */
	public void setSendAllowed(Boolean sendAllowed) {
		this.sendAllowed = sendAllowed;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "QueuePointInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": " + "{ name=" + queueName + ", queueDepth=" + queueDepth + ", state=" + queueState + ", highMessageThreshold=" + highMessageThreshold + ", sendAllowed=" + sendAllowed + ", id=" + id + "}";
    }
}
