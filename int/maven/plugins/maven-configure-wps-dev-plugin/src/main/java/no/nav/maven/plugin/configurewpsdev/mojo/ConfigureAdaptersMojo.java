package no.nav.maven.plugin.configurewpsdev.mojo;

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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import no.nav.maven.plugin.configurewpsdev.utils.Utils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * 
 * @goal configure-adapters
 * 
 * @phase process-sources
 */
public class ConfigureAdaptersMojo extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * @parameter
	 * @required
	 */
	private List<String> adapters;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			download(adapters);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void download(List<String> urls) throws MalformedURLException {
		for (String url : adapters) {
			URL adapterURL = null;
			adapterURL = new URL(url);
			Utils.downloadURL(adapterURL, outputDirectory);
		}
	}
	
	
	
}
