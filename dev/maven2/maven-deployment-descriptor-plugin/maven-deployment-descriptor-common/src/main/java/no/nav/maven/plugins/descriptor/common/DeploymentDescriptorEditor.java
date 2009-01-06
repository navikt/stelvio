package no.nav.maven.plugins.descriptor.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.jst.j2ee.application.ApplicationResource;
import org.eclipse.jst.j2ee.common.CommonFactory;
import org.eclipse.jst.j2ee.common.CommonPackage;
import org.eclipse.jst.j2ee.common.DescriptionGroup;
import org.eclipse.jst.j2ee.common.internal.impl.J2EEResourceFactoryRegistry;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveURIConverterImpl;
import org.eclipse.jst.j2ee.ejb.EJBJar;


import com.ibm.etools.webservice.WebServiceWASInit;
import com.ibm.etools.webservice.deploy.core.Utils;

public abstract class DeploymentDescriptorEditor<T> {
		private URI resourceURI;
	    private Resource resource;
	    private Archive archive;
	    private ResourceSetImpl resourcesetimpl;
	    //protected static final CommonFactory COMMONFACTORY = CommonPackage.eINSTANCE.getCommonFactory();
	    
	    static {
		    WebServiceWASInit.init();
		}
	    
	    public DeploymentDescriptorEditor(Archive archive, String resourceName) {
	    	this.archive = archive;
	        this.resourcesetimpl = new ResourceSetImpl();
	        ArchiveURIConverterImpl archiveuriconverterimpl = new ArchiveURIConverterImpl(archive, null);
	        String resourceString = Utils.getModulePath(archive, resourceName);
	        resourcesetimpl.setResourceFactoryRegistry(J2EEResourceFactoryRegistry.INSTANCE);
	        resourcesetimpl.setURIConverter(archiveuriconverterimpl);
	    	this.resourceURI = URI.createURI(resourceString);
	    	setResource(getResource(getResourceURI()));

	    }

	    protected CommonFactory getCommmonFactory(){
	    	return CommonPackage.eINSTANCE.getCommonFactory();
	    }
	    
		protected Archive getArchive() {
			return archive;
		}

		protected void setArchive(Archive archive) {
			this.archive = archive;
		}

		protected Resource getResource() {
			return resource;
		}

		protected void setResource(Resource resource) {
			this.resource = resource;
		}

		protected URI getResourceURI() {
			return resourceURI;
		}

		protected void setResourceURI(URI resourceURI) {
			this.resourceURI = resourceURI;
		}
		
		protected Resource getResource(URI resourceURI) {
			try{
				Resource r = archive.getMofResource(resourceURI.path());
				return r;
			} catch (FileNotFoundException e){
				return resourcesetimpl.createResource(resourceURI);
			}
		}   
		@SuppressWarnings("unchecked")
		protected T getDescriptor(){
			EList contents = getResource().getContents();
			if(contents.size() > 0){
				return (T)contents.get(0);
			} else {
				contents.add(createDescriptorContent());
				return (T)contents.get(0);
			}
		}

		public void save() throws IOException {
	    	getArchive().addOrReplaceMofResource(getResource());
	    }    
		
		public void saveAs(String filename) throws IOException,  SaveFailureException, ReopenException{
			getArchive().saveAs(filename);
		}
		
		
		protected abstract T createDescriptorContent();
			
		
}
