package no.nav.maven.plugins.descriptor.mojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.deploy.spi.DeploymentConfiguration;

import no.nav.maven.plugins.descriptor.config.DeploymentDescriptorMojoConfig;
import no.nav.maven.plugins.descriptor.config.EarConfig;
import no.nav.maven.plugins.descriptor.config.EjbMethodPermissionConfig;
import no.nav.maven.plugins.descriptor.config.EjbJarConfig;
import no.nav.maven.plugins.descriptor.config.SecurityRoleConfig;
import no.nav.maven.plugins.descriptor.config.EjbMethodPermissionConfig.MethodInterfaceType;
import no.nav.maven.plugins.descriptor.config.loader.ConfigLoader;
import no.nav.maven.plugins.descriptor.config.loader.FileSystemSpringConfigLoader;
import no.nav.maven.plugins.descriptor.jee.ApplicationDescriptorEditor;
import no.nav.maven.plugins.descriptor.jee.EjbJarAssemblyDescriptorEditor;
import no.nav.maven.plugins.descriptor.jee.EjbJarEditor;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmApplicationBndEditor;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonArchiveFactoryRegistry;
import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonarchiveFactory;
import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonarchivePackage;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveOptions;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.FileIterator;
import org.eclipse.jst.j2ee.commonarchivecore.internal.impl.CommonarchiveFactoryImpl;



public abstract class AbstractDeploymentDescriptorMojo extends AbstractMojo{
	
	/**
	 * @parameter
	 */
	private File ear;
	/**
	 * @parameter
	 */
	private String springConfigFile; 
	
	
	private DeploymentDescriptorMojoConfig mojoConfig;
	
	public String getSpringConfigFile() {
		return springConfigFile;
	}
	public void setSpringConfigFile(String springConfigFile) {
		this.springConfigFile = springConfigFile;
	}
	protected File getEar() {
		return ear;
	}
	public void setEar(File ear) {
		this.ear = ear;
	}
	
	public DeploymentDescriptorMojoConfig getDeploymentDescriptorMojoConfig(){
		if(mojoConfig == null){
			if(springConfigFile != null){
				ConfigLoader configLoader = new FileSystemSpringConfigLoader(springConfigFile);
				mojoConfig = configLoader.getDeploymentDescriptorMojoConfig();
			} 
		}
		return mojoConfig;
	}
	
	protected void addSecurityRoleBindings(EARFile archive, EarConfig earConfig) throws MojoExecutionException {
		
		try {
			if(earConfig != null){
				getLog().info("Adding role bindings to archive.");
				List<SecurityRoleConfig> securityRoles = earConfig.getSecurityRoles();
				ApplicationDescriptorEditor app = new ApplicationDescriptorEditor(archive);
				app.addSecurityRoles(securityRoles);
				app.save();
				IbmApplicationBndEditor bnd = new IbmApplicationBndEditor(archive, app.getApplication());
				bnd.addRoleBindings(securityRoles);
				bnd.save();
			} else {
				getLog().error("No ear config found for archive.");
			}
		} catch (IOException e) {
			throw new MojoExecutionException("An IOException has occured when attempting to add security roles to EAR.", e);
		}
	}
	
	protected EARFile openEarFile(String absolutePath) throws OpenFailureException {
		CommonarchiveFactory archiveFactory = CommonarchiveFactoryImpl.getActiveFactory();
		EARFile earFile = null;
		ArchiveOptions arcOpts = new ArchiveOptions();
		//Needed in order to save an archive in which you have added a file.
		arcOpts.setSaveOnlyDirtyMofResources(false);
		earFile = archiveFactory.openEARFile(arcOpts, absolutePath);
		return earFile;	
	}
	
	protected void closeEarFile(EARFile earFile) throws SaveFailureException, ReopenException {	
		try {
			
			 /* Needed when the following options are set:
			 *  ArchiveOptions arcOpts = new ArchiveOptions();
			 *  arcOpts.setSaveOnlyDirtyMofResources(false);
			 *  for opening a EARFile. If this is not done you may risk 
			 *  that files loaded during earFileOpen are not saved. 
			 */
			earFile.getFilesForSave();
		} catch (IOException e) {
			e.printStackTrace();
		}
		earFile.save();
		earFile.close();
	}
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		processEar();
	}
	
	public void processEar() throws MojoExecutionException {
		
		try {
			getLog().info("Executing deployment descriptor mojo.");
			getLog().info("EAR file = " + getEar());
			String earPath = getEar().getAbsolutePath();
			EARFile earFile = openEarFile(earPath);
			String applicationName = earFile.getName();
			EarConfig earConfig = null;
			if(getDeploymentDescriptorMojoConfig() != null){
				earConfig = getDeploymentDescriptorMojoConfig().getEarDescriptorConfig(applicationName);
			}
			executeDescriptorOperations(earFile, earConfig);
			closeEarFile(earFile);
			getLog().info("Finished processing for the ear " + getEar());
		} catch (OpenFailureException e) {
			throw new MojoExecutionException("Failed to open EAR file", e);
		} catch (SaveFailureException e) {
			throw new MojoExecutionException("Failed to save EAR file", e);
		} catch (ReopenException e) {
			throw new MojoExecutionException("Failed to reopen EAR file", e);
		} 
	}
	
	public abstract void executeDescriptorOperations(EARFile earFile, EarConfig earConfig) throws MojoExecutionException;
	
}
