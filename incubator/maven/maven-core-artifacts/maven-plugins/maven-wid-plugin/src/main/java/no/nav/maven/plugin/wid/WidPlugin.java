package no.nav.maven.plugin.wid;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.EclipsePlugin;
import org.apache.maven.plugin.eclipse.EclipseSourceDir;
import org.apache.maven.plugin.eclipse.writers.EclipseWriterConfig;

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
	private static final String GEN_SRC = "gen/src";

	@Override
	protected void writeConfigurationExtras(EclipseWriterConfig eclipseWriterConfig) throws MojoExecutionException {
		super.writeConfigurationExtras(eclipseWriterConfig);

		String packaging = getProject().getPackaging();
		if ("wps-library-jar".equals(packaging) || "wps-module-ear".equals(packaging)) {
			Collection<EclipseSourceDir> sourceDirs = new ArrayList<EclipseSourceDir>(2);
			sourceDirs.add(new EclipseSourceDir("", null, false, false, Collections.emptyList(), Arrays.asList(new String[] {
					"gen/", "gen/src/" }), false));
			sourceDirs.add(new EclipseSourceDir(GEN_SRC, null, false, false, Collections.emptyList(), Collections.emptyList(),
					false));
			eclipseWriterConfig.setSourceDirs(sourceDirs.toArray(new EclipseSourceDir[sourceDirs.size()]));
			// Make sure source folder is created
			new File(eclipseWriterConfig.getEclipseProjectDirectory(), GEN_SRC).mkdirs();

			eclipseWriterConfig.setBuildOutputDirectory(eclipseWriterConfig.getEclipseProjectDirectory());
		}
	}
}
