/**
 * 
 */
package no.nav.sibushelper.helper;

import javax.management.ObjectName;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
/**
 * @author utvikler
 *
 */
public class MQClientLinkInfo {

    private String name;
    private String status;
    private String qmName;
    private String channelName;
    private boolean defaultQM;
    private ObjectName mBean;

    /**
     * @param name
     * @param status
     * @param qmName
     * @param channelName
     * @param defaultQM
     * @param mBean
     */
    public MQClientLinkInfo(String name, String status, String qmName, String channelName, boolean defaultQM, ObjectName mBean)
    {
        this.name = null;
        this.status = null;
        this.qmName = null;
        this.channelName = null;
        this.defaultQM = false;
        this.mBean = null;
        
        this.name = name;
        this.status = status;
        this.qmName = qmName;
        this.channelName = channelName;
        this.defaultQM = defaultQM;
        this.mBean = mBean;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @return
     */
    public String getQueueManagerName()
    {
        return qmName;
    }

    /**
     * @return
     */
    public String getChannelName()
    {
        return channelName;
    }

    /**
     * @return
     */
    public boolean isDefaultQM()
    {
        return defaultQM;
    }

    public ObjectName getMBean()
    {
        return mBean;
    }

    /**
     * @return
     */
    public boolean isStarted()
    {
        return !status.equals("STOPPED");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "MQClientLinkInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": " + "{ name=" + name + ", status=" + status + ", qmName=" + qmName + ", channel=" + channelName + ", defaultQM=" + defaultQM + ", mBean=" + mBean + "}";
    }    
	
}
