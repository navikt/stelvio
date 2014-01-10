package no.nav.maven.plugin.wpsdeploy.plugin.utils;

public class SshUser {
	private String hostname;
	private String username;
	private String password;
	public SshUser(String hostname, String username, String password){
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}

	public String getHostname(){
		return this.hostname;
	}
	public String getUsername(){
		return this.username;
	}
	public String getPassword(){
		return this.password;
	}
}
