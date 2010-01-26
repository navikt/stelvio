package no.nav.maven.utilities.sca;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class ScaAttributesBuilderTest {
	@Test
	public void testBuild() throws Exception {
		MavenProject project = new MavenProject();
		List<Dependency> dependencies = new ArrayList<Dependency>();
		Dependency dependency = new Dependency();
		dependency.setType("wps-library-jar");
		dependency.setArtifactId("dep-lib-1");
		dependencies.add(dependency);
		project.setDependencies(dependencies);

		ScaAttributesBuilder scaAttributesBuilder = new ScaAttributesBuilder(
				project);
		StringWriter sw = new StringWriter();
		scaAttributesBuilder.writeTo(sw);
		Assert.assertTrue(sw.toString().length() > 0);
	}
}
