package no.nav.maven.plugins.fixers;

import java.util.List;

import no.nav.maven.plugins.common.utils.Random;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class RoleBinding {
	private String roleName = null;
	private List<String> groupNames = null;
	private List<String> userNames = null;
	private String id = null;
	private boolean mapToEveryone = false;
	private boolean mapToAllAuthenticated = false;
	
	public boolean isMapToAllAuthenticated() {
		return mapToAllAuthenticated;
	}

	public void setMapToAllAuthenticated(boolean mapToAllAuthenticated) {
		this.mapToAllAuthenticated = mapToAllAuthenticated;
	}

	public RoleBinding(){
		id = Random.getUniqueId();
		RunAsValues.setId(id);
	}
	
	public boolean isMapToEveryone() {
		return mapToEveryone;
	}
	public void setMapToEveryone(boolean mapToEveryone) {
		this.mapToEveryone = mapToEveryone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(List<String> groupNames) {
		if(groupNames.size() > 0 && groupNames.get(0).compareToIgnoreCase("Everyone") == 0){
			mapToEveryone = true;
		}
		if(groupNames.size() > 0 && groupNames.get(0).compareToIgnoreCase("AllAuthenticated") == 0){
			mapToAllAuthenticated = true;
		}
		this.groupNames = groupNames;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<String> getUserNames() {
		return userNames;
	}
	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}
	
	public Element getAuthorizationNode(){
		Element user, group, special, role;
		Element authNode = DocumentHelper.createElement("authorizations");
		
		authNode.addAttribute("xmi:id", "RoleAssignment_" + id);
		
		//checking if this role should be mapped to special user 'Everyone'
		if(mapToEveryone){
			special = authNode.addElement("specialSubjects");
			special.addAttribute("xmi:type", "com.ibm.ejs.models.base.bindings.applicationbnd:Everyone");
			special.addAttribute("xmi:id", "Everyone_" + Random.getUniqueId());
			special.addAttribute("name", "Everyone");
			
			role = authNode.addElement("role");
			role.addAttribute("href", "META-INF/application.xml#SecurityRole_" + id);
			
			return authNode;
		}
		
		//checking if this role should be mapped to special user 'Everyone'
		if(mapToAllAuthenticated){
			special = authNode.addElement("specialSubjects");
			special.addAttribute("xmi:type", "com.ibm.ejs.models.base.bindings.applicationbnd:AllAuthenticated");
			special.addAttribute("xmi:id", "AllAuthenticated_" + id);
			special.addAttribute("name", "AllAuthenticated");
			
			role = authNode.addElement("role");
			role.addAttribute("href", "META-INF/application.xml#SecurityRole_" + id);
			
			return authNode;
		}
		
		//adding username bindings
		if(userNames != null && userNames.size() > 0){
			for(String username : userNames){
				user = authNode.addElement("users");
				user.addAttribute("xmi:id", "User_" + id);
				user.addAttribute("name", username);
			}
		}
		
		//adding group bindings
		if(groupNames != null && groupNames.size() > 0){
			for(String groupname : groupNames){
				group = authNode.addElement("groups");
				group.addAttribute("xmi:id", "Group_" + id);
				group.addAttribute("name", groupname);
			}
		}
		
		//adding role ref node
		role = authNode.addElement("role");
		role.addAttribute("href", "META-INF/application.xml#SecurityRole_" + id);
		
		return authNode;
	}
	
	public String toString(){
		String ret = "\n\t";
		if(mapToEveryone){
			return ret + roleName + "=Everyone";
		}
		if(groupNames != null){
			ret += roleName + "=";
			for(String grp : groupNames){
				ret += grp + ";";
			}
		}
		if(userNames != null){
			ret += "\n\t" + roleName + "=";
			for(String user : userNames){
				ret += user + ";";
			}
		}
		return ret;
	}
}
