package no.stelvio.maven.plugins;

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

import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;
import no.stelvio.maven.build.plugin.utils.CommandLineUtil;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal which updates views for both DEV and INT streams
 * 
 * @goal updateViews
 * 
 * @author test@example.com
 */
public class UpdateViews extends AbstractMojo {
	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${project}"
	 * @required
	 */
	private String project;

	public void execute() throws MojoExecutionException, MojoFailureException {

		boolean fail = false;
		this.getLog().info("----------------------------------");
		this.getLog().info("--- Updating views for streams ---");
		this.getLog().info("----------------------------------");
		this.getLog().info("Updating INT view for " + this.project + "\n");
		String workingDir = "D:/cc/";
		String subcommand = "update -force -overwrite D:/cc/" + this.project;
		fail = CleartoolCommandLine.runClearToolCommand(workingDir, subcommand+"_int") != 0;
		if (fail) throw new MojoExecutionException("Unable to update INT view");
		this.getLog().info("************************************");
		this.getLog().info("Updating DEV view for " + this.project + "\n");
		fail = CleartoolCommandLine.runClearToolCommand(workingDir, subcommand+"_Dev") != 0;
		if (fail) throw new MojoExecutionException("Unable to update DEV view");
		
	}
}
