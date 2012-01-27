package no.stelvio.ibm.websphere.esb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

public class WSDLUtils {

	private static WSDLReader wsdlReader;
	
	public static Collection<Definition> getWSDLs(File workingDir) throws IOException, WSDLException, MojoFailureException {
		Collection<Definition> wsdls = new ArrayList<Definition>();
		
		Collection<File> wsdlFiles = FileUtils.getFiles(workingDir, "**/*.wsdl", null);
		for (File wsdlFile : wsdlFiles) {
			wsdls.add(getWsdlReader().readWSDL(wsdlFile.getAbsolutePath()));
		}
		
		return wsdls;
	}
	
	public static WSDLReader getWsdlReader() throws WSDLException {
		if (wsdlReader == null) {
			wsdlReader = WSDLFactory.newInstance().newWSDLReader();
			wsdlReader.setFeature("javax.wsdl.verbose", false);
			wsdlReader.setFeature("javax.wsdl.importDocuments", true);
		}
		return wsdlReader;
		}
	
	
}
