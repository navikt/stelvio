package no.nav.maven.plugins;

import java.net.URL;


public abstract class AbstractDeviceMgmtMojo extends AbstractDataPowerMojo {

    /**
     * The URL of the DataPower device to manage. Note: HTTPS required:
     * 		https://hostname:5550
     * 
     * @parameter expression="${deviceUrl}"
     * @required
     */
    private URL deviceUrl;
    
    public URL getDeviceUrl() {
    	return deviceUrl;
    }
}
