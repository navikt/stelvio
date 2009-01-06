package no.nav.maven.plugins.descriptor.websphere.bnd;

import java.io.IOException;

import no.nav.maven.plugins.descriptor.websphere.AbstractDeploymentDescriptorTestCase;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceExtEditor;

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;

public class IbmWebServiceBndEditorTest extends AbstractDeploymentDescriptorTestCase {

	public IbmWebServiceBndEditorTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testCreateDescriptorContent() {
//		fail("Not yet implemented");
//	}

	public void testGetDescriptor() {
//		String archivePath = getClass().getResource("/nav-cons-pen-pselv-person-NOTOKENS.ear").getFile();
//		Archive archive;
//		try {
//			archive = (Archive) openEarFile(archivePath).getEJBJarFiles().get(0);
//			IbmWebServiceExtEditor wsExtEditor = new IbmWebServiceExtEditor(archive);
//			//System.out.println(wsExtEditor);
//		} catch (OpenFailureException e) {
//			fail();
//		}
	}
	
	public void testAddLTPATokenConsumer(){
	
//		String archivePath = getClass().getResource("/nav-cons-pen-pselv-person-NOTOKENS.ear").getFile();
//		Archive archive;
//		String partRef = "LTPA";
//		try {
//			archive = (Archive) openEarFile(archivePath).getEJBJarFiles().get(0);
//			IbmWebServiceBndEditor wsBndEditor = new IbmWebServiceBndEditor(archive);			
//			wsBndEditor.addLTPATokenConsumer(partRef);
//			wsBndEditor.save();
//			//wsBndEditor.saveAs("e:/nav-cons-pen-pselv-person-NY.ear");
//			
//		} catch (OpenFailureException e) {
//			fail();
////		} catch (SaveFailureException e) {
////			fail();
////		} catch (ReopenException e) {
////			fail();
//		} catch (IOException e) {
//			fail();
//		}
//	
	}
}
