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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.apache.maven.shared.dependency.tree.filter.ArtifactDependencyNodeFilter;
import org.apache.maven.shared.dependency.tree.traversal.DependencyNodeVisitor;
import org.apache.maven.shared.dependency.tree.traversal.FilteringDependencyNodeVisitor;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

/**
 * This plugin builds an assembly (zip-file) that can be used as input to serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy-assembly
 * @requiresDependencyResolution
 */
public class ServiceDeployAssemblyMojo extends AbstractMojo {
	private static final String JAR_CLASSIFIER = "jar";

	/**
	 * @component
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * @component
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @component
	 */
	private ArtifactMetadataSource artifactMetadataSource;

	/**
	 * @component
	 */
	private ArtifactCollector artifactCollector;

	/**
	 * @component
	 */
	private DependencyTreeBuilder dependencyTreeBuilder;

	/**
	 * @component roleHint="zip"
	 */
	private Archiver archiver;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	/**
	 * Defines the service deploy assembly type to use. Only zip is supported.
	 * 
	 * @parameter
	 * @deprecated Parameter is ignored. Only zip is supported.
	 */
	@SuppressWarnings("unused")
	// TODO: Delete variable when all usage is removed.
	private String assemblyType;

	private ProjectArtifactResolver projectArtifactResolver = new ProjectArtifactResolver();

	private ProjectDependencyResolver projectDependencyResolver = new ProjectDependencyResolver();

	private ServiceDeployAssemblyBuilder serviceDeployAssemblyBuilder = new ServiceDeployAssemblyBuilder();

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		for (Iterator iterator = project.getArtifacts().iterator(); iterator.hasNext();) {
			Artifact artifact = (Artifact) iterator.next();
			System.out.println("Artifact="+artifact+";"+artifact.getArtifactHandler().isAddedToClasspath());
		}
		Collection<Artifact> artifacts = new ArrayList<Artifact>();
		for (Artifact artifact : (Collection<Artifact>) project.getAttachedArtifacts()) {
			System.out.println("Attached artifact="+artifact);
			if (JAR_CLASSIFIER.equals(artifact.getClassifier())) {
				artifacts.add(artifact);
				break;
			}
		}
		for (Artifact artifact : (Collection<Artifact>) project.getRuntimeArtifacts()) {
			System.out.println("Runtime artifact="+artifact);
			artifacts.add(artifact);
		}

		projectArtifactResolver.resolveArtifacts(artifacts);
		System.out.println("Artifacts after resolution="+artifacts);
		Map<Artifact, Set<Artifact>> artifactDependencyMap = projectDependencyResolver.getArtifactDependencyMap(artifacts);
		System.out.println("artifactDependencyMap="+artifactDependencyMap);
		serviceDeployAssemblyBuilder.createAssembly(artifactDependencyMap);
	}

	@SuppressWarnings("unchecked")
	private static void addProjectReferences(Map<String, MavenProject> projectReferences, MavenProject project) {
		System.out.println("Find project references for ="+project);
		for (MavenProject projectReference : ((Map<String, MavenProject>) project.getProjectReferences()).values()) {
			String projectReferenceId = getProjectReferenceId(projectReference.getGroupId(), projectReference.getArtifactId(),
					projectReference.getVersion());
			System.out.println("Project Reference="+projectReferenceId);
			projectReferences.put(projectReferenceId, projectReference);
			addProjectReferences(projectReferences, projectReference);
		}
	}

	private static String getProjectReferenceId(String groupId, String artifactId, String version) {
		return groupId + ":" + artifactId + ":" + version;
	}

	private class ProjectArtifactResolver {
		public void resolveArtifacts(Collection<Artifact> artifacts) throws MojoExecutionException {
			Map<String, MavenProject> projectReferences = new LinkedHashMap<String, MavenProject>();
			addProjectReferences(projectReferences, project);

			for (Artifact artifact : artifacts) {
				File artifactFile = artifact.getFile();
				System.out.println("Resolve artifact="+artifactFile);
				if (artifactFile == null) {
					artifact = resolveArtifact(artifact, projectReferences);
				}
			}
		}

		private Artifact resolveArtifact(Artifact artifact, Map<String, MavenProject> projectReferences)
				throws MojoExecutionException {
			// Trying to resolve artifact from project references first. This will
			// work if the artifact to resolve is built as part of this
			// (multi-module) build.
			Artifact resolvedArtifact = resolveArtifactFromProjectReferences(artifact, projectReferences);
			System.out.println("Resolved artifact="+resolvedArtifact);
			if (resolvedArtifact != null) {
				return resolvedArtifact;
			}
			return resolveArtifactFromRepositories(artifact);
		}

		@SuppressWarnings("unchecked")
		private Artifact resolveArtifactFromProjectReferences(Artifact artifact, Map<String, MavenProject> projectReferences)
				throws MojoExecutionException {
			String projectReferenceId = getProjectReferenceId(artifact.getGroupId(), artifact.getArtifactId(), artifact
					.getVersion());
			MavenProject projectReference = projectReferences.get(projectReferenceId);
			if (projectReference != null) {
				getLog().info("Matching project reference found: " + projectReference.getBasedir());
				Artifact projectReferenceArtifact = projectReference.getArtifact();
				if (projectReferenceArtifact.equals(artifact)) {
					getLog().debug("Matching artifact found in project reference: " + projectReferenceArtifact);
					return projectReferenceArtifact;
				}
				for (Artifact attachedArtifact : (Collection<Artifact>) projectReference.getAttachedArtifacts()) {
					if (attachedArtifact.equals(artifact)) {
						getLog().debug("Matching artifact found in project reference: " + projectReferenceArtifact);
						return attachedArtifact;
					}
				}
				throw new MojoExecutionException(
						"Matching project reference found, but a matching artifact was not found in matching project reference.");
			} else {
				return null;
			}
		}

		private Artifact resolveArtifactFromRepositories(Artifact artifact) throws MojoExecutionException {
			try {
				artifactResolver.resolve(artifact, project.getRemoteArtifactRepositories(), localRepository);
				return artifact;
			} catch (AbstractArtifactResolutionException e) {
				throw new MojoExecutionException("Error resolving artifact", e);
			}
		}
	}

	private class ProjectDependencyResolver {
		private ScopeArtifactFilter scopeArtifactFilter = new ScopeArtifactFilter("runtime");

		public Map<Artifact, Set<Artifact>> getArtifactDependencyMap(final Collection<Artifact> artifacts)
				throws MojoExecutionException {
			try {
				final Map<Artifact, Set<Artifact>> artifactDependencyMap = new LinkedHashMap<Artifact, Set<Artifact>>();

				DependencyNodeVisitor visitor = new DependencyNodeVisitor() {
					private Map<String, Artifact> artifactMap;

					public boolean visit(DependencyNode node) {
						Artifact artifact = getArtifact(node);
						if (!artifactDependencyMap.containsKey(artifact)) {
							artifactDependencyMap.put(artifact, new LinkedHashSet<Artifact>());
						}
						return true;
					}

					public boolean endVisit(DependencyNode node) {
						DependencyNode parent = node.getParent();
						if (parent != null) {
							Artifact parentArtifact = getArtifact(parent);
							Set<Artifact> parentDependencies = artifactDependencyMap.get(parentArtifact);

							Artifact artifact = getArtifact(node);
							Set<Artifact> dependencies = artifactDependencyMap.get(artifact);

							parentDependencies.add(artifact);
							parentDependencies.addAll(dependencies);
						}
						return true;
					}

					private Artifact getArtifact(DependencyNode node) {
						if (artifactMap == null) {
							artifactMap = new HashMap<String, Artifact>(artifacts.size());
							for (Artifact artifact : artifacts) {
								artifactMap.put(getArtifactKey(artifact), artifact);
							}
						}
						String artifactKey = getArtifactKey(node.getArtifact());
						Artifact artifact = artifactMap.get(artifactKey);
						return artifact;
					}

					private String getArtifactKey(Artifact artifact) {
						return ArtifactUtils.versionlessKey(artifact);
					}
				};

				DependencyNode rootNode = dependencyTreeBuilder.buildDependencyTree(project, localRepository, artifactFactory,
						artifactMetadataSource, scopeArtifactFilter, artifactCollector);
				visitor = new FilteringDependencyNodeVisitor(visitor, new ArtifactDependencyNodeFilter(scopeArtifactFilter));
				rootNode.accept(visitor);

				return artifactDependencyMap;
			} catch (DependencyTreeBuilderException e) {
				throw new MojoExecutionException("Error resolving dependency tree", e);
			}
		}
	}

	private class ServiceDeployAssemblyBuilder {
		public void createAssembly(Map<Artifact, Set<Artifact>> artifactDependencyMap) throws MojoExecutionException {
			try {
				Build build = project.getBuild();
				File outputFile = new File(build.getDirectory(), build.getFinalName() + "-sd" + ".zip");
				archiver.setDestFile(outputFile);
				File workingDir = createWorkingDir();
				for (Map.Entry<Artifact, Set<Artifact>> entry : artifactDependencyMap.entrySet()) {
					Artifact artifact = entry.getKey();
					Set<Artifact> dependencyArtifacts = entry.getValue();
					File newArtifactFile = createNewArtifactFile(workingDir, artifact, dependencyArtifacts);
					archiver.addFile(newArtifactFile, newArtifactFile.getName());
				}
				archiver.createArchive();
			} catch (ArchiverException e) {
				throw new MojoExecutionException("Error creating service deploy assembly", e);
			} catch (IOException e) {
				throw new MojoExecutionException("Error creating service deploy assembly", e);
			}
		}

		private File createWorkingDir() {
			File parentDir = new File(project.getBuild().getDirectory(), "service-deploy-assembly");
			File workingDir = new File(parentDir, String.valueOf(System.currentTimeMillis()));
			workingDir.mkdirs();
			return workingDir;
		}

		private File createNewArtifactFile(File workingDir, Artifact artifact, Set<Artifact> dependencyArtifacts)
				throws IOException, ArchiverException {
			File newArtifactFile = new File(workingDir, getArtifactFileName(artifact));

			JarFile artifactJarFile = new JarFile(artifact.getFile());

			// Build new classpath string for manifest (including also transitive
			// dependencies)
			StringBuilder classpath = new StringBuilder();
			for (Artifact dependencyArtifact : dependencyArtifacts) {
				if (classpath.length() > 0) {
					classpath.append(" ");
				}
				classpath.append(getArtifactFileName(dependencyArtifact));
			}
			// Update Manifest File
			Manifest manifest = artifactJarFile.getManifest();
			// service-specification has no manifest
			if (manifest == null) {
				manifest = new Manifest();
			}
			Attributes manifestMainAttributes = manifest.getMainAttributes();
			if (classpath.length() > 0) {
				manifestMainAttributes.put(Attributes.Name.CLASS_PATH, classpath.toString());
			} else {
				manifestMainAttributes.remove(Attributes.Name.CLASS_PATH);
			}

			JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(newArtifactFile)), manifest);
			byte[] buf = new byte[1024];
			for (JarEntry jarEntry : Collections.list(artifactJarFile.entries())) {
				if (!"META-INF/MANIFEST.MF".equals(jarEntry.getName())) {
					JarEntry updatedJarEntry = new JarEntry(jarEntry.getName());
					out.putNextEntry(updatedJarEntry);
					InputStream in = new BufferedInputStream(artifactJarFile.getInputStream(updatedJarEntry));
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

		private String getArtifactFileName(Artifact artifact) {
			System.out.println("Artifact!!!!="+artifact);
			String extension = artifact.getArtifactHandler().getExtension();
			// Convert service-specification files from zip to jar
			if ("zip".equals(extension)) {
				extension = "jar";
			}
			return artifact.getArtifactId() + "." + extension;
		}
	}
}
