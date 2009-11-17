package no.nav.maven.plugin.sca;

import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public interface ServiceDeployAssembly {
	Collection<Artifact> getArtifacts(MavenProject project);
	
	void addArtifact(MavenProject project, Archiver archiver, Artifact artifact) throws ArchiverException;
}
