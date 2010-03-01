package no.nav.maven.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactFilterException;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.apache.maven.shared.dependency.tree.filter.AncestorOrSelfDependencyNodeFilter;
import org.apache.maven.shared.dependency.tree.filter.ArtifactDependencyNodeFilter;
import org.apache.maven.shared.dependency.tree.filter.DependencyNodeFilter;
import org.apache.maven.shared.dependency.tree.traversal.CollectingDependencyNodeVisitor;
import org.apache.maven.shared.dependency.tree.traversal.DependencyNodeVisitor;
import org.apache.maven.shared.dependency.tree.traversal.FilteringDependencyNodeVisitor;
import org.apache.maven.shared.dependency.tree.traversal.SerializingDependencyNodeVisitor;

/**
 * List a tree of dependencies for all reactor projects on the same format as
 * dependency:tree, but filter out all artifacts which is not an ancestor of the
 * subject artifacts in the dependency tree
 * 
 * The subject artifacts are specified as a filter property artifactFilter, with
 * the format documented in AbstractStrictPatternArtifactFilter
 * 
 * @goal tree
 * @requiresDependencyResolution compile
 * @aggregator
 */
public class ReverseDependencyMojo extends AbstractMojo {

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	/**
	 * @component
	 * @required
	 * @readonly
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @component
	 * @required
	 * @readonly
	 */
	private ArtifactMetadataSource artifactMetadataSource;

	/**
	 * @component
	 * @required
	 * @readonly
	 */
	private ArtifactCollector artifactCollector;

	/**
	 * @component
	 * @required
	 * @readonly
	 */
	private DependencyTreeBuilder dependencyTreeBuilder;

	/**
	 * @parameter expression="${reactorProjects}"
	 * @required
	 * @readonly
	 */
	private List reactorProjects;

	/**
	 * @parameter expression="${artifactFilter}"
	 * @required
	 */
	private String artifactFilter;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			for (Iterator iterator = reactorProjects.iterator(); iterator
					.hasNext();) {
				MavenProject rProject = (MavenProject) iterator.next();
				DependencyNode rootNode = dependencyTreeBuilder
						.buildDependencyTree(rProject, localRepository,
								artifactFactory, artifactMetadataSource, null,
								artifactCollector);
				String dependencyTreeString = serialiseDependencyTree(rootNode);
				log(dependencyTreeString, getLog());
			}
		} catch (DependencyTreeBuilderException e) {
			throw new MojoExecutionException(
					"Cannot build project dependency tree", e);
		} catch (ArtifactFilterException e) {
			throw new MojoExecutionException("Cannot run artifact filter", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String serialiseDependencyTree(DependencyNode rootNode)
			throws ArtifactFilterException {
		StringWriter writer = new StringWriter();

		DependencyNodeVisitor visitor = new SerializingDependencyNodeVisitor(
				writer, SerializingDependencyNodeVisitor.STANDARD_TOKENS);

		ArrayList<String> patterns = new ArrayList<String>();
		patterns.add(artifactFilter);
		DependencyNodeFilter firstPassFilter = new ArtifactDependencyNodeFilter(
				new StrictPatternIncludesArtifactFilter(patterns));
		CollectingDependencyNodeVisitor collectingVisitor = new CollectingDependencyNodeVisitor();
		DependencyNodeVisitor firstPassVisitor = new FilteringDependencyNodeVisitor(
				collectingVisitor, firstPassFilter);
		rootNode.accept(firstPassVisitor);

		AncestorOrSelfDependencyNodeFilter secondPassFilter = new AncestorOrSelfDependencyNodeFilter(
				collectingVisitor.getNodes());
		visitor = new FilteringDependencyNodeVisitor(visitor, secondPassFilter);

		rootNode.accept(visitor);

		return writer.toString();
	}

	public synchronized static void log(String string, Log log)
			throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(string));
		String line;
		while ((line = reader.readLine()) != null) {
			log.info(line);
		}
		reader.close();
	}

}
