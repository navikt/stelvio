package no.nav.maven.config;


/**
 * Key Info with data that may be used to import HSM keys
 * 
 * @author Christian Askeland, Accenture
 *
 */

public class KeyInfo {
	
	private String name;
	private String fileName;
	private String password;
	private boolean kwkExport;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isKwkExport() {
		return kwkExport;
	}
	
	public void setKwkExport(boolean kwkExport) {
		this.kwkExport = kwkExport;
	}
	
	/**
	 * Checks if all fields are set and not empty
	 * @return true if all fields are set, else false
	 */
	public boolean isValid(){
		return name!=null && !name.isEmpty()
				&& fileName != null && !fileName.isEmpty()
				&& password != null && !password.isEmpty()
				&& (kwkExport==true || kwkExport==false);
	}
		
}
