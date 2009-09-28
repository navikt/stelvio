package no.nav.maven.plugin.wid.utils;

import org.apache.maven.plugin.ide.IdeDependency;

public class IdeDependencyFactory {
	public static IdeDependency copy(IdeDependency dependency) {
		return new IdeDependency(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency
				.getClassifier(), dependency.isReferencedProject(), dependency.isTestDependency(), dependency.isSystemScoped(),
				dependency.isProvided(), dependency.isAddedToClasspath(), dependency.getFile(), dependency.getType(),
				dependency.isOsgiBundle(), null, 0, dependency.getEclipseProjectName());
	}

}
