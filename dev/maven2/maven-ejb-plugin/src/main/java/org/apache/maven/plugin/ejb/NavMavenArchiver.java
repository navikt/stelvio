package org.apache.maven.plugin.ejb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import no.nav.maven.common.ProjectUtil;

import org.apache.maven.archiver.ManifestConfiguration;
import org.apache.maven.archiver.ManifestSection;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class NavMavenArchiver extends MavenArchiver {
	private Log log;
	private JarArchiver archiver;
	private File archiveFile;
	
	public Log getLog() {
        if ( log == null ) {
            log = new SystemStreamLog();
        }

        return log;
    }
	
	private void addManifestAttribute( Manifest manifest, String key, String value ) throws ManifestException {
	    // Use the empty string to suppress a Manifest entry
	    if ( value != null && !"".equals( value ) ) {
	        Manifest.Attribute attr = new Manifest.Attribute( key, value );
	        manifest.addConfiguredAttribute( attr );
	    }
	}
	
	private void addManifestAttribute( Manifest manifest, Map map, String key, String value ) throws ManifestException {
	    if ( map.containsKey( key ) ) {
	        return;  // The map value will be added later
	    }
	    addManifestAttribute( manifest, key, value );
	}
	
	private void addCustomEntries( Manifest m, Map entries, ManifestConfiguration config ) throws ManifestException {
	    addManifestAttribute( m, entries, "Built-By", System.getProperty( "user.name" ) );
	    addManifestAttribute( m, entries, "Build-Jdk", System.getProperty( "java.version" ) );
	
	/* TODO: rethink this, it wasn't working
	    Artifact projectArtifact = project.getArtifact();
	
	    if ( projectArtifact.isSnapshot() )
	    {
	        Manifest.Attribute buildNumberAttr = new Manifest.Attribute( "Build-Number", "" +
	            project.getSnapshotDeploymentBuildNumber() );
	        m.addConfiguredAttribute( buildNumberAttr );
	    }
	
	*/
	    if ( config.getPackageName() != null ) {
	        addManifestAttribute( m, entries, "Package", config.getPackageName() );
	    }
	}
	
	@Override
	protected Manifest getManifest( MavenProject project, ManifestConfiguration config, Map entries ) throws ManifestException, DependencyResolutionRequiredException  {
		return getManifest(project, config, entries, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}
	
	public Manifest getManifest( MavenProject project, ManifestConfiguration config, Map entries, List<ArtifactItem> classpathExcludes, List<String> customClasspathEntries ) throws ManifestException, DependencyResolutionRequiredException {
//		 TODO: Should we replace "map" with a copy? Note, that we modify it!

        // Added basic entries
        Manifest m = new Manifest();
        addManifestAttribute( m, entries, "Created-By", "Apache Maven" );

        addCustomEntries( m, entries, config );

        if ( config.isAddClasspath() ) {
            StringBuffer classpath = new StringBuffer();
            List artifacts = project.getRuntimeClasspathElements();
            String classpathPrefix = config.getClasspathPrefix();

            for ( Iterator iter = artifacts.iterator(); iter.hasNext(); ) {
                File f = new File( (String) iter.next() );
                if ( f.isFile() ) {
                	
                	boolean excluded = false;
                	if (classpathExcludes != null && classpathExcludes.size() > 0) {
	                	for (ArtifactItem artifactItem : classpathExcludes) {
	                		Artifact artifact = ProjectUtil.getArtifact(project, artifactItem.getGroupId(), artifactItem.getArtifactId());
	                		
	                		if (artifact != null && artifact.getFile().getName().equals(f.getName())) {
	                			getLog().info("Excluding "+artifact.getFile().getName()+" from classpath in manifest.");
	                			excluded = true;
	                			break;
	                		}
	                	}
                	}
                	
                	if (!excluded) {
	                	if ( classpath.length() > 0 ) {
	                        classpath.append( " " );
	                    }
	
	                    classpath.append( classpathPrefix );
	                    classpath.append( f.getName() );
	                    
	                    getLog().info("Adding "+f.getName()+" as classpath entry in manifest.");
                	}
                }
            }

            // Add custom classpath entries if specified
            if (customClasspathEntries != null && customClasspathEntries.size() > 0) {
            	StringBuffer customs = new StringBuffer(classpath.length() > 0 ? " " : "");
            	for (String entry : customClasspathEntries) {
            		getLog().info("Adding "+entry+" as custom classpath entry in manifest.");

            		if (customs.length() > 1) {
            			customs.append(" ");
            		}
            		
            		customs.append(entry);
            	}
            	
            	classpath.append(customs.toString());
            }
            
            if ( classpath.length() > 0 )
            {
                // Class-Path is special and should be added to manifest even if
                // it is specified in the manifestEntries section
                addManifestAttribute( m, "Class-Path", classpath.toString() );
            }
        }

        if ( config.isAddDefaultSpecificationEntries() )
        {
            addManifestAttribute( m, entries, "Specification-Title", project.getName() );
            addManifestAttribute( m, entries, "Specification-Version", project.getVersion() );

            if ( project.getOrganization() != null )
            {
                addManifestAttribute( m, entries, "Specification-Vendor", project.getOrganization().getName() );
            }
        }

        if ( config.isAddDefaultImplementationEntries() )
        {
            addManifestAttribute( m, entries, "Implementation-Title", project.getName() );
            addManifestAttribute( m, entries, "Implementation-Version", project.getVersion() );
            // MJAR-5
            addManifestAttribute( m, entries, "Implementation-Vendor-Id", project.getGroupId() );

            if ( project.getOrganization() != null )
            {
                addManifestAttribute( m, entries, "Implementation-Vendor", project.getOrganization().getName() );
            }
        }

        String mainClass = config.getMainClass();
        if ( mainClass != null && !"".equals( mainClass ) )
        {
            addManifestAttribute( m, entries, "Main-Class", mainClass );
        }

        // Added extensions
        if ( config.isAddExtensions() )
        {
            // TODO: this is only for applets - should we distinguish them as a packaging?
            StringBuffer extensionsList = new StringBuffer();
            Set artifacts = project.getArtifacts();

            for ( Iterator iter = artifacts.iterator(); iter.hasNext(); )
            {
                Artifact artifact = (Artifact) iter.next();
                if ( !Artifact.SCOPE_TEST.equals( artifact.getScope() ) )
                {
                    if ( "jar".equals( artifact.getType() ) )
                    {
                        if ( extensionsList.length() > 0 )
                        {
                            extensionsList.append( " " );
                        }
                        extensionsList.append( artifact.getArtifactId() );
                    }
                }
            }

            if ( extensionsList.length() > 0 )
            {
                addManifestAttribute( m, entries, "Extension-List", extensionsList.toString() );
            }

            for ( Iterator iter = artifacts.iterator(); iter.hasNext(); )
            {
                // TODO: the correct solution here would be to have an extension type, and to read
                // the real extension values either from the artifact's manifest or some part of the POM
                Artifact artifact = (Artifact) iter.next();
                if ( "jar".equals( artifact.getType() ) )
                {
                    String ename = artifact.getArtifactId() + "-Extension-Name";
                    addManifestAttribute( m, entries, ename, artifact.getArtifactId() );
                    String iname = artifact.getArtifactId() + "-Implementation-Version";
                    addManifestAttribute( m, entries, iname, artifact.getVersion() );

                    if ( artifact.getRepository() != null )
                    {
                        iname = artifact.getArtifactId() + "-Implementation-URL";
                        String url = artifact.getRepository().getUrl() + "/" + artifact.toString();
                        addManifestAttribute( m, entries, iname, url );
                    }
                }
            }
        }

        return m;
    }

	@Override
	public void createArchive( MavenProject project, MavenArchiveConfiguration archiveConfiguration ) throws ArchiverException, ManifestException, IOException, DependencyResolutionRequiredException {
		this.createArchive(project, archiveConfiguration, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	public void createArchive( MavenProject project, MavenArchiveConfiguration archiveConfiguration, List<String> customClasspathEntries, List<ArtifactItem> classpathExcludes) throws ArchiverException, ManifestException, IOException, DependencyResolutionRequiredException {
//		 we have to clone the project instance so we can write out the pom with the deployment version,
        // without impacting the main project instance...
        MavenProject workingProject = new MavenProject( project );

        File pomPropertiesFile = new File( workingProject.getFile().getParentFile(), "pom.properties" );

        if ( archiveConfiguration.isAddMavenDescriptor() )
        {
            // ----------------------------------------------------------------------
            // We want to add the metadata for the project to the JAR in two forms:
            //
            // The first form is that of the POM itself. Applications that wish to
            // access the POM for an artifact using maven tools they can.
            //
            // The second form is that of a properties file containing the basic
            // top-level POM elements so that applications that wish to access
            // POM information without the use of maven tools can do so.
            // ----------------------------------------------------------------------

            if ( workingProject.getArtifact().isSnapshot() )
            {
                workingProject.setVersion( workingProject.getArtifact().getVersion() );
            }

            String groupId = workingProject.getGroupId();

            String artifactId = workingProject.getArtifactId();

            archiver.addFile( project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId + "/pom.xml" );

            // ----------------------------------------------------------------------
            // Create pom.properties file
            // ----------------------------------------------------------------------

            Properties p = new Properties();

            p.setProperty( "groupId", workingProject.getGroupId() );

            p.setProperty( "artifactId", workingProject.getArtifactId() );

            p.setProperty( "version", workingProject.getVersion() );

            OutputStream os = new FileOutputStream( pomPropertiesFile );

            p.store( os, "Generated by Maven" );

            os.close(); // stream is flushed but not closed by Properties.store()

            archiver.addFile( pomPropertiesFile, "META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties" );
        }

        // ----------------------------------------------------------------------
        // Create the manifest
        // ----------------------------------------------------------------------

        File manifestFile = archiveConfiguration.getManifestFile();

        if ( manifestFile != null )
        {
            archiver.setManifest( manifestFile );
        }

//        Manifest manifest = getManifest( workingProject, archiveConfiguration );
        Manifest manifest = getManifest(workingProject, archiveConfiguration.getManifest(), Collections.EMPTY_MAP, classpathExcludes, customClasspathEntries);

        // any custom manifest sections in the archive configuration manifest?
        if ( !archiveConfiguration.isManifestSectionsEmpty() )
        {
            List sections = archiveConfiguration.getManifestSections();
            for ( Iterator iter = sections.iterator(); iter.hasNext(); )
            {
                ManifestSection section = (ManifestSection) iter.next();
                Manifest.Section theSection = new Manifest.Section();
                theSection.setName( section.getName() );

                if ( !section.isManifestEntriesEmpty() )
                {
                    Map entries = section.getManifestEntries();
                    Set keys = entries.keySet();
                    for ( Iterator it = keys.iterator(); it.hasNext(); )
                    {
                        String key = (String) it.next();
                        String value = (String) entries.get( key );
                        Manifest.Attribute attr = new Manifest.Attribute( key, value );
                        theSection.addConfiguredAttribute( attr );
                    }
                }

                manifest.addConfiguredSection( theSection );
            }
        }

//        if (customClasspathEntries != null && customClasspathEntries.size() > 0) {
//	        Manifest.Attribute classpathAttribue = manifest.getMainSection().getAttribute(Manifest.ATTRIBUTE_CLASSPATH);
//	        for (String entry : customClasspathEntries) {
//	        	getLog().info("Adding "+entry+" as custom classpath entry in manifest.");
//	        	classpathAttribue.setValue(classpathAttribue.getValue()+" "+entry);
//	        }
//        }
        
        // Configure the jar
        archiver.addConfiguredManifest( manifest );

        archiver.setCompress( archiveConfiguration.isCompress() );

        archiver.setIndex( archiveConfiguration.isIndex() );

        archiver.setDestFile( archiveFile );

        // make the archiver index the jars on the classpath, if we are adding that to the manifest
        if ( archiveConfiguration.getManifest().isAddClasspath() ) {
        	List artifacts = project.getRuntimeClasspathElements();
            for ( Iterator iter = artifacts.iterator(); iter.hasNext(); )
            {
                File f = new File( (String) iter.next() );
                archiver.addConfiguredIndexJars( f );
            }
        }

        boolean forced = archiveConfiguration.isForced();
        archiver.setForced( forced );
        if ( !archiveConfiguration.isForced()  &&  archiver.isSupportingForced() )
        {
        	// Should issue a warning here, but how do we get a logger?
        	//getLog().warn( "Forced build is disabled, but disabling the forced mode isn't supported by the archiver." );
        }
        
        // create archive
        archiver.createArchive();

        // Cleanup
        if ( archiveConfiguration.isAddMavenDescriptor() )
        {
            pomPropertiesFile.delete();
        }
	}
	
	@Override
	public void setOutputFile( File outputFile ){
        archiveFile = outputFile;
    }
	
	/**
	 * @return the archiver
	 */
	public JarArchiver getArchiver() {
		return archiver;
	}

	/**
	 * @param archiver the archiver to set
	 */
	public void setArchiver(JarArchiver archiver) {
		this.archiver = archiver;
	}

	/**
	 * @return the archiveFile
	 */
	public File getArchiveFile() {
		return archiveFile;
	}

	/**
	 * @param archiveFile the archiveFile to set
	 */
	public void setArchiveFile(File archiveFile) {
		this.archiveFile = archiveFile;
	}
}