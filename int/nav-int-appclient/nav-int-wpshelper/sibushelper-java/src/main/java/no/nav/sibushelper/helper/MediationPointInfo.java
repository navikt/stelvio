/**
 * 
 */
package no.nav.sibushelper.helper;

import javax.management.ObjectName;

import com.ibm.ws.sib.processor.MediationState;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public class MediationPointInfo {

    private String medPointName;
    private String state;
    private Long currentDepth;
    private Long highMessageThreshold;
    private Boolean sendAllowed;
    private String id;
    private String reason;
    private ObjectName mBean;

    /**
     * @param queueName
     * @param queueState
     * @param currentDepth
     * @param highMessageThreshold
     * @param sendAllowed
     * @param id
     * @param reason
     * @param mBean
     */
    public MediationPointInfo(String queueName, String queueState, Long currentDepth, Long highMessageThreshold, Boolean sendAllowed, String id, String reason, ObjectName mBean)
    {
        medPointName = null;
        state = null;
        this.currentDepth = null;
        this.highMessageThreshold = null;
        this.sendAllowed = null;
        this.id = null;
        this.reason = null;
        this.mBean = null;

        medPointName = queueName;
        state = queueState;
        this.currentDepth = currentDepth;
        this.highMessageThreshold = highMessageThreshold;
        this.sendAllowed = sendAllowed;
        this.id = id;
        this.reason = reason;
        this.mBean = mBean;
    }

    /**
     * @return
     */
    public String getName()
    {
        return medPointName;
    }

    /**
     * @return
     */
    public String getState()
    {
        return state;
    }

    /**
     * @return
     */
    public Long getCurrentDepth()
    {
        return currentDepth;
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
     * @return
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @return
     */
    public ObjectName getMBean()
    {
        return mBean;
    }

    /**
     * @return
     */
    public boolean isStarted()
    {
        return state.equals(MediationState.STARTED.toString());
    }    
}
