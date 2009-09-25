package no.nav.maven.plugin.wid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import no.nav.maven.plugin.wid.writers.WidWtpComponentWriter;
import no.nav.maven.plugin.wid.writers.WidWtpFacetsWriter;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.EclipsePlugin;
import org.apache.maven.plugin.eclipse.EclipseSourceDir;
import org.apache.maven.plugin.eclipse.writers.EclipseWriterConfig;
import org.apache.maven.plugin.ide.IdeDependency;
import org.apache.maven.project.MavenProject;

/**
 * Generates the wid configuration files.
 * 
 * @author <a href="mailto:test@example.com">Bjorn Hilstad</a>
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 * @extendsPlugin eclipse
 * @extendsGoal eclipse
 * @goal wid
 * @execute phase="generate-resources"
 */
public class WidPlugin extends EclipsePlugin {
	private static final String WPS_MODULE_EAR = "wps-module-ear";
	private static final String WPS_LIBRARY_JAR = "wps-library-jar";

	@Override
	protected void writeConfigurationExtras(EclipseWriterConfig eclipseWriterConfig) throws MojoExecutionException {
		super.writeConfigurationExtras(eclipseWriterConfig);

		String packaging = getProject().getPackaging();
		if (WPS_LIBRARY_JAR.equals(packaging) || WPS_MODULE_EAR.equals(packaging)) {
			setSourceDirs(eclipseWriterConfig);
			setOutputDir(eclipseWriterConfig);

			fixDependencies(eclipseWriterConfig);

			writeWtpSettings(eclipseWriterConfig);
		}
	}

	private void fixDependencies(EclipseWriterConfig eclipseWriterConfig) throws MojoExecutionException {
		// WID does not support dependencies of type 'var' in modules and
		// libraries
		List<IdeDependency> dependencies = new ArrayList<IdeDependency>();
		for (IdeDependency dependency : eclipseWriterConfig.getDeps()) {
			String type = dependency.getType();
			// The OR is just temporary - remove when correct type is added to
			// dependencies
			if (WPS_LIBRARY_JAR.equals(type) || reactorContainsProject(dependency)) {
				dependency.setReferencedProject(true);
				dependency.setEclipseProjectName(dependency.getArtifactId());
				dependency.setFile(null);
				dependency.setJavadocAttachment(null);
				dependency.setSourceAttachment(null);
				dependencies.add(dependency);
			} else if (WPS_MODULE_EAR.equals(getProject().getPackaging())) {
				try {
					FileUtils.copyFileToDirectory(dependency.getFile(), getEclipseProjectDir());
				} catch (IOException e) {
					throw new MojoExecutionException("Unable to copy file", e);
				}
			}
		}
		eclipseWriterConfig.setDeps(dependencies.toArray(new IdeDependency[dependencies.size()]));
	}

	private boolean reactorContainsProject(IdeDependency dependency) {
		for (Object temp : getReactorProjects()) {
			MavenProject reactorProject = (MavenProject) temp;
			if (dependency.getGroupId().equals(reactorProject.getGroupId())
					&& dependency.getArtifactId().equals(reactorProject.getArtifactId())) {
				return true;
			}
		}
		return false;
	}

	private void writeWtpSettings(EclipseWriterConfig eclipseWriterConfig) throws MojoExecutionException {
		new WidWtpComponentWriter().init(getLog(), eclipseWriterConfig).write();
		new WidWtpFacetsWriter().init(getLog(), eclipseWriterConfig).write();
	}

	private void setOutputDir(EclipseWriterConfig eclipseWriterConfig) {
		eclipseWriterConfig.setBuildOutputDirectory(eclipseWriterConfig.getEclipseProjectDirectory());
	}

	private void setSourceDirs(EclipseWriterConfig eclipseWriterConfig) {
		Collection<EclipseSourceDir> sourceDirs = new ArrayList<EclipseSourceDir>(2);
		sourceDirs.add(new EclipseSourceDir("", null, false, false, Collections.emptyList(), Arrays.asList(new String[] {
				"gen/", "gen/src/" }), false));
		sourceDirs.add(new EclipseSourceDir("gen/src", null, false, false, Collections.emptyList(), Collections.emptyList(),
				false));
		eclipseWriterConfig.setSourceDirs(sourceDirs.toArray(new EclipseSourceDir[sourceDirs.size()]));
		// Make sure source folder is created
		new File(eclipseWriterConfig.getEclipseProjectDirectory(), "gen/src").mkdirs();
	}
}
