package no.nav.maven.plugins.artifactbuilderplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import no.nav.maven.common.ProjectUtil;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @goal build-artifact
 */
public class ArtifactBuilderMojo extends AbstractMojo {
	public static final String DEFAULT_INCLUDES = "**";
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;
	
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
	
	/**
     * The Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver jarArchiver;
    
    /**
     * @parameter
     * @required
     */
    private String artifactType;
    
    /**
     * @parameter
     * @required
     */
    private String artifactClassifier;
    
    /**
     * The resulting jar file.
     * 
     * @parameter
     * @required
     */
    private File outputFile;
    
    /**
     * List of projects to include in the artifact.
     * 
     * @parameter
     */
    private List<Source> sources;
    
    /**
     * List of dependencies to include in the artifact.
     * 
     * @parameter
     */
    private List<ArtifactItem> dependencies;
    
    /**
     * List of expressions that will be included in sources and dependencies.
     * 
     * @parameter
     */
    private List<String> globalIncludes;
    
    /**
     * List of expressions that will be excluded in sources and dependencies.
     * 
     * @parameter
     */
    private List<String> globalExcludes;

    /**
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
	public void execute() throws MojoExecutionException, MojoFailureException {
		init();
		
		try {
			MavenArchiver archiver = new MavenArchiver();
			archiver.setArchiver(jarArchiver);
			archiver.setOutputFile(outputFile);
			
			// Add sources to artifact 
            if (sources != null) {
            	for (Source source : sources) {
            		getLog().info("Adding resources from project \""+source.getName()+"\"");
            		
            		if (!source.getBasedir().exists()) {
            			getLog().warn("Source folder "+source.getBasedir()+" does not exsist. No resources from "+source.getName()+" will be added to created artifact.");
            		}
            		else {
            			source.getIncludes().addAll(globalIncludes);
            			source.getExcludes().addAll(globalExcludes);
            			source.getExcludes().addAll(Arrays.asList(DirectoryScanner.DEFAULTEXCLUDES));
            			archiver.getArchiver().addDirectory(source.getBasedir(), (String[]) source.getIncludes().toArray(EMPTY_STRING_ARRAY), (String[]) source.getExcludes().toArray(EMPTY_STRING_ARRAY));
            		}
            	}
            }
            else {
            	getLog().warn("No source folders specified!");
            }
            
            // Add dependencies to artifact
            List<File> unzipTempFolders = new ArrayList<File>();
            if (dependencies != null) {
            	for (ArtifactItem artifactItem : dependencies) {
            		Artifact artifact = ProjectUtil.getArtifact(project, artifactItem.getGroupId(), artifactItem.getArtifactId());
            		if (artifact != null) {
            			File unzipTempFolder = unpackJarEntries(artifact);
            			unzipTempFolders.add(unzipTempFolder);
            			
            			getLog().info("Adding resources from artifact \""+artifact.getFile().getName()+"\"");
            			artifactItem.getIncludes().addAll(globalIncludes);
            			artifactItem.getExcludes().addAll(globalExcludes);
            			archiver.getArchiver().addDirectory(unzipTempFolder, (String[]) artifactItem.getIncludes().toArray(EMPTY_STRING_ARRAY), (String[]) artifactItem.getExcludes().toArray(EMPTY_STRING_ARRAY));
            			
            			continue;
            		}
            		
            		getLog().warn("Artifact "+artifact+" is not attached to this project. Dependency-resources from this artifact will not be added to created artifact.");
            	}
            }
			
			archiver.createArchive(project, archive);
			
			// Delete all temporary folders
			for (File unzipTempFolder : unzipTempFolders) {
				FileUtils.deleteDirectory(unzipTempFolder);
			}
			
			// Attach resulting jar-file to project in order for it to be installed and deployed
			boolean alreadyAttached = false;

			for(Artifact a : (List<Artifact>)project.getAttachedArtifacts()) {
				if(a.getFile().getName().equals(archiver.getArchiver().getDestFile().getName())) {
					alreadyAttached = true;
				}
			}

			if(!alreadyAttached) {
				getLog().info("Artifact attached to project as \""+artifactType+"\" with classifier \""+artifactClassifier+"\"");
				projectHelper.attachArtifact(project, artifactType, artifactClassifier, archiver.getArchiver().getDestFile());
			}
			else {
				getLog().info("Artifact " + artifactType+"\" with classifier \"" + artifactClassifier +  " \"already attached, skipping" );
			}

		}
		catch (ArchiverException e) {
			throw new MojoExecutionException("An error occured creating the artifact.", e);
		}
		catch (ManifestException e) {
			throw new MojoExecutionException("An error occured creating the artifact.", e);
		}
		catch (IOException e) {
			throw new MojoExecutionException("An error occured creating the artifact.", e);
		}
		catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException("An error occured creating the artifact.", e);
		}
	}
	
	/**
	 * Initialize variables.
	 */
	private void init() {
		if (globalIncludes == null) {
			globalIncludes = new ArrayList<String>();
		}
		
		if (globalExcludes == null) {
			globalExcludes = new ArrayList<String>();
		}
	}
	
	/**
	 * Unpack the jar file attached to an artifact to a temporary folder. 
	 * 
	 * @param artifact
	 * @return the temporary folder the files where unpacked to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private File unpackJarEntries(Artifact artifact) throws FileNotFoundException, IOException {
    	File unzipTempFolder = new File(System.getProperty("java.io.tmpdir")+System.getProperty("file.separator")+project.getArtifactId()+"_"+artifact.getArtifactId()+"_"+System.currentTimeMillis());
    	JarInputStream jis = new JarInputStream(new FileInputStream(artifact.getFile()));
		JarEntry je = null;
		while ((je = jis.getNextJarEntry()) != null) {
			if (je.isDirectory()) {
				continue;
			}
			
			int bytes = 0;
			File unzippedFile = new File(unzipTempFolder, je.getName());
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
}