package no.nav.maven.plugin.wsi;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.wsi.WSIException;
import org.wsi.test.analyzer.BasicProfileAnalyzer;
import org.wsi.test.analyzer.config.AnalyzerConfig;
import org.wsi.test.analyzer.config.WSDLElement;
import org.wsi.test.analyzer.config.WSDLReference;
import org.wsi.test.analyzer.config.impl.WSDLElementImpl;
import org.wsi.test.analyzer.config.impl.WSDLReferenceImpl;
import org.wsi.test.common.AddStyleSheet;
import org.wsi.test.common.impl.AddStyleSheetImpl;
import org.wsi.test.document.DocumentFactory;

/**
 * 
 * @author test@example.com
 * 
 * @goal analyze
 */
public class AnalyzeMojo extends AbstractMojo {
	/**
	 * @parameter
	 */
	private boolean verbose;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @readonly
	 */
	private File projectBuildDirectory;

	/**
	 * @parameter expression="${failOnFailure}" default-value="true"
	 */
	private boolean failOnFailure;

	/**
	 * @parameter expression="${project.build.sourceDirectory}"
	 */
	private File basedir;

	/**
	 * @parameter
	 */
	private Set<String> includes = new HashSet<String>();

	/**
	 * @parameter
	 */
	private Set<String> excludes = new HashSet<String>();

	private WSDLReader wsdlReader;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			DocumentFactory documentFactory = DocumentFactory.newInstance();

			AnalyzerConfig analyzerConfig = documentFactory.newAnalyzerConfig();
			analyzerConfig.setVerboseOption(verbose);

			// TODO: Make selection of profile configurable
			analyzerConfig.setTestAssertionsDocumentLocation("common/profiles/SSBP10_BP11_TAD.xml");

			analyzerConfig.setReportLocation(new File(projectBuildDirectory, "report.xml").getPath());
			analyzerConfig.setReplaceReport(true);

			AddStyleSheet addStyleSheet = new AddStyleSheetImpl();
			addStyleSheet.setHref("common/xsl/report.xsl");
			addStyleSheet.setType("text/xsl");
			analyzerConfig.setAddStyleSheet(addStyleSheet);

			WSDLReference wsdlReference = getWSDLReference();

			analyzerConfig.setWSDLReference(wsdlReference);

			if (getLog().isDebugEnabled()) {
				getLog().debug(analyzerConfig.toString());
			}

			int result = new BasicProfileAnalyzer(Collections.singletonList(analyzerConfig)).validateConformance();
			if (failOnFailure && result > 0) {
				throw new MojoFailureException("WSI validation failed, check report for details.");
			}

		} catch (WSIException e) {
			throw new MojoExecutionException("WSIException", e);
		}
	}

	private WSDLReference getWSDLReference() throws MojoExecutionException {
		// TODO: Find all applicable port types
		try {
			WSDLReference wsdlReference = new WSDLReferenceImpl();

			DirectoryScanner scanner = new DirectoryScanner();
			scanner.setBasedir(basedir);
			if (!includes.isEmpty()) {
				scanner.setIncludes(includes.toArray(new String[includes.size()]));
			} else {
				scanner.setIncludes(new String[] { "**/*.wsdl" });
			}
			if (excludes.isEmpty()) {
				scanner.setExcludes(excludes.toArray(new String[excludes.size()]));
			}
			scanner.scan();

			String[] includedFiles = scanner.getIncludedFiles();

			for (int i = 0; i < includedFiles.length; i++) {
				String includedFile = includedFiles[i];
				Definition wsdl = getWsdlReader().readWSDL(basedir.toString(), includedFile);
				Map<QName, PortType> portTypes = wsdl.getPortTypes();
				for (QName qName : portTypes.keySet()) {
					wsdlReference.setWSDLLocation(includedFile);
					WSDLElement wsdlElement = new WSDLElementImpl();
					wsdlReference.setWSDLElement(wsdlElement);
					wsdlElement.setType("portType");
					wsdlElement.setNamespace(qName.getNamespaceURI());
					wsdlElement.setName(qName.getLocalPart());

					return wsdlReference;
				}
			}
			return null;
		} catch (WSDLException e) {
			throw new MojoExecutionException("Error parsing WSDL", e);
		}
	}

	private WSDLReader getWsdlReader() throws WSDLException {
		if (wsdlReader == null) {
			wsdlReader = WSDLFactory.newInstance().newWSDLReader();
			wsdlReader.setFeature("javax.wsdl.verbose", false);
			wsdlReader.setFeature("javax.wsdl.importDocuments", false);
		}
		return wsdlReader;
	}
}
