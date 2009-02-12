package no.nav.maven.plugin.artifact.modifier.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.maven.plugins.descriptor.config.SecurityRoleConfig;
import no.nav.maven.plugins.descriptor.jee.ApplicationDescriptorEditor;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmApplicationBndEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.GroupType;
import no.nav.pensjonsprogrammet.wpsconfiguration.RoleType;
import no.nav.pensjonsprogrammet.wpsconfiguration.RolesType;
import no.nav.pensjonsprogrammet.wpsconfiguration.UserType;

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

import sun.security.action.GetLongAction;

public class RoleSecurity {

	public static final void updateRoleBindings(final Archive archive, final RolesType securityRoles) {
	
		List<SecurityRoleConfig> securityRolesConfig = securityRoles2securityRoleConfigList(securityRoles);
		
		try {
			ApplicationDescriptorEditor app = new ApplicationDescriptorEditor(archive);
			app.addSecurityRoles(securityRolesConfig);
			app.save();

			IbmApplicationBndEditor bnd = new IbmApplicationBndEditor(archive, app.getApplication());
			bnd.addRoleBindings(securityRolesConfig);
			bnd.save();
		} catch (IOException e) {
			throw new RuntimeException("An error occured saving the application descriptor for the ear archive", e);
		}
	}
	
	private static final List<SecurityRoleConfig> securityRoles2securityRoleConfigList(RolesType securityRoles) {
		List<SecurityRoleConfig> rolesConfig = new ArrayList<SecurityRoleConfig>();
		
		for(RoleType r : securityRoles.getRoleList()) {
			SecurityRoleConfig securityRoleConfig = new SecurityRoleConfig();
			securityRoleConfig.setRoleName(r.getName());
			
			if(r.getUsers() != null && r.getUsers().sizeOfUserArray() > 0) {
				List<UserType> users = r.getUsers().getUserList();
				List<String> stringUsers = new ArrayList<String>();
				for(UserType u : users) {
					stringUsers.add(u.getName());
				}
				securityRoleConfig.setUsers(stringUsers);
			}
			
			if(r.getGroups() != null && r.getGroups().sizeOfGroupArray() > 0) {
				List<GroupType> groups = r.getGroups().getGroupList();
				List<String> stringGroups = new ArrayList<String>();
				for(GroupType g : groups) {
					stringGroups.add(g.getName());
				}
				securityRoleConfig.setGroups(stringGroups);
			}
			rolesConfig.add(securityRoleConfig);
		}
		
		return rolesConfig;
	}
}