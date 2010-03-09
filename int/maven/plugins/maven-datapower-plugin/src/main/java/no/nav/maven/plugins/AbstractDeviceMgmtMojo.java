package no.nav.maven.plugins;

import java.net.URL;

import no.nav.datapower.xmlmgmt.XMLMgmtSession;


public abstract class AbstractDeviceMgmtMojo extends AbstractDataPowerMojo {

    /**
     * The URL of the DataPower device to manage. Note: HTTPS required:
     * 		https://hostname:5550
     * 
     * @parameter expression="${datapower.baseurl}" alias="host"
     * @required
     */
    private URL deviceUrl;

    /**
     * The device username used to authenticate the XML Management Interface
     * 
     * @parameter expression="${datapower.username}" alias="user"
     * @required
     */    
    private String username;

    /**
     * The device password used to authenticate the XML Management Interface
     * 
     * @parameter expression="${datapower.password}" alias="pwd"
     * @required
     */    
    private String password;

    private XMLMgmtSession mgmtSession;
    
    protected URL getDeviceUrl() {
    	return deviceUrl;
    }
    
//    public void setDeviceUrl(URL deviceUrl) {
//    	this._deviceUrl = deviceUrl;
//    }
    
	protected XMLMgmtSession getXMLMgmtSession() {
		if (mgmtSession == null) {
			getLog().debug("Creating new XMLMgmtSession, host = " + getDeviceUrl() + ", user = " + username + ", pwd = " + password);	
			mgmtSession = new XMLMgmtSession.Builder(getDeviceUrl().toString()).user(username).password(password).domain(getDomain()).build();
		}
		return mgmtSession;
	}
}
