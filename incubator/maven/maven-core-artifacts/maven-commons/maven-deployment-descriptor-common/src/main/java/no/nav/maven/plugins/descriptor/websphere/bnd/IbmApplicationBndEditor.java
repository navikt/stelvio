package no.nav.maven.plugins.descriptor.websphere.bnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.nav.maven.plugins.descriptor.common.DeploymentDescriptorEditor;
import no.nav.maven.plugins.descriptor.config.SecurityRoleConfig;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.j2ee.application.Application;
import org.eclipse.jst.j2ee.common.SecurityRole;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

import com.ibm.ejs.models.base.bindings.applicationbnd.AllAuthenticatedUsers;
import com.ibm.ejs.models.base.bindings.applicationbnd.ApplicationBinding;
import com.ibm.ejs.models.base.bindings.applicationbnd.ApplicationBindingsHelper;
import com.ibm.ejs.models.base.bindings.applicationbnd.ApplicationbndFactory;
import com.ibm.ejs.models.base.bindings.applicationbnd.ApplicationbndPackage;
import com.ibm.ejs.models.base.bindings.applicationbnd.AuthorizationTable;
import com.ibm.ejs.models.base.bindings.applicationbnd.Everyone;
import com.ibm.ejs.models.base.bindings.applicationbnd.Group;
import com.ibm.ejs.models.base.bindings.applicationbnd.RoleAssignment;
import com.ibm.ejs.models.base.bindings.applicationbnd.User;

public class IbmApplicationBndEditor extends DeploymentDescriptorEditor<ApplicationBinding> {

	private static final String IBM_APPLICATION_BND_FILE = "ibm-application-bnd.xmi";
	private ApplicationBinding appBnd;
	private Application application;
	private ApplicationbndFactory APP_BND_FACTORY = ApplicationbndPackage.eINSTANCE.getApplicationbndFactory();
	
	public IbmApplicationBndEditor(Archive archive, Application application){
		super(archive, IBM_APPLICATION_BND_FILE);
		this.application = application;
		this.appBnd = getDescriptor();
	}
	
	protected ApplicationBinding createDescriptorContent(){		
		return ApplicationBindingsHelper.getApplicationBinding(application);
	}
	
	public void addRoleBindings(List<SecurityRoleConfig> securityRoles){
		for (SecurityRoleConfig config : securityRoles) {
			addRoleAssignment(config);
		}
	}
	
	private void addRoleAssignment(SecurityRoleConfig config){
		//getAuthorizationTable().
		String roleName = config.getRoleName();
		if(application.containsSecurityRole(roleName)){
			SecurityRole role = application.getSecurityRoleNamed(roleName);
			RoleAssignment roleAssignment = createNewRoleAssignment(role, config);
			if(isRoleAssignmentPresent(roleAssignment)){
				int index = getRoleAssignmentIndex(roleAssignment);
				getAuthorizationTable().getAuthorizations().set(index, roleAssignment);
			} else {
				getAuthorizationTable().getAuthorizations().add(roleAssignment);
			}
		} else {
			throw new IllegalArgumentException("SecurityRole '" + roleName + "' "
												+ "from config is not present in application.xml");
		}
	}
	
	private int getRoleAssignmentIndex(RoleAssignment roleAssignmentToCheck){
		EList authorizations = getAuthorizationTable().getAuthorizations();
		int i = 0;
		for (Object object : authorizations) {
			RoleAssignment roleAssignment = (RoleAssignment)object;
			if(roleAssignment.getRole().getRoleName().equals(
					roleAssignmentToCheck.getRole().getRoleName())){
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private boolean isRoleAssignmentPresent(RoleAssignment roleAssignmentToCheck){
		EList authorizations = getAuthorizationTable().getAuthorizations();
		for (Object object : authorizations) {
			RoleAssignment roleAssignment = (RoleAssignment)object;
			if(roleAssignment.getRole().getRoleName().equals(
					roleAssignmentToCheck.getRole().getRoleName())){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private RoleAssignment createNewRoleAssignment(SecurityRole role, SecurityRoleConfig config){
		
		RoleAssignment roleAssignment = APP_BND_FACTORY.createRoleAssignment();
		roleAssignment.setRole(role);
		
		List<String> groupNames = config.getGroups();
		List<String> userNames = config.getUsers();
		boolean allAuthenticated = config.isAllAuthenticatedUsers();
		boolean everyone = config.isEveryone();
		
		if(allAuthenticated){
			roleAssignment.getSpecialSubjects().add(createAllAuthenticatedUsers());
		} else if(everyone){
			roleAssignment.getSpecialSubjects().add(APP_BND_FACTORY.createEveryone());	
		} else {
			if(groupNames != null){
				roleAssignment.getGroups().addAll(createGroups(groupNames));
			}
			if(userNames != null){
				roleAssignment.getUsers().addAll(createUsers(userNames));
			}
		} 
		return roleAssignment;
	}
	
	private User createUser(String userName) {
		User user = APP_BND_FACTORY.createUser();
		user.setName(userName);
		return user;
	}

	private AllAuthenticatedUsers createAllAuthenticatedUsers() {
		AllAuthenticatedUsers allAuthenticated = APP_BND_FACTORY.createAllAuthenticatedUsers();
		allAuthenticated.setName("AllAuthenticatedUsers");
		return allAuthenticated;
	}

	private Group createGroup(String groupName) {
		Group group = APP_BND_FACTORY.createGroup();
		group.setName(groupName);
		return group;
	}
	
	private List<Group> createGroups(List<String> groupNames) {
		List<Group> groups = new ArrayList<Group>();
		for (String groupName : groupNames) {
			Group group = createGroup(groupName);
			groups.add(group);
		}
		return groups;
	}
	private List<User> createUsers(List<String> userNames) {
		List<User> users = new ArrayList<User>();
		for (String userName : userNames) {
			User user = createUser(userName);
			users.add(user);
		}
		return users;
	}
	
	private AuthorizationTable getAuthorizationTable(){
		if(appBnd.getAuthorizationTable() == null){
			appBnd.setAuthorizationTable(APP_BND_FACTORY.createAuthorizationTable());
		}
		return appBnd.getAuthorizationTable();
	}
}
