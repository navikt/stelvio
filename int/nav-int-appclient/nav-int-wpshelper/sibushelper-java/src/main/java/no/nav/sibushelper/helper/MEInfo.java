/**
 * 
 */
package no.nav.sibushelper.helper;

import javax.management.ObjectName;

import no.nav.sibushelper.common.Constants;


/**
 * @author persona2c5e3b49756 Schnell
 *
 */
/**
 * @author wpsadmin
 *
 */
public class MEInfo implements Comparable {

    private String name;
    private String haGroup;
    private String state;
    private String process;
    private String node;
    private String bus;
    private String cell;
    private String cluster;
    private ObjectName mBean;
    private boolean isStarted;	
	
	/**
	 * @param name
	 * @param haGroup
	 * @param state
	 * @param isStarted
	 * @param process
	 * @param node
	 * @param cell
	 * @param mBean
	 */
	public MEInfo(String name, String haGroup, String state, boolean isStarted, String process, String node, String cell, ObjectName mBean) {
		this.name = null;
		this.haGroup = null;
		this.state = null;
		this.process = null;
		this.node = null;
		bus = null;
		this.cell = null;
		cluster = null;
		this.mBean = null;
		this.isStarted = false;
		
        this.name = name;
        this.haGroup = haGroup;
        this.state = state;
        this.isStarted = isStarted;
        this.process = process;
        this.node = node;
        this.cell = cell;
        this.mBean = mBean;
	
		bus = getValueFromName(haGroup, Constants.HA_BUS);
		if(bus == null) bus = "<Unknown>";
		cluster = getValueFromName(haGroup, "IBM_hc");
	}

	
    /**
     * @param all
     * @param key
     * @return
     */
    private String getValueFromName(String all, String key)
    {
    	String value = null;
    	if(!key.endsWith("=")) key = key + "=";
    	int pos1 = all.indexOf(key);
    	if(pos1 != -1)
        {
    		int pos2 = all.indexOf(",", pos1 + 1);
    		if(pos2 == -1) pos2 = all.length();
    		value = all.substring(pos1 + key.length(), pos2);
        }
    	return value;
    }

	/**
	 * @return
	 */
	public java.lang.String getHaGroup()
	{
		return haGroup;
	}

	/**
	 * @return
	 */
	public java.lang.String getName()
	{
		return name;
	}

	/**
	 * @return
	 */
	public java.lang.String getState()
	{
		return state;
	}

	/**
	 * @return
	 */
	public java.lang.String getNode()
	{
		return node;
	}

	/**
	 * @return
	 */
	public java.lang.String getProcess()
	{
		return process;
	}

	/**
	 * @return
	 */
	public java.lang.String getBus()
	{
		return bus;
	}

	/**
	 * @return
	 */
	public java.lang.String getCell()
	{
		return cell;
	}

	/**
	 * @return
	 */
	public java.lang.String getCluster()
	{
		return cluster;
	}

	/**
	 * @return
	 */
	public javax.management.ObjectName getMBean()
	{
		return mBean;
	}

	/**
	 * @return
	 */
	public boolean isStarted()
	{
		return isStarted;
	}

	/**
	 * @return
	 */
	public boolean isMemberOfCluster()
	{
		return cluster != null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(java.lang.Object other)
	{
		int result = -1;
		if(other instanceof no.nav.sibushelper.helper.MEInfo)
			result = ((no.nav.sibushelper.helper.MEInfo)other).getName().compareTo(getName());
		return 0 - result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public java.lang.String toString()
	{
		return "MEInfo@" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + ": " + "{ name=" + name + ", haGroup=" + haGroup + ", state=" + state + ", process=" + process + ", node=" + node + ", bus=" + bus + ", cell=" + cell + ", cluster=" + cluster + ", mBean=" + mBean + "}";
	}
	
}
