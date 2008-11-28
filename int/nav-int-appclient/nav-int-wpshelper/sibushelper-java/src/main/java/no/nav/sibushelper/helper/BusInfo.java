/**
 * 
 */
package no.nav.sibushelper.helper;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public class BusInfo {

    private String name;
    private String description;
    private Boolean secure;
    private Boolean configReload;

    /**
     * @param name
     * @param description
     * @param secure
     * @param configReload
     */
    public BusInfo(String busName, String busDescription, Boolean busSecure, Boolean busConfigReload)
    {
        this.name = null;
        this.description = null;
        this.secure = false;
        this.configReload = false;

        this.name = busName;
        this.description = busDescription;
        this.secure = busSecure;
        this.configReload = busConfigReload;
    }

    /**
     * @return
     */
    public Boolean getConfigReload()
    {
        return configReload;
    }

    /**
     * @return
     */
    public String getDescription()
    {
        return description;
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
    public Boolean getSecure()
    {
        return secure;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object anOther)
    {
        boolean equal = false;
        if(anOther instanceof BusInfo)
        {
            BusInfo otherBus = (BusInfo)anOther;
            if(otherBus.configReload != configReload)
                equal = false;
            else
            if(!otherBus.description.equals(description))
                equal = false;
            else
            if(!otherBus.name.equals(name))
                equal = false;
            else
            if(otherBus.secure != secure)
                equal = false;
        }
        return equal;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "BusInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + "name=" + name + ", " + "description=" + description + ", " + "secure=" + secure + ", " + "configReload=" + configReload + "}";
    }    

}
