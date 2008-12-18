package no.nav.maven.plugins.descriptor.websphere;

import no.nav.maven.plugins.descriptor.common.DeploymentDescriptorEditor;

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

import com.ibm.etools.webservice.WebServiceWASInit;
import com.ibm.etools.webservice.wscbnd.impl.WscbndPackageImpl;

public abstract class IbmWebServiceDescriptorEditor<T> extends DeploymentDescriptorEditor<T> {
	
	public IbmWebServiceDescriptorEditor(Archive archive, String resourceName) {
		super(archive, resourceName);
	}
	
	protected abstract T createDescriptorContent();
}
