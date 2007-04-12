package org.apache.maven.plugin.war;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.maven.common.ProjectUtil;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.archiver.war.WarArchiver;

/**
 * Build a war/webapp.
 *
 * @author <a href="test@example.com">Emmanuel Venisse</a>
 * @version $Id: WarMojo.java 480784 2006-11-30 00:07:45Z jvanzyl $
 * @goal war
 * @phase package
 * @requiresDependencyResolution runtime
 */
public class WarMojo extends AbstractWarMojo {
	/**
     * A list of custom classpath entries
     * 
     * @parameter
     */
    private List<String> customClasspathEntries;
    
	/**
	 * List of artifacts to include in WEB-INF/lib
	 * @parameter
	 */
	private List<ArtifactItem> libraryArtifactItems;
	
    /**
     * The directory for the generated WAR.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The name of the generated WAR.
     *
     * @parameter expression="${project.build.finalName}"
     * @required
     */
    private String warName;

    /**
     * Classifier to add to the artifact generated. If given, the artifact will be an attachment instead.
     *
     * @parameter
     */
    private String classifier;

    /**
     * The Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#war}"
     * @required
     */
    private WarArchiver warArchiver;

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    /**
     * Whether this is the main artifact being built. Set to <code>false</code> if you don't want to install or
     * deploy it to the local repository instead of the default one in an execution.
     *
     * @parameter expression="${primaryArtifact}" default-value="true"
     */
    private boolean primaryArtifact;

    // ----------------------------------------------------------------------
    // Implementation
    // ----------------------------------------------------------------------
    private String[] getLibraryIncludes() {
        List<String> includes = new ArrayList<String>();
        
        for (ArtifactItem item : libraryArtifactItems) {
            Artifact artifact = ProjectUtil.getArtifact(getProject(), item.getGroupId(), item.getArtifactId()); 
            if(artifact != null) {
            	includes.add("WEB-INF/lib/"+item.getArtifactId()+"*.jar");
            	getLog().info("Adding artifact "+artifact.getGroupId()+":"+artifact.getArtifactId()+":"+artifact.getVersion()+" to WEB-INF/lib");
            }
        }
        
        return (String[]) includes.toArray(new String[0]);
    }
    
    /**
     * Overload this to produce a test-war, for example.
     */
    protected String getClassifier()
    {
        return classifier;
    }

    protected static File getWarFile( File basedir, String finalName, String classifier )
    {
        if ( classifier == null )
        {
            classifier = "";
        }
        else if ( classifier.trim().length() > 0 && !classifier.startsWith( "-" ) )
        {
            classifier = "-" + classifier;
        }

        return new File( basedir, finalName + classifier + ".war" );
    }

    /**
     * Executes the WarMojo on the current project.
     *
     * @throws MojoExecutionException if an error occured while building the webapp
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
    	getLog().info("TEST ARTIFACT: "+ProjectUtil.getArtifact(getProject(), "jfree", "jfreechart"));
    	
        File warFile = getWarFile( new File( outputDirectory ), warName, classifier );

        try
        {
            performPackaging( warFile );
        }
        catch ( DependencyResolutionRequiredException e )
        {
            throw new MojoExecutionException( "Error assembling WAR: " + e.getMessage(), e );
        }
        catch ( ManifestException e )
        {
            throw new MojoExecutionException( "Error assembling WAR", e );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Error assembling WAR", e );
        }
        catch ( ArchiverException e )
        {
            throw new MojoExecutionException( "Error assembling WAR: " + e.getMessage(), e );
        }
    }

    /**
     * Generates the webapp according to the <tt>mode</tt> attribute.
     *
     * @param warFile the target war file
     * @throws IOException
     * @throws ArchiverException
     * @throws ManifestException
     * @throws DependencyResolutionRequiredException
     *
     */
    private void performPackaging( File warFile )
        throws IOException, ArchiverException, ManifestException, DependencyResolutionRequiredException,
        MojoExecutionException, MojoFailureException
    {
        buildExplodedWebapp( getWebappDirectory() );

        //generate war file
        getLog().info( "Generating war " + warFile.getAbsolutePath() );

        NavMavenArchiver archiver = new NavMavenArchiver();

        archiver.setArchiver( warArchiver );

        archiver.setOutputFile( warFile );

        // Add libraries to include before excluding others
        if(libraryArtifactItems != null)
            warArchiver.addDirectory(getWebappDirectory(), getLibraryIncludes(), null);
        
        warArchiver.addDirectory( getWebappDirectory(), getIncludes(), getExcludes() );

        warArchiver.setWebxml( new File( getWebappDirectory(), "WEB-INF/web.xml" ) );

        // create archive
        archiver.createArchive( getProject(), archive, getCustomClasspathEntries(), getClasspathExcludes() );

        String classifier = this.classifier;
        if ( classifier != null )
        {
            projectHelper.attachArtifact( getProject(), "war", classifier, warFile );
        }
        else
        {
            Artifact artifact = getProject().getArtifact();
            if ( primaryArtifact )
            {
                artifact.setFile( warFile );
            }
            else if ( artifact.getFile() == null || artifact.getFile().isDirectory() )
            {
                artifact.setFile( warFile );
            }
        }
    }

	/**
	 * @return the customClasspathEntries
	 */
	public List<String> getCustomClasspathEntries() {
		return customClasspathEntries;
	}

	/**
	 * @param customClasspathEntries the customClasspathEntries to set
	 */
	public void setCustomClasspathEntries(List<String> customClasspathEntries) {
		this.customClasspathEntries = customClasspathEntries;
	}
}
