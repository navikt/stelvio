package no.nav.maven.plugin.sca;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public class JarServiceDeployAssembly implements ServiceDeployAssembly {
	private static final String JAR_CLASSIFIER = "jar";

	@SuppressWarnings("unchecked")
	public Collection<Artifact> getArtifacts(MavenProject project) {
		Collection<Artifact> artifacts = new ArrayList<Artifact>();

		Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
		for (Artifact attachedArtifact : attachedArtifacts) {
			if (JAR_CLASSIFIER.equals(attachedArtifact.getClassifier())) {
				artifacts.add(attachedArtifact);
				break;
			}
		}

		Collection<Artifact> runtimeArtifacts = project.getRuntimeArtifacts();
		artifacts.addAll(runtimeArtifacts);

		return artifacts;
	}

	public void addArtifact(Archiver archiver, Artifact artifact) throws ArchiverException {
		archiver.addFile(artifact.getFile(), artifact.getArtifactId() + "." + artifact.getArtifactHandler().getExtension());
	}
}
