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

import no.stelvio.esb.models.service.metamodel.ComplexType;
import no.stelvio.esb.models.service.metamodel.ServiceMetamodelFactory;
import no.stelvio.esb.models.service.metamodel.ServicePackage;
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

public class WebPublishComplexTypes {
	private static Logger logger = Logger.getLogger("no.stelvio.esb.models.transformation.servicemodel2html");
	private static final IPath xsltFilePath = new Path(
			"src/main/resources/xslt/web-publish-complexTypes.xsl");

	// Name of the required input parameters for xslt transformation
	private static final String paramOutputDirPath = "outputDirPath";
	private static final String paramCurrentFilePath = "currentFilePath";

	private static final String paramComplexTypeUUID = "complexTypeUUID";
	private static final String paramComplexTypeName = "complexTypeName";
	private static final String paramServicePackageUUID = "servicePackageUUID";
	private static final String paramServicePackageName = "servicePackageName";

	protected static void run(File inputFile, File outputDirectory)
			throws SAXException, IOException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException,
			URISyntaxException {
		List<ServicePackage> servicePackageList = parseAndGetServicePackageList(inputFile);

		for (ServicePackage servicePackage : servicePackageList) {
			for (ComplexType complexType : servicePackage.getComplexTypes()) {
				publishComplexType(inputFile, outputDirectory, complexType,
						servicePackage);
			}
		}
	}

	private static List<ServicePackage> parseAndGetServicePackageList(
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
		NodeIterator nl = XPathAPI.selectNodeIterator(doc, "//childPackages");

		List<ServicePackage> servicePackageListe = new ArrayList<ServicePackage>();
		Node n;
		ServiceMetamodelFactory factory = new ServiceMetamodelFactoryImpl();
		while ((n = nl.nextNode()) != null) {
			NamedNodeMap nnm = n.getAttributes();
			String name = nnm.getNamedItem("name").getNodeValue();
			String uuid = nnm.getNamedItem("UUID").getNodeValue();

			ServicePackage servicePackage = factory.createServicePackage();
			servicePackage.setName(name);
			servicePackage.setUUID(uuid);

			servicePackageListe.add(servicePackage);
		}

		for (ServicePackage servicePackage : servicePackageListe) {
			NodeIterator complexTypesIterator = XPathAPI.selectNodeIterator(
					doc, "//childPackages[@UUID='" + servicePackage.getUUID()
							+ "']/complexTypes");

			Node complexTypeNode;
			List<ComplexType> complexTypeList = new ArrayList<ComplexType>();
			while ((complexTypeNode = complexTypesIterator.nextNode()) != null) {
				NamedNodeMap nnm = complexTypeNode.getAttributes();
				
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
					logger.debug("FEIL i WebPublishComplexTypes.parseAndGetServicePackageList! " + feilmelding + " (" + e.getMessage() + ")");
				}

				ComplexType complexType = factory.createComplexType();
				complexType.setName(name);
				complexType.setUUID(uuid);
				complexType.setNamespace(namespace);

				complexTypeList.add(complexType);
			}
			servicePackage.getComplexTypes().addAll(complexTypeList);
		}

		return servicePackageListe;
	}

	private static void publishComplexType(File inputFile,
			File outputDirectory, ComplexType complexType,
			ServicePackage servicePackage) throws TransformerException,
			IOException, URISyntaxException {

		String complextypeNamespace = complexType.getNamespace();
		String complextypeName = complexType.getName();

		File complextypeFile = PublishFileUtils.createComplextypeFile(
				outputDirectory, complextypeNamespace, complextypeName);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		File xsltFile = PublishFileUtils.getBoundleFileForPath(xsltFilePath);
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));

		// Sending required input parameters to the xsl transformation
		transformer.setParameter(paramComplexTypeUUID, complexType.getUUID());
		transformer.setParameter(paramComplexTypeName, complexType.getName());
		transformer.setParameter(paramServicePackageUUID,
				servicePackage.getUUID());
		transformer.setParameter(paramServicePackageName,
				servicePackage.getName());

		transformer.setParameter(paramOutputDirPath,
				outputDirectory.getAbsolutePath());
		transformer.setParameter(paramCurrentFilePath,
				complextypeFile.getAbsolutePath());

		transformer.transform(new StreamSource(inputFile), new StreamResult(
				new FileOutputStream(complextypeFile)));

		logger.debug("Creating new service operation file: " + complextypeFile.getAbsolutePath());
	}
}
