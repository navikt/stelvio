/**
 * 
 */
package no.nav.maven.plugins.descriptor.websphere;

import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonarchiveFactory;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveOptions;
import org.eclipse.jst.j2ee.commonarchivecore.internal.impl.CommonarchiveFactoryImpl;

import junit.framework.TestCase;

/**
 * @author utvikler
 *
 */
public abstract class AbstractDeploymentDescriptorTestCase extends TestCase {

	
	public AbstractDeploymentDescriptorTestCase(String arg0) {
		super(arg0);
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

}
