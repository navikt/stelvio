/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package no.nav.maven.plugin.wid.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.EclipseSourceDir;
import org.apache.maven.plugin.eclipse.Messages;
import org.apache.maven.plugin.eclipse.writers.wtp.AbstractWtpResourceWriter;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.XMLWriter;

/**
 * Creates a org.eclipse.wst.common.component file that WID accepts.
 * 
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 */
public class WidWtpComponentWriter extends AbstractWtpResourceWriter {
	/**
	 * Context root attribute.
	 */
	public static final String ATTR_CONTEXT_ROOT = "context-root"; //$NON-NLS-1$

	/**
	 * The .settings folder for Web Tools Project 1.x release.
	 */
	public static final String DIR_WTP_SETTINGS = ".settings"; //$NON-NLS-1$

	/**
	 * @see org.apache.maven.plugin.eclipse.writers.EclipseWriter#write()
	 */
	public void write() throws MojoExecutionException {
		// create a .settings directory (if not existing)
		File settingsDir = new File(config.getEclipseProjectDirectory(), DIR_WTP_SETTINGS);
		settingsDir.mkdirs();

		Writer w;
		try {
			w = new OutputStreamWriter(new FileOutputStream(new File(settingsDir, "org.eclipse.wst.common.component")), "UTF-8");
		} catch (IOException ex) {
			throw new MojoExecutionException(Messages.getString("EclipsePlugin.erroropeningfile"), ex); //$NON-NLS-1$
		}

		// create a .component file and write out to it
		XMLWriter writer = new PrettyPrintXMLWriter(w);

		writeModuleTypeComponent(writer);

		IOUtil.close(w);
	}

	private void writeModuleTypeComponent(XMLWriter writer) throws MojoExecutionException {
		writer.startElement(ELT_PROJECT_MODULES);
		writer.addAttribute(ATTR_MODULE_ID, "moduleCoreId"); //$NON-NLS-1$
		writer.addAttribute(ATTR_PROJECT_VERSION, "1.5.0");
		writer.startElement(ELT_WB_MODULE);

		// we should use the eclipse project name as the deploy name.
		writer.addAttribute(ATTR_DEPLOY_NAME, config.getEclipseProjectName());

		for (EclipseSourceDir dir : config.getSourceDirs()) {
			// <wb-resource deploy-path="/" source-path="/gen/src" />
			writer.startElement(ELT_WB_RESOURCE);
			writer.addAttribute(ATTR_DEPLOY_PATH, "/");
			String path = dir.getPath();
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			writer.addAttribute(ATTR_SOURCE_PATH, path);
			writer.endElement();
		}

		writer.endElement(); // wb-module
		writer.endElement(); // project-modules
	}
}
