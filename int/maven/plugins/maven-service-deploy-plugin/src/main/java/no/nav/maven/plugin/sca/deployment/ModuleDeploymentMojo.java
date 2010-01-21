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
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
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
	private void executeInternal() throws MojoExecutionException {
		try {
			Collection<String> webServiceExportNames = getWebServiceExportNames();
			if (!webServiceExportNames.isEmpty()) {
				getLog().info(webServiceExportNames.toString());

				Document deploymentDescriptorDocument;
				Element rootElement;
				File deploymentDescriptorFile = new File(outputDirectory, "ibm-deploy.scaj2ee");
				if (deploymentDescriptorFile.exists()) {
					deploymentDescriptorDocument = saxBuilder.build(deploymentDescriptorFile);
					rootElement = deploymentDescriptorDocument.getRootElement();
				} else {
					getLog().debug("Deployment descriptor does not exist - creating one.");
					rootElement = new Element("IntegrationModuleDeploymentConfiguration", "scaj2ee",
							"http://www.ibm.com/xmlns/prod/websphere/sca/j2ee/6.0.2");
					Namespace xmiNamespace = Namespace.getNamespace("xmi", "http://www.omg.org/XMI");
					rootElement.addNamespaceDeclaration(xmiNamespace);
					rootElement.setAttribute("version", "2.0", xmiNamespace);
					deploymentDescriptorDocument = new Document(rootElement);
				}

				Element wsExportsElement = rootElement.getChild("wsExports");
				if (wsExportsElement == null) {
					wsExportsElement = new Element("wsExports");
					rootElement.addContent(wsExportsElement);
				}
				
				Element exportHandlerElement = new Element("exportHandler");
				for (Handler handler : handlers) {
					Element handlerElement = new Element("handler");
					handlerElement.setAttribute("handlerName", handler.getHandlerName());
					handlerElement.setAttribute("handlerClass", handler.getHandlerClass());
					exportHandlerElement.addContent(handlerElement);
				}
				
				
				// TODO: Support update of wsExportElements
				List<Element> wsExportElements = wsExportsElement.getChildren("wsExport");
				wsExportElements.clear();
				for (String webServiceExportName : webServiceExportNames) {
					Element wsExportElement = new Element("wsExport");
					wsExportElement.addContent(new Element("name").setText(webServiceExportName));
					wsExportElement.addContent(exportHandlerElement);
					wsExportElements.add(wsExportElement);
				}

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
