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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal configure-adapters
 * 
 * @phase process-sources
 */
public class ConfigureAdaptersMojo extends AbstractWSAdminMojo {

	/**
	 * @parameter expression="${adaptersHome}"
	 */
	protected String adaptersHome;
	
	protected void runWSAdmin(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		Commandline.Argument arg0 = new Commandline.Argument();
		arg0.setLine(scriptsHome+"\\configureAdapters.py");
		commandLine.addArg(arg0);
		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine(scriptsHome);
		commandLine.addArg(arg1);
		Commandline.Argument arg2 = new Commandline.Argument();
		arg2.setLine(adaptersHome);
		commandLine.addArg(arg2);
		executeCommand(commandLine);
		
	}

}
