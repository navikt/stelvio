package no.nav.maven.plugins;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class GenerateMappingFileTest {

	//@Test
	public void testPatternMatch(){
		String str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
		"<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"+
			"targetNamespace=\"http://poc-lib1/no/nav/gbo\">";
		System.out.println(str+"\n\nResults:");
		Pattern p=Pattern.compile("\"http://([^\"]+)\"");
		Matcher m=p.matcher(str);
		while (m.find()){
			String ns=m.group(1);
			String[] parts = ns.split("/");
			for (String part:parts){
				System.out.println(part+",");
			}
		}
	}
	@Test
	public void testCreateNameSpaceToPackageMapFromWSDLDirectory() {
		File wsdlDirectory=new File("E:\\wsMaven\\maven\\plugins\\maven-wsdl-export-plugin\\src\\test\\resources\\wsdl\\");
		GenerateJavaMojo mojo=new GenerateJavaMojo();
		Map<String, String> ret = mojo.createNameSpaceToPackageMapFromWSDLDirectory(wsdlDirectory);
		System.out.println(ret.toString());		
	}

}
