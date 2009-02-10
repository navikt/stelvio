package no.nav.maven.plugins.descriptor.jee;

import java.util.List;

import no.nav.maven.plugins.descriptor.common.DeploymentDescriptorEditor;
import no.nav.maven.plugins.descriptor.config.SecurityRoleConfig;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.j2ee.application.Application;
import org.eclipse.jst.j2ee.application.ApplicationPackage;
import org.eclipse.jst.j2ee.common.SecurityRole;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.ejb.EJBJar;

public class ApplicationDescriptorEditor extends DeploymentDescriptorEditor<Application> {

	private Application application;
	private static final String APPLICATION_XML_FILE = "application.xml";
	
	public ApplicationDescriptorEditor(Archive archive){
		super(archive, APPLICATION_XML_FILE);
		application = getDescriptor();	
	}
	
	public void addSecurityRoles(List<SecurityRoleConfig> securityRoles){
		for (SecurityRoleConfig securityRole : securityRoles) {
			addSecurityRole(securityRole.getRoleName());
		}
	}

	
	private void addSecurityRole(String roleName){
		if(!isSecurityRolePresent(roleName)){
			SecurityRole role = createNewSecurityRole(roleName);
			getSecurityRoles().add(role);
		}
	}

	private boolean isSecurityRolePresent(String roleName){
		return application.containsSecurityRole(roleName);
	}
	
	private EList getSecurityRoles(){
		return application.getSecurityRoles();
	}
	
	private SecurityRole createNewSecurityRole(String roleName){
		SecurityRole securityRole= getCommmonFactory().createSecurityRole();
		securityRole.setRoleName(roleName);	
		return securityRole;
	}
	protected Application createDescriptorContent(){		
		return ApplicationPackage.eINSTANCE.getApplicationFactory().createApplication();
	}
	public Application getApplication(){
		return application;
	}
}
