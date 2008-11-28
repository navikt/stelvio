/**
 * 
 */
package no.nav.sibushelper.helper;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public class PublicationPointInfo {

    private String publicationPointName;
    private Long highMessageThreshold;
    private Boolean sendAllowed;
    private String id;

    /**
     * @param publicationPointName
     * @param highMessageThreshold
     * @param sendAllowed
     * @param id
     */
    public PublicationPointInfo(String publicationPointName, Long highMessageThreshold, Boolean sendAllowed, String id)
    {
        this.publicationPointName = null;
        this.highMessageThreshold = null;
        this.sendAllowed = null;
        this.id = null;
       
        this.publicationPointName = publicationPointName;
        this.highMessageThreshold = highMessageThreshold;
        this.sendAllowed = sendAllowed;
        this.id = id;
    }

    /**
     * @return
     */
    public String getName()
    {
        return publicationPointName;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "PublicationPointInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": " + "{ name=" + publicationPointName + ", highMessageThreshold=" + highMessageThreshold + ", sendAllowed=" + sendAllowed + ", id=" + id + "}";
    }    
}
