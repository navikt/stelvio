package no.nav.maven.plugins;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class NamespaceToPackageMapGeneratorTest {

	// @Test
	public void testPatternMatch() {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
				+ "targetNamespace=\"http://poc-lib1/no/nav/gbo\">";
		System.out.println(str + "\n\nResults:");
		Pattern p = Pattern.compile("\"http://([^\"]+)\"");
		Matcher m = p.matcher(str);
		while (m.find()) {
			String ns = m.group(1);
			String[] parts = ns.split("/");
			for (String part : parts) {
				System.out.println(part + ",");
			}
		}
	}

	@Test
	public void testCreateNameSpaceToPackageMapFromWSDLDirectory() throws Exception {
		URL wsdlDirectoryURL = this.getClass().getClassLoader().getResource("wsdl");
		File wsdlDirectory = new File(new URI(wsdlDirectoryURL.toString()));
		Map<String, String> ret = new NamespaceToPackageMapGenerator()
				.createNameSpaceToPackageMapFromWSDLDirectory(wsdlDirectory);
		assertEquals("no.nav.gbo", ret.get("http\\://poc-lib2/no/nav/gbo"));
	}
}
