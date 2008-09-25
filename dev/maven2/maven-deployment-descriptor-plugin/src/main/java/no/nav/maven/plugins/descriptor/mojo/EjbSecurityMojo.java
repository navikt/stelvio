package no.nav.maven.plugins.descriptor.mojo;

import java.io.IOException;

import no.nav.maven.plugins.descriptor.config.EarConfig;
import no.nav.maven.plugins.descriptor.config.EjbJarConfig;
import no.nav.maven.plugins.descriptor.config.EjbMethodPermissionConfig;
import no.nav.maven.plugins.descriptor.jee.EjbJarAssemblyDescriptorEditor;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;

/**
 * @goal addEjbSecurity
 */
public class EjbSecurityMojo extends AbstractDeploymentDescriptorMojo{
		
	public void executeDescriptorOperations(EARFile earFile, EarConfig earConfig) throws MojoExecutionException {
		if(earConfig != null){
			addSecurityRoleBindings(earFile, earConfig);
			addMethodPermissionsToEjbJars(earFile, earConfig);
		} else {
			getLog().error("executeDescriptorOperations(): No ear config found for archive.");
		}
	}
	
	private void addMethodPermissionsToEjbJars(EARFile earFile, EarConfig earConfig) throws MojoExecutionException {
		
		for(EjbJarConfig jarConfig : earConfig.getEjbJarFileConfigList()){
			getLog().info("Searching for ejb jar file that matches the pattern: " + jarConfig.getJarName());
			EJBJarFile ejbJarFile = getEjbJarFile(earFile, jarConfig.getJarName());
			if(ejbJarFile != null){
				getLog().info("Found a matching jar: " + ejbJarFile.getName());
				addMethodPermissionsToEjb(ejbJarFile, jarConfig);
			} else {
				getLog().error("Could not find jar file: " + jarConfig.getJarName());
			}
		}
	}
	
	private EJBJarFile getEjbJarFile(EARFile earFile, String jarName){
		for(Object o: earFile.getEJBJarFiles()){
			EJBJarFile ejbJarFile = (EJBJarFile)o;
			if(ejbJarFile.getName().startsWith(jarName)){
				return ejbJarFile;
			}
		}
		return null;
	}
	
	private void addMethodPermissionsToEjb(EJBJarFile archive, EjbJarConfig jarConfig) throws MojoExecutionException {
		try {
			
			EjbJarAssemblyDescriptorEditor editor = new EjbJarAssemblyDescriptorEditor(archive);
			for(EjbMethodPermissionConfig ejbPermissionConfig : jarConfig.getEjbConfigList()){
				getLog().info("Adding ejb method permission to ejb " + archive.getName()
								+ " with security roles " + ejbPermissionConfig.getRoleNames());
				
				editor.addEjbMethodPermission(ejbPermissionConfig);
			}
			editor.save();
		} catch (IOException e) {
			throw new MojoExecutionException("An IOException has occurred while adding method permissions to EJB jar.", e);
		}
	}
}
