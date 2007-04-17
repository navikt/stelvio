package org.apache.maven.plugin.ejb;

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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import no.nav.maven.common.ProjectUtil;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Build an EJB (and optional client) from the current project.
 *
 * @author <a href="test@example.com">Emmanuel Venisse</a>
 * @version $Id: EjbMojo.java 500542 2007-01-27 15:14:57Z snicoll $
 * @goal ejb
 * @phase package
 */
public class EjbMojo extends AbstractMojo {
	/**
     * A list of custom classpath entries in the IDE manifest.
     * 
     * @parameter
     */
    private List<String> customIdeClasspathEntries;
    
	/**
     * A list of custom classpath entries in the target manifest.
     * 
     * @parameter
     */
    private List<String> customClasspathEntries;
    
    /**
     * The list of artifacts to exclude from IDE manifest.
     *
     * @parameter
     */
    private List<ArtifactItem> ideClasspathExcludes;
    
    /**
     * The list of artifacts to exclude from target manifest.
     *
     * @parameter
     */
    private List<ArtifactItem> classpathExcludes;
    
    // TODO: will null work instead?
    private static final String[] DEFAULT_INCLUDES = new String[]{"**/**"};

    private static final String[] DEFAULT_EXCLUDES =
        new String[]{"**/*Bean.class", "**/*CMP.class", "**/*Session.class", "**/package.html"};

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String EJB_JAR_XML = "META-INF/ejb-jar.xml";
    
    private static final String MANIFEST_FILE = "MANIFEST.MF";

    /**
     * The directory to export the generated manifest file to.
     *
     * @parameter
     */
    private File exportManifestToDir;
    
    /**
     * The directory for the generated EJB.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     * @todo use File instead
     */
    private String basedir;

    /**
     * Directory that resources are copied to during the build.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The name of the EJB file to generate.
     *
     * @parameter expression="${project.build.finalName}"
     * @required
     */
    private String jarName;

    /**
     * Classifier to add to the artifact generated. If given, the artifact will
     * be an attachment instead.
     *
     * @parameter
     */
    private String classifier;

    /**
     * Whether the ejb client jar should be generated or not. Default
     * is false.
     *
     * @parameter
     * @todo boolean instead
     */
    private String generateClient = Boolean.FALSE.toString();

    /**
     * Excludes.
     *
     * <br/>Usage:
     * <pre>
     * &lt;clientIncludes&gt;
     *   &lt;clientInclude&gt;**&#47;*Ejb.class&lt;&#47;clientInclude&gt;
     *   &lt;clientInclude&gt;**&#47;*Bean.class&lt;&#47;clientInclude&gt;
     * &lt;&#47;clientIncludes&gt;
     * </pre>
     * <br/>Attribute is used only if client jar is generated.
     * <br/>Default exclusions: **&#47;*Bean.class, **&#47;*CMP.class, **&#47;*Session.class, **&#47;package.html
     *
     * @parameter
     */
    private List clientExcludes;

    /**
     * Includes.
     *
     * <br/>Usage:
     * <pre>
     * &lt;clientIncludes&gt;
     *   &lt;clientInclude&gt;**&#47;*&lt;&#47;clientInclude&gt;
     * &lt;&#47;clientIncludes&gt;
     * </pre>
     * <br/>Attribute is used only if client jar is generated.
     * <br/>Default value: **&#47;**
     *
     * @parameter
     */
    private List clientIncludes;

    /**
     * @parameter
     */
    private List<Project> clientIncludeProjects;
    
    /**
     * @parameter
     */
    private List<ArtifactItem> clientIncludeDependencyResources;
    
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver jarArchiver;

    /**
     * What EJB version should the ejb-plugin generate? ejbVersion can be "2.x" or "3.x"
     * (where x is a digit), defaulting to "2.1".  When ejbVersion is "3.x", the
     * ejb-jar.xml file is optional.
     * <p/>
     * Usage:
     * <pre>
     * &lt;ejbVersion&gt;3.0&lt;&#47;ejbVersion&gt;
     * </pre>
     *
     * @parameter default-value="2.1"
     * @required
     * @since 2.1
     */
    private String ejbVersion;

    /**
     * The client Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver clientJarArchiver;

    /**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    private MavenProjectHelper projectHelper;

    /**
     * The maven archiver to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    private File unzipJarEntries(Artifact artifact) throws FileNotFoundException, IOException {
    	File unzipTempFolder = new File(System.getProperty("java.io.tmpdir")+project.getArtifactId()+"_"+artifact.getArtifactId()+"_"+System.currentTimeMillis());
    	JarInputStream jis = new JarInputStream(new FileInputStream(artifact.getFile()));
		JarEntry je = null;
		while ((je = jis.getNextJarEntry()) != null) {
			if (je.isDirectory()) {
				continue;
			}
			
			int bytes = 0;
			File unzippedFile = new File(unzipTempFolder, je.getName());
			getLog().info("Unpacking "+je.getName()+" to "+unzippedFile);
			new File(unzippedFile.getParent()).mkdirs();
			FileOutputStream fos = new FileOutputStream(unzippedFile);
			byte[] buffer = new byte[10];
			while ((bytes = jis.read(buffer, 0, buffer.length)) > 0) {
				for (int i = 0; i < bytes; i++) {
					fos.write((byte) buffer[i]);
				}
			}
			fos.close();
		}
		jis.close();
		
		return unzipTempFolder;
    }
    
    /**
     * Generates an ejb jar and optionnaly an ejb-client jar.
     *
     * @todo Add license files in META-INF directory.
     */
    public void execute()
        throws MojoExecutionException
    {    	
        if ( getLog().isInfoEnabled() )
        {
            getLog().info( "Building ejb " + jarName + " with ejbVersion " + ejbVersion );
        }

        File jarFile = getEJBJarFile( basedir, jarName, classifier );

        NavMavenArchiver archiver = new NavMavenArchiver();

        archiver.setArchiver( jarArchiver );

        archiver.setOutputFile( jarFile );

        File deploymentDescriptor = new File( outputDirectory, EJB_JAR_XML );

        /* test EJB version compliance */
        if ( !ejbVersion.matches( "\\A[2-3]\\.[0-9]\\z" ) )
        {
            throw new MojoExecutionException(
                "ejbVersion is not valid: " + ejbVersion + ". Must be 2.x or 3.x (where x is a digit)" );
        }

        if ( ejbVersion.matches( "\\A2\\.[0-9]\\z" ) && !deploymentDescriptor.exists() )
        {
            throw new MojoExecutionException(
                "Error assembling EJB: " + EJB_JAR_XML + " is required for ejbVersion 2.x" );
        }

        try
        {
            archiver.getArchiver().addDirectory( new File( outputDirectory ), DEFAULT_INCLUDES,
                                                 new String[]{EJB_JAR_XML, "**/package.html"} );

            if ( deploymentDescriptor.exists() )
            {
                archiver.getArchiver().addFile( deploymentDescriptor, EJB_JAR_XML );
            }

            // create archive
            archiver.createArchive(project, archive, getCustomClasspathEntries(), getClasspathExcludes());
        }
        catch ( ArchiverException e )
        {
            throw new MojoExecutionException( "There was a problem creating the EJB archive: " + e.getMessage(), e );
        }
        catch ( ManifestException e )
        {
            throw new MojoExecutionException( "There was a problem creating the EJB archive: " + e.getMessage(), e );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "There was a problem creating the EJB archive: " + e.getMessage(), e );
        }
        catch ( DependencyResolutionRequiredException e )
        {
            throw new MojoExecutionException( "There was a problem creating the EJB archive: " + e.getMessage(), e );
        }
        
        // Export the manifest file if specified
        if (exportManifestToDir != null) {
        	if (!exportManifestToDir.exists()) {
        		exportManifestToDir.mkdirs();
        	}
        	
        	PrintWriter manifestWriter = null;
        	try {				
				Manifest manifest = archiver.getManifest(project, archive.getManifest(), Collections.EMPTY_MAP, getIdeClasspathExcludes(), getCustomIdeClasspathEntries());
				manifestWriter = new PrintWriter(new File(exportManifestToDir, MANIFEST_FILE));
				manifest.write(manifestWriter);
				getLog().info("Manifest file exported to "+exportManifestToDir+File.separator+MANIFEST_FILE);
				
			} catch (IOException e) {
				throw new MojoExecutionException("Could not copy manifest file to "+exportManifestToDir, e);
			} catch (DependencyResolutionRequiredException e) {
				throw new MojoExecutionException("Could not copy manifest file to "+exportManifestToDir, e);
			} catch (ManifestException e) {
				throw new MojoExecutionException("Could not copy manifest file to "+exportManifestToDir, e);
			} finally {
				manifestWriter.close();
			}
        }
        
        // Handle the classifier if necessary
        if ( classifier != null )
        {
            projectHelper.attachArtifact( project, "ejb", classifier, jarFile );
        }
        else
        {
            project.getArtifact().setFile( jarFile );
        }

        if ( new Boolean( generateClient ).booleanValue() )
        {
            getLog().info( "Building ejb client " + jarName + "-client" );

            String[] excludes = DEFAULT_EXCLUDES;
            String[] includes = DEFAULT_INCLUDES;

            if ( clientIncludes != null && !clientIncludes.isEmpty() )
            {
                includes = (String[]) clientIncludes.toArray( EMPTY_STRING_ARRAY );
            }

            if ( clientExcludes != null && !clientExcludes.isEmpty() )
            {
                excludes = (String[]) clientExcludes.toArray( EMPTY_STRING_ARRAY );
            }

            File clientJarFile = new File( basedir, jarName + "-client.jar" );

            MavenArchiver clientArchiver = new MavenArchiver();

            clientArchiver.setArchiver( clientJarArchiver );

            clientArchiver.setOutputFile( clientJarFile );

            try
            {
                clientArchiver.getArchiver().addDirectory( new File( outputDirectory ), includes, excludes );

                // Add resources from other projects
                if (clientIncludeProjects != null) {
                	for (Project clientProject : clientIncludeProjects) {
                		getLog().info("Adding client resources from project \""+clientProject.getProjectName()+"\"");
                		clientArchiver.getArchiver().addDirectory(clientProject.getBasedir(), (String[]) clientProject.getIncludes().toArray(EMPTY_STRING_ARRAY), (String[]) Collections.EMPTY_LIST.toArray(EMPTY_STRING_ARRAY));
                	}
                }
                
                // Add resources from project dependency
                if (clientIncludeDependencyResources != null) {
                	for (ArtifactItem artifactItem : clientIncludeDependencyResources) {
                		Artifact artifact = ProjectUtil.getArtifact(project, artifactItem.getGroupId(), artifactItem.getArtifactId());
                		if (artifact != null) {
                			File unzipTempFolder = unzipJarEntries(artifact);
                			
                			clientArchiver.getArchiver().addDirectory(unzipTempFolder, (String[]) artifactItem.getIncludes().toArray(EMPTY_STRING_ARRAY), (String[]) Collections.EMPTY_LIST.toArray(EMPTY_STRING_ARRAY));
                			
                			getLog().debug("Deleting temporary folder "+unzipTempFolder);
                			FileUtils.deleteDirectory(unzipTempFolder);
                			continue;
                		}
                		
                		getLog().warn("Artifact "+artifact+" is not attached to this project. Dependency-resources will not be added to client.");
                	}
                }
                
                // create archive
                clientArchiver.createArchive( project, archive );

            }
            catch ( ArchiverException e )
            {
                throw new MojoExecutionException(
                    "There was a problem creating the EJB client archive: " + e.getMessage(), e );
            }
            catch ( ManifestException e )
            {
                throw new MojoExecutionException(
                    "There was a problem creating the EJB client archive: " + e.getMessage(), e );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException(
                    "There was a problem creating the EJB client archive: " + e.getMessage(), e );
            }
            catch ( DependencyResolutionRequiredException e )
            {
                throw new MojoExecutionException(
                    "There was a problem creating the EJB client archive: " + e.getMessage(), e );
            }

            // TODO: shouldn't need classifer
            projectHelper.attachArtifact( project, "ejb-client", "client", clientJarFile );
        }
    }

    /**
     * Returns the EJB Jar file to generate, based on an optional classifier.
     *
     * @param basedir    the output directory
     * @param finalName  the name of the ear file
     * @param classifier an optional classifier
     * @return the EJB file to generate
     */
    private static File getEJBJarFile( String basedir, String finalName, String classifier )
    {
        if ( classifier == null )
        {
            classifier = "";
        }
        else if ( classifier.trim().length() > 0 && !classifier.startsWith( "-" ) )
        {
            classifier = "-" + classifier;
        }

        return new File( basedir, finalName + classifier + ".jar" );
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

	/**
	 * @return the classpathExcludes
	 */
	public List<ArtifactItem> getClasspathExcludes() {
		return classpathExcludes;
	}

	/**
	 * @param classpathExcludes the classpathExcludes to set
	 */
	public void setClasspathExcludes(List<ArtifactItem> classpathExcludes) {
		this.classpathExcludes = classpathExcludes;
	}

	/**
	 * @return the customIdeClasspathEntries
	 */
	public List<String> getCustomIdeClasspathEntries() {
		return customIdeClasspathEntries;
	}

	/**
	 * @param customIdeClasspathEntries the customIdeClasspathEntries to set
	 */
	public void setCustomIdeClasspathEntries(List<String> customIDEClasspathEntries) {
		this.customIdeClasspathEntries = customIDEClasspathEntries;
	}

	/**
	 * @return the ideClasspathExcludes
	 */
	public List<ArtifactItem> getIdeClasspathExcludes() {
		return ideClasspathExcludes;
	}

	/**
	 * @param ideClasspathExcludes the ideClasspathExcludes to set
	 */
	public void setIdeClasspathExcludes(List<ArtifactItem> ideClasspathExcludes) {
		this.ideClasspathExcludes = ideClasspathExcludes;
	}
}