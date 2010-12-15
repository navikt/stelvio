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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which updates views for both DEV and INT streams
 *
 * @goal updateViews
 * 
 * @author test@example.com
 */
public class UpdateViews extends AbstractMojo
{
	/**
	 * Stream name - BUILD_TEST
	 * @parameter expression="${stream}"
	 * @required
	 */
	private String stream;
	
    public void execute() throws MojoExecutionException
    {
    	this.getLog().info("----------------------------------");
    	this.getLog().info("--- Updating views for streams ---");
    	this.getLog().info("----------------------------------");
        this.getLog().info("Updating views for " + this.stream);
    }
}
