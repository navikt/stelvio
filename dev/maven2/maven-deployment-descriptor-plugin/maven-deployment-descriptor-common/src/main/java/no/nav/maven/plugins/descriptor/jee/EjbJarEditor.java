package no.nav.maven.plugins.descriptor.jee;

import java.io.IOException;

import no.nav.maven.plugins.descriptor.common.DeploymentDescriptorEditor;

import org.eclipse.jst.j2ee.common.CommonFactory;
import org.eclipse.jst.j2ee.common.CommonPackage;

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.ejb.AssemblyDescriptor;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EjbFactory;
import org.eclipse.jst.j2ee.ejb.EjbPackage;


public class EjbJarEditor extends DeploymentDescriptorEditor<EJBJar>{

	protected EJBJar ejbJar;	
	private static final String EJB_JAR_FILE_NAME = "ejb-jar.xml";
	//protected static final EjbFactory EJBFACTORY = EjbPackage.eINSTANCE.getEjbFactory();
	
	public EjbJarEditor(Archive archive){
		super(archive, EJB_JAR_FILE_NAME);
		ejbJar = getDescriptor();
	}
	
	protected EjbFactory getEjbFactory(){
		return EjbPackage.eINSTANCE.getEjbFactory();
	}
	
	protected AssemblyDescriptor getAssemblyDescriptor(){
		if(ejbJar.getAssemblyDescriptor() == null){
			ejbJar.setAssemblyDescriptor(getEjbFactory().createAssemblyDescriptor());
		}
		return ejbJar.getAssemblyDescriptor();
	}
	
	protected EJBJar createDescriptorContent(){		
		return getEjbFactory().createEJBJar();
	}
}
