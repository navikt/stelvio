package no.nav.maven.plugins.descriptor.config;

import java.util.List;

public class SecurityRoleConfig {

	private String roleName;
	private List<String> groups;
	private List<String> users;
	private boolean allAuthenticatedUsers = false;
	private boolean everyone = false;
	
	public boolean isAllAuthenticatedUsers() {
		return allAuthenticatedUsers;
	}
	public void setAllAuthenticatedUsers(boolean allAuthenticatedUsers) {
		this.allAuthenticatedUsers = allAuthenticatedUsers;
	}
	public boolean isEveryone() {
		return everyone;
	}
	public void setEveryone(boolean everyone) {
		this.everyone = everyone;
	}
	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
}
