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
package org.apache.maven.plugin.eclipse;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.writers.EclipseWriterConfig;
import org.apache.maven.plugin.eclipse.writers.wid.WidClasspathWriter;

/**
 * Generates the wid configuration files.
 * 
 * @author <a href="mailto:test@example.com">Bjorn Hilstad</a> 
 * @goal wid
 * @execute phase="generate-resources"
 */
public class WidPlugin
    extends EclipsePlugin
{

    /**
     * write all wid configuration files. <br/> <b> NOTE: This could change the config! </b>
     * 
     * @see EclipsePlugin#writeConfiguration()
     * @param deps resolved dependencies to handle
     * @throws MojoExecutionException if the config files could not be written.
     */
    protected void writeConfigurationExtras( EclipseWriterConfig config )
        throws MojoExecutionException
    {
        super.writeConfigurationExtras( config );
    }


    /**
     * WARNING: The manifest resources added here will not have the benefit of the dependencies of the project, since
     * that's not provided in the setup() apis...
     */
    protected void setupExtras()
        throws MojoExecutionException
    {        
        super.setupExtras();
    }
    
    /**
     * Postprocessing extension point
     */
    protected void postProcessing(EclipseWriterConfig config) throws MojoExecutionException
    {
    	getLog().debug("Performing postProcessing for WID plugin");
    	// postprocess .classpath file
    	new WidClasspathWriter().init( getLog(), config ).write();
    	
    }
    
}
