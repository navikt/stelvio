package no.nav.ant;


/**
 * Class holding module data for one element in projectset file.
 * 
 * @author personcca3f1d5452e
 */
public class ProjectSetData {
	
	private String svnUrl = null;
	private String moduleName = null;
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String cvsRoot) {
		this.svnUrl = cvsRoot;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String module) {
		this.moduleName = module;
	}
	
	

}