package no.stelvio.esb.models.transformation.servicemodel2html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import no.stelvio.esb.models.service.metamodel.ServiceInterface;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelFactory;
import no.stelvio.esb.models.service.metamodel.ServiceOperation;
import no.stelvio.esb.models.service.metamodel.impl.ServiceMetamodelFactoryImpl;
import no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WebPublishOperations {
	
	private static Logger logger = Logger.getLogger("no.stelvio.esb.models.transformation.servicemodel2html");
	private static final IPath xsltFilePath = new Path(
			"src/main/resources/xslt/web-publish-operations.xsl");

	// Name of the required input parameters for xslt transformation
	private static final String paramOutputDirPath = "outputDirPath";
	private static final String paramCurrentFilePath = "currentFilePath";
	private static final String paramServiceOperationUUID = "serviceOperationUUID";
	private static final String paramServiceOperationName = "serviceOperationName";
	private static final String paramServiceInterfaceUUID = "serviceInterfaceUUID";
	private static final String paramServiceInterfaceName = "serviceInterfaceName";

	protected static void run(File inputFile, File outputDirectory) 
	throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, URISyntaxException {
		List<ServiceInterface> serviceInterfaceList = parseAndGetServiceInterfaceList(inputFile);

		for (ServiceInterface serviceInterface : serviceInterfaceList) {
			for (ServiceOperation serviceOperation : serviceInterface
					.getServiceOperations()) {
				publishOperation(outputDirectory, inputFile, serviceInterface,
						serviceOperation);
			}
		}
	}

	private static List<ServiceInterface> parseAndGetServiceInterfaceList(
			File inputFile) throws SAXException, IOException,
			ParserConfigurationException, TransformerFactoryConfigurationError,
			TransformerException {
		InputSource in = new InputSource(new FileInputStream(inputFile));
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		dfactory.setNamespaceAware(true);
		Document doc = dfactory.newDocumentBuilder().parse(in);

		// Set up an identity transformer to use as serializer.
		Transformer serializer = TransformerFactory.newInstance()
				.newTransformer();
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		// Use the simple XPath API to select a nodeIterator.
		NodeIterator nl = XPathAPI.selectNodeIterator(doc, "//serviceInterface");

		List<ServiceInterface> serviceInterfaceList = new ArrayList<ServiceInterface>();
		Node n;
		ServiceMetamodelFactory factory = new ServiceMetamodelFactoryImpl();
		while ((n = nl.nextNode()) != null) {
			NamedNodeMap nnm = n.getAttributes();
			String name = nnm.getNamedItem("name").getNodeValue();
			String uuid = nnm.getNamedItem("UUID").getNodeValue();
			String namespace = nnm.getNamedItem("namespace").getNodeValue();

			ServiceInterface serviceInterface = factory.createServiceInterface();
			serviceInterface.setName(name);
			serviceInterface.setUUID(uuid);
			serviceInterface.setNamespace(namespace);

			serviceInterfaceList.add(serviceInterface);
		}

		for (ServiceInterface serviceInterface : serviceInterfaceList)
		{
			NodeIterator operationsIterator = XPathAPI.selectNodeIterator(doc,
					"//serviceInterface[@UUID='" + serviceInterface.getUUID() + "']/serviceOperations");

			Node operationNode;
			List<ServiceOperation> serviceOperationList = new ArrayList<ServiceOperation>();
			while ((operationNode = operationsIterator.nextNode()) != null) {
				NamedNodeMap nnm = operationNode.getAttributes();
				String name = null;
				String uuid = null;
				String namespace = null;
				try {
					name = nnm.getNamedItem("name").getNodeValue();
					uuid = nnm.getNamedItem("UUID").getNodeValue();
					namespace = nnm.getNamedItem("namespace").getNodeValue();
				}
				catch (Exception e) {
					String feilmelding = "";
					if (name == null) {
						feilmelding += "Name var null";
					} else {
						feilmelding += "Name: " + name;
					}
					
					if (uuid == null) {
						feilmelding += ", uuid var null";
					} else {
						feilmelding += ", UUID: " + uuid;
					}

					if (namespace == null) {
						feilmelding += ", namespace var null";
					} else {
						feilmelding += ", namespace: " + namespace;
					}
					logger.debug("FEIL i WebPublishOperations.parseAndGetServiceInterfaceList! " + feilmelding + " (" + e.getMessage() + ")");
				}

				ServiceOperation serviceOperation = factory
						.createServiceOperation();
				serviceOperation.setName(name);
				serviceOperation.setUUID(uuid);
				serviceOperation.setNamespace(namespace);

				serviceOperationList.add(serviceOperation);
			}
			serviceInterface.getServiceOperations()
					.addAll(serviceOperationList);
		}

		return serviceInterfaceList;
	}

	private static void publishOperation(File outputDirectory, File inputFile,
			ServiceInterface serviceInterface, ServiceOperation serviceOperation)
			throws TransformerException, IOException, URISyntaxException {
		String interfaceName = serviceInterface.getName();
		String interfaceNamespace = serviceInterface.getNamespace();
		String operationName = serviceOperation.getName();

		File operationFile = PublishFileUtils.createOperationFile(
				outputDirectory, interfaceNamespace, interfaceName,
				operationName);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		File xsltFile = PublishFileUtils.getBoundleFileForPath(xsltFilePath);
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));

		// Sending required input parameters to the xsl transformation
		transformer.setParameter(paramServiceOperationUUID,
				serviceOperation.getUUID());
		transformer.setParameter(paramServiceOperationName,
				serviceOperation.getName());
		transformer.setParameter(paramServiceInterfaceUUID,
				serviceInterface.getUUID());
		transformer.setParameter(paramServiceInterfaceName,
				serviceInterface.getName());

		transformer.setParameter(paramOutputDirPath,
				outputDirectory.getAbsolutePath());
		transformer.setParameter(paramCurrentFilePath,
				operationFile.getAbsolutePath());

		transformer.transform(new StreamSource(inputFile), new StreamResult(
				new FileOutputStream(operationFile)));

		logger.debug("Creating new service operation file: " + operationFile.getAbsolutePath());
	}
}
