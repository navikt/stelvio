package no.nav.maven.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.bean.BeanDocumentFactory;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.sql.rowset.spi.XmlWriter;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal touch
 * 
 * @phase process-sources
 */
public class MyMojo extends AbstractMojo {

	private final String TEMP_OUTPUT = "target";
	/**
	 * @parameter
	 * @required
	 */
	private File earDirectory;

	public void execute() throws MojoExecutionException {
		
		String unpackDir;
		
		File[] files = earDirectory.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().startsWith("nav-cons")) {
				unpackDir = TEMP_OUTPUT+file.separator+"ear"+file.separator+file.getName();
				extraxtZipFiles(unpackDir,file);
				getLog().info("\tdone unpacking ear files");
				extraxtEJBJarFiles(unpackDir);
			}
		}
		
		//addHandler();
		
	}
	/*
	 * This method extracts the EJB jar files, and adds the handler element to webservices.xml
	 */
	private void extraxtEJBJarFiles(String unpackDir) throws MojoExecutionException {
		File dir = new File(unpackDir);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().startsWith("nav-cons") && file.getName().endsWith("EJB.jar")) {
				final String outputDir = TEMP_OUTPUT+file.separator+"jar"+file.separator+file.getName();
				extraxtZipFiles(outputDir, file);
				File settings = new File(outputDir+file.separator+"META-INF"+file.separator+"webservices.xml");
				addHandler(settings);
				writeZipFiles(new File(TEMP_OUTPUT+file.separator+"jar"+file.separator+"test.jar"), new File(outputDir));
			}
			
		}
	}

	private void extraxtZipFiles(String unpackDir, File zipFile) throws MojoExecutionException {
		getLog().info("unpacking "+zipFile.getName());
		try {
			ZipInputStream zipStream = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry zipEntry= null;
			while ((zipEntry = zipStream.getNextEntry()) != null) {
				int bytes = 0;
				File unzippedFile = new File(unpackDir, zipEntry.getName());
				getLog().info("\tUnpacking jar entry to "+unzippedFile);
				new File(unzippedFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(unzippedFile);
				byte[] buffer = new byte[10];
				while ((bytes = zipStream.read(buffer, 0, buffer.length)) > 0) {
					for (int i = 0; i < bytes; i++) {
						fos.write((byte) buffer[i]);
					}
				}
				fos.close();
			}
		} catch (IOException e) {
			throw new MojoExecutionException("Error reading earfiles",e);
		}
	}

	private void writeZipFiles(File zipFile, File zipDir) throws MojoExecutionException {
		getLog().info("packing "+zipFile.getName());
		try {
			ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
			String[] files = zipDir.list();
			for (int i = 0; i < files.length; i++) {
				String file = files[i];
				ZipEntry entry = new ZipEntry(file);
				zipStream.putNextEntry(entry);
			}
			zipStream.close();
			
		} catch (IOException e) {
			throw new MojoExecutionException("Error reading earfiles",e);
		}
	}
	
	private void addHandler(File inputFile) throws MojoExecutionException {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(inputFile);
			
			
			Element root = doc.getRootElement();
			Iterator iter = root.elementIterator("webservice-description");
			while (iter.hasNext()) {
				Element elem = (Element) iter.next();
				Element elem2 = elem.element("port-component");
								
				Element handler = elem2.addElement("handler");
				handler.addElement("handler-name").addText("no.stelvio.common.bus.handlers.jaxrpc.StelvioCommonContextHandler");
				handler.addElement("handler-class").addText("no.stelvio.common.bus.handlers.jaxrpc.StelvioCommonContextHandler");

				XMLWriter writer;
				OutputFormat format = OutputFormat.createPrettyPrint();
				writer = new XMLWriter( new FileWriter(inputFile), format);
				writer.write(doc);
				writer.close();
			}
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile",e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile",e);
		}
	}
	
}
