package no.nav.maven.plugins.fixers;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import no.nav.maven.plugins.common.utils.Random;

public class RunAsValues {

	private String userName = null;

	private String passwd = null;

	private String id = null;

	public RunAsValues() {
		id = Random.getUniqueId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Element getAuthenticationNode() {
		Element authData, role;
		Element runAsBindings = DocumentHelper.createElement("runAsBindings");

		runAsBindings.addAttribute("xmi:id", "RunAsBindings_" + id);

		authData = runAsBindings.addElement("authData");
		authData.addAttribute("xmi:type","com.ibm.ejs.models.base.bindings.commonbnd:BasicAuthData");
		authData.addAttribute("xmi:id", "BasicAuthData_" + id);
		authData.addAttribute("userId", userName);
		authData.addAttribute("password", passwd) ;

		role = runAsBindings.addElement("securityRole");
		role.addAttribute("href", "META-INF/application.xml#SecurityRole_"+ id);

		return runAsBindings;
	}

}
