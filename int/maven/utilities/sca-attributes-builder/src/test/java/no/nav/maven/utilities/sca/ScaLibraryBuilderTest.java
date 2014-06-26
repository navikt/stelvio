package no.nav.maven.utilities.sca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

public class ScaLibraryBuilderTest {
	
	@Test
	public void testWriteTo() throws IOException {
		ScaLibraryBuilder builder = new ScaLibraryBuilder("nav-lib-mytest");
		File file = new File("sca.library");
		builder.writeTo(new FileWriter(file));
		assertTrue(file.exists());
		assertEquals(file.getName(),"sca.library");
		
		file.delete();
	}
	
	@Test
	public void testScaLibraryContent() throws IOException, JDOMException {
		final String libname = "nav-lib-mytest"; 
		ScaLibraryBuilder builder = new ScaLibraryBuilder(libname);
		File file = new File("sca.library");
		builder.writeTo(new FileWriter(file));
		
		testScaLibraryContent(libname,file);
		
		file.delete();
	}
	
	@Test
	public void testWriteToDirectory() throws IOException, JDOMException {
		String libname = "nav-this-is-it";
		ScaLibraryBuilder builder = new ScaLibraryBuilder(libname);
		File dir = new File("libDir");
		assertTrue("could not create dir",dir.mkdir());
		builder.writeToDirectory(dir);
		File libFile = new File(dir, "sca.library");
		assertTrue(libFile.exists());
		
		testScaLibraryContent(libname,libFile);
		assertTrue("could not delete file", libFile.delete());
		assertTrue("coulde not delete dir",dir.delete());
		
	}
	
	private void testScaLibraryContent(String libname, File libraryFile) throws JDOMException, IOException {
		SAXBuilder xmlBuilder = new SAXBuilder();
		Document libXml = xmlBuilder.build(libraryFile);
		
		Element root = libXml.getRootElement();
		assertEquals("library", root.getName());
		assertEquals("shareByValue", root.getAttribute("libraryType").getValue());
		assertEquals(libname, root.getAttribute("name").getValue());
	}

}

