package no.nav.maven.plugin.bounce.plugin.utils;

/**
 * This is a class that holds the flags for which modules should be restarted for a given application.
 * @author test@example.com
 *
 */
public class RestartConfig {
	private String app;
	public String getApp() {
		return app;
	}

	private boolean was_ss;
	private boolean was_is;
	private boolean wps;
	
	public RestartConfig(){
		this.app = "";
		was_ss = false;
		was_is = false;
		wps = false;
	}
	
	public void setApp(String app) {
		this.app = app;
	}

	public void setWas_ss(String was_ss) {
		this.was_ss = was_ss.equals("true");
	}

	public void setWas_is(String was_is) {
		this.was_is = was_is.equals("true");
	}

	public void setWps(String wps) {
		this.wps = wps.equals("true");
	}

	public boolean hasWas_ss() {
		return was_ss;
	}

	public boolean hasWas_is() {
		return was_is;
	}

	public boolean hasWps() {
		return wps;
	}

	public String toString(){
		return app +":"+ this.was_ss + "/" + this.was_is + "/"+this.wps;
	}
	
}
