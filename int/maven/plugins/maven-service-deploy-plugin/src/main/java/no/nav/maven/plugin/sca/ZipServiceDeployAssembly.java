package no.nav.maven.plugin.sca;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public class ZipServiceDeployAssembly implements ServiceDeployAssembly {
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

		artifacts.addAll(project.getRuntimeArtifacts());

		return artifacts;
	}

	@SuppressWarnings("unchecked")
	public void addArtifacts(MavenProject project, Archiver archiver, Collection<Artifact> artifacts) throws ArchiverException {
		Collection<ArtifactDecorator> decoratedArtifacts = decorateArtifacts(project, artifacts);

		for (Artifact artifact : decoratedArtifacts) {
			archiver.addFile(artifact.getFile(), artifact.getArtifactId() + "." + artifact.getArtifactHandler().getExtension());
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<ArtifactDecorator> decorateArtifacts(MavenProject project, Collection<Artifact> artifacts)
			throws ArchiverException {
		try {
			Map<String, ArtifactDecorator> artifactMap = new LinkedHashMap<String, ArtifactDecorator>(artifacts.size());
			for (Artifact artifact : artifacts) {
				artifactMap.put(ArtifactUtils.versionlessKey(artifact), new ArtifactDecorator(artifact));
			}

			for (ArtifactDecorator artifact : artifactMap.values()) {
				String artifactKey = ArtifactUtils.versionlessKey(artifact);
				for (String dependencyArtifactId : (List<String>) artifact.getDependencyTrail()) {
					String dependencyArtifactKey = getVersionlessKey(dependencyArtifactId);
					if (!dependencyArtifactKey.equals(artifactKey)) {
						ArtifactDecorator dependencyArtifact = artifactMap.get(dependencyArtifactKey);
						dependencyArtifact.addDependency(artifact);
					}
				}
			}

			File workingDir = createWorkingDir(project);
			for (ArtifactDecorator artifact : artifactMap.values()) {
				File newArtifactFile = createNewArtifactFile(workingDir, artifact);
				artifact.setFile(newArtifactFile);
			}

			return artifactMap.values();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String getVersionlessKey(String artifactId) {
		StringTokenizer st = new StringTokenizer(artifactId, ":");
		return st.nextToken() + ":" + st.nextToken();
	}

	private File createNewArtifactFile(File workingDir, ArtifactDecorator artifact) throws IOException, ArchiverException {
		File newArtifactFile = new File(workingDir, artifact.getArtifactId() + "."
				+ artifact.getArtifactHandler().getExtension());

		JarFile artifactJarFile = new JarFile(artifact.getFile());

		// Build new classpath string for manifest (including also transitive
		// dependencies)
		StringBuilder classpath = new StringBuilder();
		for (ArtifactDecorator dependency : artifact.getAllDependencies()) {
			if (classpath.length() > 0) {
				classpath.append(" ");
			}
			String dependencyArtifactExtension = dependency.getArtifactHandler().getExtension();
			classpath.append(dependency.getArtifactId()).append(".").append(dependencyArtifactExtension);
		}
		// Update Manifest File
		Manifest manifest = artifactJarFile.getManifest();
		manifest.getMainAttributes().putValue(Attributes.Name.CLASS_PATH.toString(), classpath.toString());

		JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(newArtifactFile)), manifest);
		byte[] buf = new byte[1024];
		for (JarEntry jarEntry : Collections.list(artifactJarFile.entries())) {
			if (!"META-INF/MANIFEST.MF".equals(jarEntry.getName())) {
				out.putNextEntry(jarEntry);
				InputStream in = new BufferedInputStream(artifactJarFile.getInputStream(jarEntry));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
		}
		out.close();

		return newArtifactFile;
	}

	private File createWorkingDir(MavenProject project) {
		File parentDir = new File(project.getBuild().getDirectory(), "service-deploy-assembly");
		File workingDir = new File(parentDir, String.valueOf(System.currentTimeMillis()));
		workingDir.mkdirs();
		return workingDir;
	}

	private static class ArtifactDecorator implements Artifact {
		private Artifact artifact;
		private Map<String, ArtifactDecorator> dependencies = new HashMap<String, ArtifactDecorator>();

		public ArtifactDecorator(Artifact artifact) {
			this.artifact = artifact;
		}

		public void addDependency(ArtifactDecorator dependency) {
			dependencies.put(dependency.getArtifactId(), dependency);
		}

		public Collection<ArtifactDecorator> getDependencies() {
			return dependencies.values();
		}

		public Collection<ArtifactDecorator> getAllDependencies() {
			Map<String, ArtifactDecorator> allDependencies = new HashMap<String, ArtifactDecorator>();
			populateAllDependencies(allDependencies);
			return allDependencies.values();
		}

		private void populateAllDependencies(Map<String, ArtifactDecorator> dependenciesMap) {
			dependenciesMap.putAll(dependencies);
			for (ArtifactDecorator dependency : dependencies.values()) {
				dependency.populateAllDependencies(dependenciesMap);
			}
		}

		public void addMetadata(ArtifactMetadata metadata) {
			artifact.addMetadata(metadata);
		}

		@SuppressWarnings("unchecked")
		public int compareTo(Object o) {
			return artifact.compareTo(o);
		}

		public ArtifactHandler getArtifactHandler() {
			return artifact.getArtifactHandler();
		}

		public String getArtifactId() {
			return artifact.getArtifactId();
		}

		public List getAvailableVersions() {
			return artifact.getAvailableVersions();
		}

		public String getBaseVersion() {
			return artifact.getBaseVersion();
		}

		public String getClassifier() {
			return artifact.getClassifier();
		}

		public String getDependencyConflictId() {
			return artifact.getDependencyConflictId();
		}

		public ArtifactFilter getDependencyFilter() {
			return artifact.getDependencyFilter();
		}

		public List getDependencyTrail() {
			return artifact.getDependencyTrail();
		}

		public String getDownloadUrl() {
			return artifact.getDownloadUrl();
		}

		public File getFile() {
			return artifact.getFile();
		}

		public String getGroupId() {
			return artifact.getGroupId();
		}

		public String getId() {
			return artifact.getId();
		}

		public Collection getMetadataList() {
			return artifact.getMetadataList();
		}

		public ArtifactRepository getRepository() {
			return artifact.getRepository();
		}

		public String getScope() {
			return artifact.getScope();
		}

		public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
			return artifact.getSelectedVersion();
		}

		public String getType() {
			return artifact.getType();
		}

		public String getVersion() {
			return artifact.getVersion();
		}

		public VersionRange getVersionRange() {
			return artifact.getVersionRange();
		}

		public boolean hasClassifier() {
			return artifact.hasClassifier();
		}

		public boolean isOptional() {
			return artifact.isOptional();
		}

		public boolean isRelease() {
			return artifact.isRelease();
		}

		public boolean isResolved() {
			return artifact.isResolved();
		}

		public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
			return artifact.isSelectedVersionKnown();
		}

		public boolean isSnapshot() {
			return artifact.isSnapshot();
		}

		public void selectVersion(String version) {
			artifact.selectVersion(version);
		}

		public void setArtifactHandler(ArtifactHandler handler) {
			artifact.setArtifactHandler(handler);
		}

		public void setArtifactId(String artifactId) {
			artifact.setArtifactId(artifactId);
		}

		public void setAvailableVersions(List versions) {
			artifact.setAvailableVersions(versions);
		}

		public void setBaseVersion(String baseVersion) {
			artifact.setBaseVersion(baseVersion);
		}

		public void setDependencyFilter(ArtifactFilter artifactFilter) {
			artifact.setDependencyFilter(artifactFilter);
		}

		public void setDependencyTrail(List dependencyTrail) {
			artifact.setDependencyTrail(dependencyTrail);
		}

		public void setDownloadUrl(String downloadUrl) {
			artifact.setDownloadUrl(downloadUrl);
		}

		public void setFile(File destination) {
			artifact.setFile(destination);
		}

		public void setGroupId(String groupId) {
			artifact.setGroupId(groupId);
		}

		public void setOptional(boolean optional) {
			artifact.setOptional(optional);
		}

		public void setRelease(boolean release) {
			artifact.setRelease(release);
		}

		public void setRepository(ArtifactRepository remoteRepository) {
			artifact.setRepository(remoteRepository);
		}

		public void setResolved(boolean resolved) {
			artifact.setResolved(resolved);
		}

		public void setResolvedVersion(String version) {
			artifact.setResolvedVersion(version);
		}

		public void setScope(String scope) {
			artifact.setScope(scope);
		}

		public void setVersion(String version) {
			artifact.setVersion(version);
		}

		public void setVersionRange(VersionRange newRange) {
			artifact.setVersionRange(newRange);
		}

		public void updateVersion(String version, ArtifactRepository localRepository) {
			artifact.updateVersion(version, localRepository);
		}
	}
}
