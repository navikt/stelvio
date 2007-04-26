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

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.*;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.*;
import org.dom4j.io.*;

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

	private ZipUtils zipUtils = new ZipUtils();

	public void execute() throws MojoExecutionException {

		String unpackDir;

		try {
			File[] files = earDirectory.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getName().startsWith("nav-cons")) {
					unpackDir = TEMP_OUTPUT + file.separator + "ear" + file.separator + file.getName();
					final File destination = new File(unpackDir);
					destination.mkdirs();
					zipUtils.extract(file, destination);
					getLog().info("\tdone unpacking ear files");
					extraxtEJBJarFiles(unpackDir);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// addHandler();

	}

	/*
	 * This method extracts the EJB jar files, and adds the handler element to
	 * webservices.xml
	 */
	private void extraxtEJBJarFiles(String unpackDir) throws MojoExecutionException, IOException {
		File dir = new File(unpackDir);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().startsWith("nav-cons") && file.getName().endsWith("EJB.jar")) {
				final String outputDir = TEMP_OUTPUT + file.separator + "jar" + file.separator + file.getName();
				final File jarDir = new File(outputDir);
				zipUtils.extract(file, jarDir);
				File settings = new File(outputDir + file.separator + "META-INF" + file.separator + "webservices.xml");
				addHandler(settings);
				zipUtils.compress(jarDir, new File(TEMP_OUTPUT+file.separator+"test.jar"));
			}

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
				writer = new XMLWriter(new FileWriter(inputFile), format);
				writer.write(doc);
				writer.close();
			}
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing inputfile", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing outputfile", e);
		}
	}

}
