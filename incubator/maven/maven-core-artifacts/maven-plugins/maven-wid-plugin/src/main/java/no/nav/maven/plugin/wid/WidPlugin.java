package no.nav.maven.plugin.wid;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import no.nav.maven.plugin.wid.writers.WidWtpComponentWriter;
import no.nav.maven.plugin.wid.writers.WidWtpFacetsWriter;

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
	@Override
	protected void writeConfigurationExtras(EclipseWriterConfig eclipseWriterConfig) throws MojoExecutionException {
		super.writeConfigurationExtras(eclipseWriterConfig);

		String packaging = getProject().getPackaging();
		if ("wps-library-jar".equals(packaging) || "wps-module-ear".equals(packaging)) {
			setSourceDirs(eclipseWriterConfig);
			setOutputDir(eclipseWriterConfig);
			writeWtpSettings(eclipseWriterConfig);
		}
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
