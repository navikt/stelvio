package no.nav.maven.plugin.sca;

import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public interface ServiceDeployAssembly {
	Collection<Artifact> getArtifacts(MavenProject project);

	void addArtifacts(MavenProject project, Archiver archiver, Collection<Artifact> artifacts) throws ArchiverException;
}
