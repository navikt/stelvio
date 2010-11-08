package no.nav.maven.plugin.wsi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
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
import org.wsi.util.WSIProperties;

/**
 * 
 * @author test@example.com
 * 
 * @goal analyze
 */
public class AnalyzeMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @component roleHint="zip"
	 */
	private UnArchiver unArchiver;

	/**
	 * @parameter expression="${plugin.artifacts}"
	 * @readonly
	 */
	private Collection<Artifact> pluginDependendencies;

	/**
	 * @parameter
	 */
	private boolean verbose;

	/**
	 * @parameter expression="${project.build.directory}/wsi-tools"
	 */
	private File reportDirectory;

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
		if (!"pom".equals(project.getPackaging())) {
			try {
				reportDirectory.mkdirs();
				extractWsiToolsFiles();
	
				DocumentFactory documentFactory = DocumentFactory.newInstance();
	
				AnalyzerConfig analyzerConfig = documentFactory.newAnalyzerConfig();
				analyzerConfig.setLocation(reportDirectory.getPath());
				analyzerConfig.setVerboseOption(verbose);
	
				// TODO: Make selection of profile configurable
				analyzerConfig.setTestAssertionsDocumentLocation(new File(reportDirectory, "common/profiles/SSBP10_BP11_TAD.xml")
						.getPath());
	
				analyzerConfig.setReplaceReport(true);
	
				AddStyleSheet addStyleSheet = new AddStyleSheetImpl();
				addStyleSheet.setHref("common/xsl/report.xsl");
				addStyleSheet.setType("text/xsl");
				analyzerConfig.setAddStyleSheet(addStyleSheet);
	
				// This is sort of a "hack" to make the WSI Testing Tool find schemas
				// TODO: This can be improved by setting properties explicitly (see class org.wsi.test.util.TestUtils for details)
				System.setProperty(WSIProperties.PROP_WSI_HOME, reportDirectory.getPath());
	
				int aggregatedResult = 0;
				for (WSDLReference wsdlReference : getWSDLReferences()) {
					String reportFilename = wsdlReference.getWSDLElement().getName() + ".xml";
	
					analyzerConfig.setWSDLReference(wsdlReference);
					analyzerConfig.setReportLocation(new File(reportDirectory, reportFilename).getPath());
	
					if (getLog().isDebugEnabled()) {
						getLog().debug(analyzerConfig.toString());
					}
	
					int result = new BasicProfileAnalyzer(Collections.singletonList(analyzerConfig)).validateConformance();
	
					getLog().info(
							wsdlReference.getWSDLElement().getQName() + ":[" + (result == 0 ? "OK" : "FAILED") + "]" + ":"
									+ reportFilename);
	
					aggregatedResult += result;
				}
	
				if (failOnFailure && aggregatedResult > 0) {
					throw new MojoFailureException("WSI validation failed, check report for details.");
				}
	
			} catch (WSIException e) {
				throw new MojoExecutionException("WSIException", e);
			}
		} else {
			getLog().debug("Skipping analyze because packaging is pom");
		}
	}

	/**
	 * TODO: This method is needed because the tool require files to be available on the local file system. Can be improved?
	 */
	private void extractWsiToolsFiles() throws MojoExecutionException {
		try {
			String versionlessKey = ArtifactUtils.versionlessKey("org.wsi.test", "wsi-test-tools-files");
			for (Artifact pluginDependency : pluginDependendencies) {
				if (ArtifactUtils.versionlessKey(pluginDependency).equals(versionlessKey)) {
					// Found what I was looking for. Extract contents
					unArchiver.setDestDirectory(reportDirectory);
					unArchiver.setSourceFile(pluginDependency.getFile());
					unArchiver.extract();
					return;
				}
			}
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error extracting WSI tools files", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error extracting WSI tools files", e);
		}
		throw new MojoExecutionException("Dependency containing WSI Tools files not found. Unable to continue.");
	}

	private Collection<WSDLReference> getWSDLReferences() throws MojoExecutionException {
		try {
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

			Collection<WSDLReference> wsdlReferences = new ArrayList<WSDLReference>();
			for (int i = 0; i < includedFiles.length; i++) {
				String includedFile = includedFiles[i];
				Definition wsdl = getWsdlReader().readWSDL(basedir.toString(), includedFile);
				Map<QName, PortType> portTypes = wsdl.getPortTypes();
				for (QName qName : portTypes.keySet()) {
					WSDLReference wsdlReference = new WSDLReferenceImpl();
					wsdlReference.setWSDLLocation(new File(basedir, includedFile).getAbsolutePath());
					WSDLElement wsdlElement = new WSDLElementImpl();
					wsdlReference.setWSDLElement(wsdlElement);
					wsdlElement.setType("portType");
					wsdlElement.setNamespace(qName.getNamespaceURI());
					wsdlElement.setName(qName.getLocalPart());

					wsdlReferences.add(wsdlReference);
				}
			}
			return wsdlReferences;
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
