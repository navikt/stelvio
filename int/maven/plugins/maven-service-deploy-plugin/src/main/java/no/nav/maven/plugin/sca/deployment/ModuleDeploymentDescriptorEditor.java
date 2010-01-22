package no.nav.maven.plugin.sca.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

@SuppressWarnings("unchecked")
public class ModuleDeploymentDescriptorEditor {
	private static final String TARGET_NAMESPACE = "http://www.ibm.com/xmlns/prod/websphere/sca/j2ee/6.0.2";

	private Document deploymentDescriptorDocument;

	public ModuleDeploymentDescriptorEditor(Document deploymentDescriptorDocument) throws MojoFailureException {
		this.deploymentDescriptorDocument = deploymentDescriptorDocument;
	}

	public void createOrUpdateExportHandlers(Collection<String> webServiceExportNames, Collection<Handler> handlers) {
		Element wsExportsElement = getWsExportsElement();

		for (String webServiceExportName : webServiceExportNames) {
			Element wsExportElement = getWsExportElement(wsExportsElement, webServiceExportName);
			Element exportHandlerElement = getExportHandlerElement(wsExportElement);
			Collection<Element> handlerElementsToAdd = createHandlerElements(handlers);
			List<Element> handlerElements = exportHandlerElement.getChildren("handler");
			if (handlerElements.isEmpty()) {
				handlerElements.addAll(handlerElementsToAdd);
			} else {
				// TODO: Implement merging of handler elements
				throw new UnsupportedOperationException("Merging of Handlers is currently unsupported");
			}
		}
	}

	private Collection<Element> createHandlerElements(Collection<Handler> handlers) {
		Collection<Element> handlerElements = new ArrayList<Element>(handlers.size());
		for (Handler handler : handlers) {
			Element handlerElement = new Element("handler");
			if (handler.getDisplayName() != null) {
				handlerElement.setAttribute("displayName", handler.getDisplayName());
			}
			if (handler.getHandlerName() != null) {
				handlerElement.setAttribute("handlerName", handler.getHandlerName());
			}
			if (handler.getHandlerClass() != null) {
				handlerElement.setAttribute("handlerClass", handler.getHandlerClass());
			}
			if (handler.getDescription() != null) {
				Element descriptionsElement = new Element("descriptions");
				descriptionsElement.setAttribute("value", handler.getDescription());
				handlerElement.addContent(descriptionsElement);
			}
			handlerElements.add(handlerElement);
		}
		return handlerElements;
	}

	private Element getRootElement() {
		Element rootElement;
		if (deploymentDescriptorDocument.hasRootElement()) {
			rootElement = deploymentDescriptorDocument.getRootElement();
			String targetNamespace = rootElement.getNamespace().getURI();
			if (!TARGET_NAMESPACE.equals(targetNamespace)) {
				throw new RuntimeException("Unsupported target namespace in existing module deployment descriptor. Was "
						+ targetNamespace + ", expected " + TARGET_NAMESPACE + ".");
			}
		} else {
			rootElement = new Element("IntegrationModuleDeploymentConfiguration", "scaj2ee", TARGET_NAMESPACE);
			Namespace xmiNamespace = Namespace.getNamespace("xmi", "http://www.omg.org/XMI");
			rootElement.addNamespaceDeclaration(xmiNamespace);
			rootElement.setAttribute("version", "2.0", xmiNamespace);
			deploymentDescriptorDocument.setRootElement(rootElement);
		}
		return rootElement;
	}

	private Element getWsExportsElement() {
		Element rootElement = getRootElement();

		Element wsExportsElement = rootElement.getChild("wsExports");
		if (wsExportsElement == null) {
			// Create new element
			wsExportsElement = new Element("wsExports");
			List<Element> children = rootElement.getChildren();
			ListIterator<Element> childrenIterator = children.listIterator();
			boolean added = false;
			while (childrenIterator.hasNext()) {
				if (childrenIterator.next().getName().equals("webProject")) {
					children.add(childrenIterator.previousIndex(), wsExportsElement);
					added = true;
					break;
				}
			}
			if (!added) {
				children.add(wsExportsElement);
			}
		}
		return wsExportsElement;
	}

	private Element getExportHandlerElement(Element wsExportElement) {
		Element exportHandlerElement = wsExportElement.getChild("exportHandler");
		if (exportHandlerElement == null) {
			// Create new element
			exportHandlerElement = new Element("exportHandler");
			List<Element> children = wsExportElement.getChildren();
			ListIterator<Element> childrenIterator = children.listIterator();
			boolean added = false;
			while (childrenIterator.hasNext()) {
				String childName = childrenIterator.next().getName();
				if (childName.equals("wsDescExtensions") || childName.equals("wsDescBindings")
						|| childName.equals("URLPatternType") || childName.equals("urlpattern")) {
					children.add(childrenIterator.previousIndex(), exportHandlerElement);
					added = true;
					break;
				}
			}
			if (!added) {
				children.add(exportHandlerElement);
			}
		}
		return exportHandlerElement;
	}

	private Element getWsExportElement(Element wsExportsElement, String webServiceExportName) {
		List<Element> wsExportElements = wsExportsElement.getChildren("wsExport");
		for (Element wsExportElement : wsExportElements) {
			if (wsExportElement.getChildText("name").equals(webServiceExportName)) {
				return wsExportElement;
			}
		}
		Element wsExportElement = new Element("wsExport");
		wsExportElement.addContent(new Element("name").setText(webServiceExportName));
		wsExportElements.add(0, wsExportElement);
		return wsExportElement;
	}
}
