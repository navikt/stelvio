package no.stelvio.serviceregistry;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

//import no.stelvio.websphere.datapower.ConfigurationManagement;

import org.junit.Test;

import com.datapower.schemas.management.wsdl.XmlMgmt;
import com.datapower.schemas.management.wsdl.XmlMgmt_Service;

public class ServiceRegistryTest {

	@Test
	public void testSerializeServiceRegistry() throws Exception {
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		List<ServiceOperation> operations = new ArrayList<ServiceOperation>();
		operations.add(new ServiceOperation("TestOperation", "http://www.example.org/soapaction"));
		serviceRegistry.addServiceInstance(new QName("http://www.example.org/test", "TestService", "ts"),
				new QName("http://www.example.org/test", "TestBinding"),
				new ServiceInstance("TestEnvironment", "e11apvl036.utv.internsone.local", "9080", "TestModule", "TestExport"),
				operations);

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile("/*/service/serviceVersion[operation/@soapAction='\"http://www.example.org/soapaction\"']/serviceInstance[environment='TestEnvironment']/@endpoint");
		String result = expr.evaluate(serviceRegistry.toDocument());
//		System.out.println(servicRegistry.toXml());
//		System.out.println(result);
		assertEquals("http://e11apvl036.utv.internsone.local:9080/TestModuleWeb/sca/TestExport", result);
	}

	@Test
	public void testDatapowerUpload() throws Exception {
		XmlMgmt xmlMgmt = new XmlMgmt_Service(getClass().getResource("/wsdl/soma/xml-mgmt.wsdl")).getXmlMgmt();
		((BindingProvider) xmlMgmt).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://dp-tst-01.adeo.no:5550/service/mgmt/current");
//		ConfigurationManagement.setFile(xmlMgmt, "secgw-u1", "local:///test.xml", "".getBytes());
	}
}
