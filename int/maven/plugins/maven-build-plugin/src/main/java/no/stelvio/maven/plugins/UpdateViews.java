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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	/**
	 * Development stream tag
	 * 
	 * @parameter expression="${devStream}" default-value="_Dev"
	 */
	private String devStream;
	
	/**
	 * Integration stream tag
	 * 
	 * @parameter expression="${intStream}" default-value="_int"
	 */
	private String intStream;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_updateViews}" default-value=true
	 */
	private boolean perform;
	
	/**
	 * Which stream to update. Can be "dev" or "int"
	 * @parameter expression="${stream_to_update}"
	 * @required
	 */
	private String stream_to_update;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!perform) {
			this.getLog().warn("Skipping update view");
			return;
		}
		
		boolean fail = false;
		String workingDir = this.ccProjectDir;
		String subcommand = "update -force -overwrite " + this.ccProjectDir + this.build;
		if (this.stream_to_update.equalsIgnoreCase("int")){
			this.getLog().info("Updating INT view for " + this.build + "\n");
			fail = this.updateView(workingDir, subcommand+this.intStream) != 0;
			if (fail) throw new MojoExecutionException("Unable to update INT view");
		}else if (this.stream_to_update.equalsIgnoreCase("dev")){
			this.getLog().info("Updating DEV view for " + this.build + "\n");
			fail = this.updateView(workingDir, subcommand+this.devStream) != 0;
			if (fail) throw new MojoExecutionException("Unable to update DEV view");
		}		
	}
	
	private int updateView(String workingDir, String subcommand) throws MojoFailureException{
		this.getLog().info("-------------------------");
		this.getLog().info("--- Updating "+ this.stream_to_update+" view ---");
		this.getLog().info("-------------------------");
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
	}
	
}
