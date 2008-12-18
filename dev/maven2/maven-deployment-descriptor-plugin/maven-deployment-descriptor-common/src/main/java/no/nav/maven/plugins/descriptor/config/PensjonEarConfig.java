package no.nav.maven.plugins.descriptor.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import no.nav.maven.plugins.descriptor.config.EjbMethodPermissionConfig.MethodInterfaceType;

public class PensjonEarConfig extends EarConfig {

	private Map<String, SecurityRoleConfig> ejbSecurityRoleMap;
	private EjbMethodPermissionConfig defaultEjbMethodPermission;
	
	public EjbMethodPermissionConfig getDefaultEjbMethodPermission() {
		return defaultEjbMethodPermission;
	}

	public void setDefaultEjbMethodPermission(
			EjbMethodPermissionConfig defaultEjbMethodPermission) {
		this.defaultEjbMethodPermission = defaultEjbMethodPermission;
	}

	public Map<String, SecurityRoleConfig> getEjbSecurityRoleMap() {
		return ejbSecurityRoleMap;
	}

	public void setEjbSecurityRoleMap(
			Map<String, SecurityRoleConfig> ejbSecurityRoleMap) {
		this.ejbSecurityRoleMap = ejbSecurityRoleMap;
	}

	@Override
	public List<EjbJarConfig> getEjbJarFileConfigList() {	
		if(getEjbSecurityRoleMap() != null){
			List<EjbJarConfig> jarList = new ArrayList<EjbJarConfig>();
			Set<Entry<String,SecurityRoleConfig>> entries = getEjbSecurityRoleMap().entrySet();
			for (Entry<String, SecurityRoleConfig> entry : entries) {
				EjbJarConfig jarConfig = new EjbJarConfig();
				jarConfig.setJarName(entry.getKey());
				SecurityRoleConfig role = entry.getValue();
				jarConfig.setEjbConfigList(createEjbMethodPermissionConfig(role));
				jarList.add(jarConfig);
			}
			return jarList;
		}
		return super.getEjbJarFileConfigList();
	}
	private List<EjbMethodPermissionConfig> createEjbMethodPermissionConfig(SecurityRoleConfig securityRoleConfig){
		EjbMethodPermissionConfig defaultPermission = new EjbMethodPermissionConfig();
		defaultPermission.setMethodInterfaceType(getDefaultEjbMethodPermission().getMethodInterfaceType());
		defaultPermission.setMethodName(getDefaultEjbMethodPermission().getMethodName());
		defaultPermission.setApplyToAllEntepriseBeans(getDefaultEjbMethodPermission().isApplyToAllEntepriseBeans());
		List<SecurityRoleConfig> roles = new ArrayList<SecurityRoleConfig>();
		roles.add(securityRoleConfig);
		defaultPermission.setSecurityRoles(roles);
		List<EjbMethodPermissionConfig> permissionList = new ArrayList<EjbMethodPermissionConfig>();
		permissionList.add(defaultPermission);
		return permissionList;
	}
	
}
