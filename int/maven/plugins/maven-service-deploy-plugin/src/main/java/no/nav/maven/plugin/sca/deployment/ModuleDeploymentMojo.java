package no.nav.maven.plugin.sca.deployment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 * 
 * @goal module-deployment
 * @phase process-resources
 */
public class ModuleDeploymentMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project.packaging}"
	 * @required
	 * @readonly
	 */
	private String packaging;
	
	/**
	 * @parameter expression="${project.basedir}"
	 * @required
	 * @readonly
	 */
	protected File baseDirectory;

	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 * @readonly
	 */
	private File outputDirectory;

	/**
	 * @parameter
	 */
	private List<Handler> handlers;

	private SAXBuilder saxBuilder;
	private XPath webServiceExportXPath;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if ("pom".equals(packaging)) {
			// POM project - nothing to do
			return;
		}
		if (handlers == null || handlers.isEmpty()) {
			// No handlers defined - nothing to do
			return;
		}
		init();
		executeInternal();
	}

	private void init() throws MojoExecutionException {
		try {
			saxBuilder = new SAXBuilder();
			webServiceExportXPath = XPath.newInstance("//esbBinding[@xsi:type='webservice:WebServiceExportBinding']");
		} catch (JDOMException e) {
			throw new MojoExecutionException("Error initalizing XML processing framework", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void executeInternal() throws MojoExecutionException, MojoFailureException {
		try {
			Collection<String> webServiceExportNames = getWebServiceExportNames();
			if (!webServiceExportNames.isEmpty()) {
				Document deploymentDescriptorDocument;
				File sourceDeploymentDescriptorFile = new File(baseDirectory, "ibm-deploy.scaj2ee");
				File deploymentDescriptorFile = new File(outputDirectory, "ibm-deploy.scaj2ee");
				if (sourceDeploymentDescriptorFile.exists()) {
					FileUtils.copyFile(sourceDeploymentDescriptorFile, deploymentDescriptorFile);
					deploymentDescriptorDocument = saxBuilder.build(deploymentDescriptorFile);
				} else {
					getLog().debug("Deployment descriptor does not exist - creating one.");
					deploymentDescriptorDocument = new Document();
				}

				getLog().info("Adding handlers " + handlers + " to all web service exports " + webServiceExportNames + ".");
				ModuleDeploymentDescriptorEditor deploymentDescriptorEditor = new ModuleDeploymentDescriptorEditor(
						deploymentDescriptorDocument);
				deploymentDescriptorEditor.createOrUpdateExportHandlers(webServiceExportNames, handlers);

				XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
				xmlOutputter.output(deploymentDescriptorDocument, new FileWriter(deploymentDescriptorFile));
			}
		} catch (IOException e) {
			throw new MojoExecutionException("Error accessing file system", e);
		} catch (JDOMException e) {
			throw new MojoExecutionException("Error processing XML", e);
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<String> getWebServiceExportNames() throws IOException, JDOMException {
		List<File> exportFiles = FileUtils.getFiles(outputDirectory, "**/*.export", null);

		Collection<String> webServiceExportNames = new ArrayList<String>(exportFiles.size());

		for (File exportFile : exportFiles) {
			Document exportDocument = saxBuilder.build(exportFile);
			if (webServiceExportXPath.selectSingleNode(exportDocument) != null) {
				webServiceExportNames.add(exportDocument.getRootElement().getAttributeValue("name"));
			}
		}

		return webServiceExportNames;
	}
}
