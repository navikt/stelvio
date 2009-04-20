package no.nav.maven.plugins.descriptor.jee;

import java.util.ArrayList;
import java.util.List;

import no.nav.maven.plugins.descriptor.config.EjbMethodPermissionConfig;
import no.nav.maven.plugins.descriptor.config.EjbMethodPermissionConfig.MethodInterfaceType;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.j2ee.common.SecurityRole;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EjbMethodElementHelper;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.MethodElement;
import org.eclipse.jst.j2ee.ejb.MethodElementKind;
import org.eclipse.jst.j2ee.ejb.MethodPermission;

public class EjbJarAssemblyDescriptorEditor extends EjbJarEditor{
	
	private boolean onePermissionPerEnterpriseBean = false; 
	
	public EjbJarAssemblyDescriptorEditor(Archive archive){
		super(archive);		
	}

	public void addEjbMethodPermission(EjbMethodPermissionConfig ejbPermission){
		if(ejbPermission != null){
			addSecurityRoles(ejbPermission.getRoleNames());
			
			if(ejbPermission.isApplyToAllEntepriseBeans()){
				addEjbMethodPermissionToAllEnterpriseBeans(ejbPermission);
			} else {
				String ejbName = ejbPermission.getEjbName();
				EnterpriseBean bean = ejbJar.getEnterpriseBeanNamed(ejbName);
				addMethodPermission(ejbPermission, bean);
			}
		}
	}
	
	public void addEjbMethodPermissionToAllEnterpriseBeans(EjbMethodPermissionConfig ejbPermission){
		EList beans = ejbJar.getEnterpriseBeans();
		if(onePermissionPerEnterpriseBean){
			for (Object object : beans) {
				EnterpriseBean bean = (EnterpriseBean)object;
				addMethodPermission(ejbPermission, bean);
			}
		} else {
			EnterpriseBean[] entBeans = (EnterpriseBean[])beans.toArray();
			addMethodPermission(ejbPermission, entBeans);
		}
		
	}
	
	public final boolean containsBusinessProcesses() {
		EList beans = ejbJar.getEnterpriseBeans();
		for (Object b : beans) {
			EnterpriseBean bean = (EnterpriseBean)b;
			String beanClassName = bean.getEjbClassName();
			if("com.ibm.bpe.framework.sca.ProcessSessionBean".equals(beanClassName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public final List<String> getEjbSecurityRoleNames() {
		List<String> roles = new ArrayList<String>();
		EList secRoles = getSecurityRoles();
		
		for(Object o : secRoles) {
			roles.add(((SecurityRole)o).getRoleName());
		}
		
		return roles;
	}
	
	private void addSecurityRoles(List<String> roleNames){
		for (String string : roleNames) {
			addSecurityRole(string);
		}
	}
	
	private void addSecurityRole(String roleName){
		if(!isSecurityRolePresent(roleName)){
			SecurityRole role = createNewSecurityRole(roleName);
			getSecurityRoles().add(role);
		}
	}
	
	private boolean isSecurityRolePresent(String roleName){
		return ejbJar.containsSecurityRole(roleName);
	}
	
	private EList getSecurityRoles(){
		return getAssemblyDescriptor().getSecurityRoles();
	}
	
	private SecurityRole createNewSecurityRole(String roleName){
		SecurityRole securityRole= getCommmonFactory().createSecurityRole();
		securityRole.setRoleName(roleName);	
		return securityRole;
	}
	
	private void addMethodPermission(EjbMethodPermissionConfig ejbPermission, 
			EnterpriseBean... beans){
		MethodPermission permission = createNewMethodPermission(ejbPermission, beans);
		if(!isMethodPermissionPresent(permission)){
			getMethodPermissions().add(permission);
		}
	}
	
	private MethodPermission createNewMethodPermission(EjbMethodPermissionConfig ejbPermission, 
			EnterpriseBean... beans) {
		MethodPermission permission = getEjbFactory().createMethodPermission();
		EList secRoles = permission.getRoles();
		for (String role : ejbPermission.getRoleNames()) {
			SecurityRole secRole = createNewSecurityRole(role);
			secRoles.add(secRole);
		}
		for(EnterpriseBean bean : beans){
			MethodElementKind methodElementKind = createMethodElementKind(ejbPermission.getMethodInterfaceType());
			MethodElement element = 
				createNewMethodElement(bean, ejbPermission.getMethodName(), methodElementKind );
			permission.getMethodElements().add(element);
		}
		return permission;
	}
	
	private boolean isMethodPermissionPresent(MethodPermission methodPermission){
		EList elements = methodPermission.getMethodElements();
		for (Object object : elements) {
			if(isMethodElementPresent((MethodElement)object)){
				return true;
			}
		}
		return false;
	}
	
	private EList getMethodPermissions(){
		return getAssemblyDescriptor().getMethodPermissions();
	}
	
	private boolean isMethodElementPresent(MethodElement element){
		List elements = getAssemblyDescriptor().
		getMethodPermissionMethodElements(element.getEnterpriseBean());
		for (Object object : elements) {
			MethodElement elementInList = (MethodElement)object;
			if(isEqual(elementInList, element)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isEqual(MethodElement element1, MethodElement element2){
		return (element1.getName().equals(element2.getName()) &&
				element1.getEnterpriseBean().equals(element2.getEnterpriseBean()) &&
				element1.getType().equals(element2.getType()));
	}
	
	private MethodElement createNewMethodElement(EnterpriseBean bean, String methodName, 
			MethodElementKind interfaceType){
		MethodElement element = getEjbFactory().createMethodElement();
		element.setType(interfaceType);
		element.setName(methodName);
		
		element.setEnterpriseBean(bean);
		return element;
	}
	
	private MethodElementKind createMethodElementKind(MethodInterfaceType interfaceType){
		if(interfaceType == MethodInterfaceType.HOME){
			return MethodElementKind.HOME_LITERAL;
		} else if(interfaceType == MethodInterfaceType.REMOTE){
			return MethodElementKind.REMOTE_LITERAL;
		} else if(interfaceType == MethodInterfaceType.LOCAL){
			return MethodElementKind.LOCAL_LITERAL;
		} else if(interfaceType == MethodInterfaceType.SERVICE_ENDPOINT){
			return MethodElementKind.SERVICE_ENDPOINT_LITERAL;
		} else {
			return MethodElementKind.UNSPECIFIED_LITERAL;
		}
	}
	
}
